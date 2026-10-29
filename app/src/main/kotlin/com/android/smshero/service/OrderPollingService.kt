package com.android.smshero.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.android.smshero.data.local.EncryptedPrefs
import com.android.smshero.data.local.SmsHeroDb
import com.android.smshero.data.model.OrderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class OrderPollingService : Service() {
    private val serviceJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notification("Monitoring aktif"))
        val orderId = intent?.getStringExtra(EXTRA_ORDER_ID)
        if (orderId != null) startPolling(orderId)
        return START_STICKY
    }

    private fun startPolling(orderId: String) {
        runningJobs.computeIfAbsent(orderId) {
            scope.launch {
                val db = SmsHeroDb.build(this@OrderPollingService, EncryptedPrefs(this@OrderPollingService))
                val dao = db.orderDao()
                while (isActive) {
                    val order = dao.getById(orderId) ?: break
                    if (order.status in setOf(OrderStatus.OTP_RECEIVED, OrderStatus.FINISHED, OrderStatus.CANCELED, OrderStatus.ERROR)) break
                    if (order.expiredAt != null && System.currentTimeMillis() >= order.expiredAt) {
                        dao.updateStatus(orderId, OrderStatus.CANCELED.name)
                        break
                    }
                    // Provider polling belongs in repository/API implementation; no fake OTP is created here.
                    delay(POLL_INTERVAL_MS)
                }
                runningJobs.remove(orderId)
                if (runningJobs.isEmpty()) stopSelf()
            }
        }
    }

    override fun onDestroy() {
        serviceJob.cancel()
        runningJobs.values.forEach(Job::cancel)
        runningJobs.clear()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(NotificationChannel(CHANNEL_ID, "SMSHERO OTP", NotificationManager.IMPORTANCE_LOW))
        }
    }

    private fun notification(text: String) = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("SMSHERO")
        .setContentText(text)
        .setOngoing(true)
        .build()

    companion object {
        const val EXTRA_ORDER_ID = "order_id"
        private const val CHANNEL_ID = "smshero_otp"
        private const val NOTIFICATION_ID = 1001
        private const val POLL_INTERVAL_MS = 5_000L
        private val runningJobs = ConcurrentHashMap<String, Job>()

        fun start(context: Context, orderId: String) {
            val intent = Intent(context, OrderPollingService::class.java).putExtra(EXTRA_ORDER_ID, orderId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(intent) else context.startService(intent)
        }

        fun stop(context: Context, orderId: String) {
            runningJobs.remove(orderId)?.cancel()
            if (runningJobs.isEmpty()) context.stopService(Intent(context, OrderPollingService::class.java))
        }
    }
}
