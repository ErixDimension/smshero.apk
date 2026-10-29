package com.android.smshero.data.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientFactory {
    private val saBase = "https://hero-sms.com/"
    private val v1Base = "https://hero-sms.com/"
    
    private val httpClient = OkHttpClient.Builder()
        .dns(DnsOverHttps.Builder()
            .client(OkHttpClient.Builder().build())
            .url("https://1.1.1.1/dns-query".toHttpUrl())
            .includeIPv6(false)
            .build())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", CHROME_ANDROID_USER_AGENT)
                .build()
            chain.proceed(request)
        }
        .build()

    private const val CHROME_ANDROID_USER_AGENT = "Mozilla/5.0 (Linux; Android 13; SM-S908B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36"
    
    private val gson = Gson()
    
    fun legacyApi(): HeroSmsLegacyApi = Retrofit.Builder()
        .baseUrl(saBase)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(HeroSmsLegacyApi::class.java)
    
    fun v1Api(): HeroSmsV1Api = Retrofit.Builder()
        .baseUrl(v1Base)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(HeroSmsV1Api::class.java)
}
