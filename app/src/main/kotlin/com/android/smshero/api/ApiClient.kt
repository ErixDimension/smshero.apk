package com.android.smshero.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.HttpUrl.Companion.toHttpUrl
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class ApiClient {
    private val client = OkHttpClient.Builder()
        .dns(DnsOverHttps.Builder()
            .client(OkHttpClient.Builder().build())
            .url("https://1.1.1.1/dns-query".toHttpUrl())
            .includeIPv6(false)
            .build())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", CHROME_ANDROID_USER_AGENT)
                .build()
            chain.proceed(request)
        }
        .build()

    private companion object {
        const val CHROME_ANDROID_USER_AGENT = "Mozilla/5.0 (Linux; Android 13; SM-S908B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36"
    }

    private val gson = Gson()

    private fun makeRequest(url: String): String {
        client.newCall(Request.Builder().url(url).build()).execute().use { response ->
            if (!response.isSuccessful) throw Exception("HTTP ${response.code}")
            return response.body?.string().orEmpty()
        }
    }

    fun getBalance(apiKey: String): Result<Double> = try {
        val body = makeRequest("$SA_BASE?action=getBalance&api_key=$apiKey")
        if (body.startsWith("ACCESS_BALANCE:")) {
            Result.success(body.substring("ACCESS_BALANCE:".length).toDoubleOrNull() ?: 0.0)
        } else Result.failure(Exception("Invalid balance response"))
    } catch (e: Exception) { Result.failure(e) }

    fun verifyApiKey(apiKey: String): Result<Unit> = try {
        val body = makeRequest("$SA_BASE?action=getBalance&api_key=$apiKey")
        if (!body.startsWith("ACCESS_BALANCE:")) throw Exception("Invalid key")
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    data class ServiceCatalog(
        val code: String,
        val name: String,
        val count: Int
    )

    fun getCatalog(apiKey: String): Result<List<ServiceCatalog>> = try {
        val body = makeRequest("$SA_BASE?action=getServicesList&api_key=$apiKey")
        val catalog = gson.fromJson(body, Array<ServiceCatalog>::class.java).toList()
        Result.success(catalog)
    } catch (e: Exception) { Result.failure(e) }
}

private const val SA_BASE = "https://hero-sms.com/stubs/handler_api.php"
