package com.android.smshero.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

class EncryptedPrefs(context: Context) {
    private val key = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val prefs = EncryptedSharedPreferences.create(context, "smshero_secure", key, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    fun saveApiKey(value: String) { prefs.edit().putString("api_key", value).apply() }
    fun getApiKey(): String? = prefs.getString("api_key", null)
    fun clear() { prefs.edit().clear().apply() }
    fun getDatabaseKey(): String = prefs.getString("database_key", null) ?: ByteArray(32).also { SecureRandom().nextBytes(it) }.joinToString("") { "%02x".format(it) }.also { prefs.edit().putString("database_key", it).apply() }
}
