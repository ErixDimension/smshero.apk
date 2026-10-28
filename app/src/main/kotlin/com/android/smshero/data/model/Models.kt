package com.android.smshero.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Service(val code: String, val name: String)

@Serializable
data class Country(val id: Int, val eng: String, val rus: String = "", val visible: Int = 1) {
    val displayName: String get() = eng.ifBlank { rus.ifBlank { "#$id" } }
}

data class Operator(val name: String, val isAny: Boolean = false)
data class PriceVariant(val price: Double, val count: Int, val tier: Int = 1, val totalTiers: Int = 1)
enum class OrderStatus { WAITING, OTP_RECEIVED, FINISHED, CANCELED, ERROR }

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val id: String,
    val phone: String,
    val service: String,
    val serviceName: String,
    val country: Int,
    val countryName: String,
    val countryPhoneCode: String = "",
    val operator: String? = null,
    val price: Double,
    val currency: Int = 840,
    val boughtAt: Long = System.currentTimeMillis(),
    val expiredAt: Long? = null,
    val status: OrderStatus = OrderStatus.WAITING,
    val smsCode: String? = null,
    val smsText: String? = null,
)

data class OrderDraft(val service: String, val serviceName: String, val country: Int, val countryName: String, val operator: String?, val price: Double, val quantity: Int)
