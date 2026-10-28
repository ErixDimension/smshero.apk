package com.android.smshero.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson

class ConfigStorage(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "smshero_config",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    private val gson = Gson()
    
    fun saveApiKey(key: String) {
        prefs.edit().putString("api_key", key).apply()
    }
    
    fun getApiKey(): String? = prefs.getString("api_key", null)
    
    fun saveConfig(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
    
    fun getConfig(key: String, default: String = ""): String {
        return prefs.getString(key, default) ?: default
    }
    
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
