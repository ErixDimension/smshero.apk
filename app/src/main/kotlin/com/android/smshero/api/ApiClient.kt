package com.android.smshero.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.concurrent.TimeUnit

class ApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()
    
    private val gson = Gson()
    private val baseUrl = "https://hero-sms.com/api/v1"
    private val saBase = "https://hero-sms.com/stubs/handler_api.php"
    
    fun getBalance(apiKey: String): Result<Double> = try {
        val params = "api_key=$apiKey&action=getBalance"
        val request = Request.Builder()
            .url("$saBase?$params")
            .build()
        
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("HTTP ${response.code}")
        
        val body = response.body?.string() ?: ""
        if (body.startsWith("ACCESS_BALANCE:")) {
            val balance = body.substring("ACCESS_BALANCE:".length).toDoubleOrNull() ?: 0.0
            Result.success(balance)
        } else {
            Result.failure(Exception("Invalid balance response: $body"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    data class ServiceCatalog(
        val code: String,
        val name: String,
        val count: Int
    )
    
    fun getCatalog(apiKey: String): Result<List<ServiceCatalog>> = try {
        val request = Request.Builder()
            .url("$baseUrl/catalog?api_key=$apiKey")
            .build()
        
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("HTTP ${response.code}")
        
        val jsonStr = response.body?.string() ?: "[]"
        val catalog = gson.fromJson(jsonStr, Array<ServiceCatalog>::class.java).toList()
        Result.success(catalog)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
