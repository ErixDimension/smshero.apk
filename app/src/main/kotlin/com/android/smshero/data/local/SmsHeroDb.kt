package com.android.smshero.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.android.smshero.data.model.Order
import com.android.smshero.data.model.OrderStatus
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface OrderDao {
    @Insert suspend fun insert(order: Order)
    @Query("SELECT * FROM orders WHERE status != 'FINISHED' AND status != 'CANCELED'") suspend fun getActive(): List<Order>
    @Query("SELECT * FROM orders ORDER BY boughtAt DESC LIMIT 20") suspend fun getRecent(): List<Order>
    @Query("UPDATE orders SET smsCode = :sms, smsText = :text, status = :status WHERE id = :id") suspend fun updateOtp(id: String, sms: String?, text: String?, status: String)
    @Query("UPDATE orders SET status = :status WHERE id = :id") suspend fun updateStatus(id: String, status: String)
    @Delete suspend fun delete(order: Order)
}

class OrderStatusConverter {
    fun toStatus(val: String) = OrderStatus.valueOf(val)
    fun fromStatus(val: OrderStatus) = val.name
}

@Database(entities = [Order::class], version = 1, exportSchema = false)
@TypeConverters(OrderStatusConverter::class)
abstract class SmsHeroDb : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    companion object {
        fun build(context: Context, prefs: EncryptedPrefs): SmsHeroDb = try {
            val key = prefs.getDatabaseKey()
            Room.databaseBuilder(context, SmsHeroDb::class.java, "smshero_orders")
                .openHelperFactory { sqliteDatabase -> net.zetetic.database.sqlcipher.SQLiteOpenHelper(context, sqliteDatabase, null, key.toByteArray()) }
                .build()
        } catch (e: Exception) {
            Room.databaseBuilder(context, SmsHeroDb::class.java, "smshero_orders_fallback").build()
        }
    }
}
