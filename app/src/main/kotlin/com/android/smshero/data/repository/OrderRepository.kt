package com.android.smshero.data.repository

import com.android.smshero.data.api.ApiClientFactory
import com.android.smshero.data.model.Order
import com.android.smshero.data.model.OrderStatus

class OrderRepository {
    private val legacy = ApiClientFactory.legacyApi()
    private val v1 = ApiClientFactory.v1Api()

    suspend fun buyOrder(apiKey: String, service: String, country: Int, quantity: Int, price: Double, operator: String?): Result<List<Order>> = try {
        val authHeader = "ApiKey $apiKey"
        val body = mapOf(
            "service" to service,
            "country" to country,
            "amount" to quantity,
            "maxPrice" to price,
            "fixedPrice" to true,
            "operator" to (operator ?: "any")
        )
        var resp = v1.buyActivations(authHeader, body)
        if (resp.isSuccessful && resp.body() != null) {
            val data = resp.body() as? Map<*, *> ?: return Result.failure(Exception("Invalid"))
            val orders = (data["data"] as? List<*>)?.mapNotNull { o ->
                val m = o as? Map<*, *> ?: return@mapNotNull null
                Order(
                    id = m["id"].toString(),
                    phone = m["phone"].toString(),
                    service = service,
                    serviceName = "",
                    country = country,
                    countryName = "",
                    countryPhoneCode = m["countryPhoneCode"]?.toString() ?: "",
                    operator = operator,
                    price = price,
                    status = OrderStatus.WAITING,
                    expiredAt = m["expiredAt"]?.toString()?.let { System.currentTimeMillis() + 20 * 60 * 1000 } ?: (System.currentTimeMillis() + 20 * 60 * 1000)
                )
            } ?: emptyList()
            return Result.success(orders)
        }
        Result.failure(Exception("HTTP ${resp.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun pollOrders(apiKey: String, service: String, country: Int): Result<List<Order>> = try {
        val authHeader = "ApiKey $apiKey"
        val resp = v1.getActiveActivations(authHeader, service, country.toString())
        if (resp.isSuccessful && resp.body() != null) {
            val data = resp.body() as? Map<*, *> ?: return Result.failure(Exception("Invalid"))
            val orders = (data["data"] as? List<*>)?.mapNotNull { o ->
                val m = o as? Map<*, *> ?: return@mapNotNull null
                val otp = m["otpLast"] as? Map<*, *>
                Order(
                    id = m["id"].toString(),
                    phone = m["phone"].toString(),
                    service = m["service"].toString(),
                    serviceName = "",
                    country = m["country"].toString().toIntOrNull() ?: 0,
                    countryName = "",
                    countryPhoneCode = m["countryPhoneCode"]?.toString() ?: "",
                    status = if (otp?.get("smsCode") != null) OrderStatus.OTP_RECEIVED else OrderStatus.WAITING,
                    smsCode = otp?.get("smsCode")?.toString(),
                    smsText = otp?.get("smsText")?.toString()
                )
            } ?: emptyList()
            return Result.success(orders)
        }
        Result.failure(Exception("HTTP ${resp.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun finishOrder(apiKey: String, orderId: String): Result<Unit> = try {
        val authHeader = "ApiKey $apiKey"
        val resp = v1.finishActivation(authHeader, orderId)
        if (resp.isSuccessful) Result.success(Unit) else Result.failure(Exception("HTTP ${resp.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun cancelOrder(apiKey: String, orderId: String): Result<Unit> = try {
        val authHeader = "ApiKey $apiKey"
        val resp = v1.cancelActivation(authHeader, orderId)
        if (resp.isSuccessful) Result.success(Unit) else Result.failure(Exception("HTTP ${resp.code()}"))
    } catch (e: Exception) { Result.failure(e) }
}
