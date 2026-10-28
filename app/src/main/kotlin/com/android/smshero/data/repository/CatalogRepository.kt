package com.android.smshero.data.repository

import com.android.smshero.data.api.ApiClientFactory
import com.android.smshero.data.model.Country
import com.android.smshero.data.model.Operator
import com.android.smshero.data.model.PriceVariant
import com.android.smshero.data.model.Service

class CatalogRepository {
    private val legacy = ApiClientFactory.legacyApi()
    private val v1 = ApiClientFactory.v1Api()

    suspend fun getServices(apiKey: String): Result<List<Service>> {
        return try {
            val resp = legacy.getServicesList(apiKey)
            if (resp.isSuccessful && resp.body() != null) {
                val data = resp.body() as? Map<*, *> ?: return Result.failure(Exception("Invalid format"))
                val services = (data["services"] as? List<*>)?.mapNotNull { item ->
                    val m = item as? Map<*, *> ?: return@mapNotNull null
                    Service(code = m["code"].toString(), name = m["name"].toString())
                } ?: emptyList()
                Result.success(services)
            } else {
                Result.failure(Exception("HTTP ${resp.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCountries(apiKey: String): Result<List<Country>> {
        return try {
            val resp = legacy.getCountries(apiKey)
            if (resp.isSuccessful && resp.body() != null) {
                val countries = resp.body()!!.filterValues { (it as? Map<*, *>)?.get("visible") == 1 }.values.toList()
                Result.success(countries)
            } else {
                Result.failure(Exception("HTTP ${resp.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOperators(apiKey: String, country: Int): Result<List<Operator>> {
        return try {
            val resp = legacy.getOperators(apiKey, country = country)
            if (resp.isSuccessful && resp.body() != null) {
                val data = resp.body() as? Map<*, *> ?: return Result.success(listOf(Operator("Semua operator", true)))
                val ops = (data["countryOperators"] as? Map<*, *>)?.get(country.toString()) as? List<*> ?: emptyList()
                val result = listOf(Operator("Semua operator", true)) + ops.mapNotNull { it?.toString()?.let { o -> Operator(o) } }
                Result.success(result)
            } else {
                Result.success(listOf(Operator("Semua operator", true)))
            }
        } catch (e: Exception) {
            Result.success(listOf(Operator("Semua operator", true)))
        }
    }

    suspend fun getPriceVariants(apiKey: String, service: String, country: Int): Result<List<PriceVariant>> {
        return try {
            val authHeader = "ApiKey $apiKey"
            val resp = v1.getPriceOffers(authHeader, service, country.toString())
            if (resp.isSuccessful && resp.body() != null) {
                val data = resp.body() as? Map<*, *> ?: return Result.failure(Exception("Invalid"))
                val map = ((data["data"] as? Map<*, *>)?.get(service) as? Map<*, *>)?.get(country.toString()) as? Map<*, *> ?: return Result.failure(Exception("No data"))
                val priceMap = map["map"] as? Map<*, *> ?: emptyMap()
                return Result.success(priceMap.map { (k, v) -> PriceVariant(k.toString().toDouble(), v.toString().toInt()) }.sortedBy { it.price })
            }
            val resp2 = legacy.getPrices(apiKey, service = service, country = country)
            if (resp2.isSuccessful && resp2.body() != null) {
                val data = resp2.body() as? Map<*, *> ?: return Result.failure(Exception("Invalid"))
                val item = (data[country.toString()] as? Map<*, *>)?.get(service) as? Map<*, *>
                return Result.success(listOf(PriceVariant(item?.get("cost").toString().toDouble(), item?.get("count").toString().toInt())))
            }
            Result.failure(Exception("All fallbacks exhausted"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
