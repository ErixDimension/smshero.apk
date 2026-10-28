package com.android.smshero.data.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientFactory {
    private val saBase = "https://hero-sms.com/"
    private val v1Base = "https://hero-sms.com/"
    
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
    
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
