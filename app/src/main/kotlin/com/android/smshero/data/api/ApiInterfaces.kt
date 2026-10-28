package com.android.smshero.data.api

import com.android.smshero.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface HeroSmsLegacyApi {
    @GET("stubs/handler_api.php")
    suspend fun getBalance(@Query("api_key") key: String, @Query("action") action: String = "getBalance"): Response<String>

    @GET("stubs/handler_api.php")
    suspend fun getServicesList(@Query("api_key") key: String, @Query("action") action: String = "getServicesList"): Response<Map<String, Any>>

    @GET("stubs/handler_api.php")
    suspend fun getCountries(@Query("api_key") key: String, @Query("action") action: String = "getCountries"): Response<Map<String, Country>>

    @GET("stubs/handler_api.php")
    suspend fun getOperators(@Query("api_key") key: String, @Query("action") action: String = "getOperators", @Query("country") country: Int): Response<Map<String, Any>>

    @GET("stubs/handler_api.php")
    suspend fun getNumber(@Query("api_key") key: String, @Query("action") action: String = "getNumber", @Query("service") service: String, @Query("country") country: Int, @Query("maxPrice") maxPrice: Double?, @Query("operator") operator: String?): Response<String>

    @GET("stubs/handler_api.php")
    suspend fun getStatus(@Query("api_key") key: String, @Query("action") action: String = "getStatus", @Query("id") id: String): Response<String>

    @GET("stubs/handler_api.php")
    suspend fun setStatus(@Query("api_key") key: String, @Query("action") action: String = "setStatus", @Query("id") id: String, @Query("status") status: Int): Response<String>

    @GET("stubs/handler_api.php")
    suspend fun getPrices(@Query("api_key") key: String, @Query("action") action: String = "getPrices", @Query("service") service: String, @Query("country") country: Int): Response<Map<String, Any>>

    @GET("stubs/handler_api.php")
    suspend fun getTopCountriesByService(@Query("api_key") key: String, @Query("action") action: String = "getTopCountriesByService", @Query("service") service: String): Response<List<Map<String, Any>>>
}

interface HeroSmsV1Api {
    @GET("api/v1/activations/offers/sms")
    suspend fun getPriceOffers(@Header("Authorization") auth: String, @Query("services") services: String, @Query("countries") countries: String): Response<Map<String, Any>>

    @POST("api/v1/activations")
    suspend fun buyActivations(@Header("Authorization") auth: String, @Body body: Map<String, Any>): Response<Map<String, Any>>

    @GET("api/v1/activations")
    suspend fun getActiveActivations(@Header("Authorization") auth: String, @Query("services") services: String?, @Query("countries") countries: String?, @Query("size") size: Int = 10, @Query("page") page: Int = 1): Response<Map<String, Any>>

    @GET("api/v1/activations/{id}/otp/last")
    suspend fun getLastOtp(@Header("Authorization") auth: String, @Path("id") id: String): Response<Map<String, Any>>

    @POST("api/v1/activations/{id}/finish")
    suspend fun finishActivation(@Header("Authorization") auth: String, @Path("id") id: String): Response<Unit>

    @DELETE("api/v1/activations/{id}")
    suspend fun cancelActivation(@Header("Authorization") auth: String, @Path("id") id: String): Response<Unit>
}
