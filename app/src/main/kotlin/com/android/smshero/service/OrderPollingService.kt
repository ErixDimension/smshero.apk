package com.android.smshero.service

import android.app.Service
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.android.smshero.api.ApiClient
import com.android.smshero.data.local.EncryptedPrefs
import com.android.smshero.data.local.SmsHeroDb
import com.android.smshero.data.model.OrderStatus
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class OrderPollingService : Service() {
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel("smshero", "SMSHERO", NotificationManager.IMPORTANCE_LOW).apply { 
                (getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(this) 
            }; "smshero"
        } else "smshero"
        startForeground(1, NotificationCompat.Builder(this, channel).setContentTitle("SMSHERO — Monitoring...").setSmallIcon(android.R.drawable.ic_dialog_info).build())
        scope.launch {
            val db = SmsHeroDb.build(this@OrderPollingService, EncryptedPrefs(this@OrderPollingService))
            val dao = db.orderDao()
            val client = ApiClient()
            val prefs = EncryptedPrefs(this@OrderPollingService)
            val key = prefs.getApiKey() ?: return@launch
            while (isActive) {
                try {
                    val orders = dao.getActive()
                    for (order in orders) {
                        try {
                            if (order.expiredAt != null && System.currentTimeMillis() > order.expiredAt!! - 2 * 60 * 1000) {
                                dao.updateStatus(order.id, OrderStatus.CANCELED.name)
                                continue
                            }
                            val resp = client.getBalance(key)
                            resp.fold({ b -> }, { e -> throw e })
                        } catch (e: Exception) { }
                    }
                } catch (e: Exception) { }
                delay(5000)
            }
        }
        return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() { scope.cancel(); super.onDestroy() }
}

class PollingWorker(ctx: android.content.Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result = try {
        applicationContext.startService(Intent(applicationContext, OrderPollingService::class.java))
        Result.success()
    } catch (e: Exception) { Result.retry() }
}

fun schedulePolling(ctx: android.content.Context) {
    val work = PeriodicWorkRequestBuilder<PollingWorker>(5, TimeUnit.SECONDS).build()
    WorkManager.getInstance(ctx).enqueueUniquePeriodicWork("order_polling", ExistingPeriodicWorkPolicy.KEEP, work)
}
