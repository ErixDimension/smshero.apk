package com.android.smshero.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureSessionStore(context: Context) {
    private val key = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "smshero_secure_session",
        key,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveApiKey(value: String) {
        prefs.edit().putString("api_key", value).apply()
    }

    fun getApiKey(): String? = prefs.getString("api_key", null)

    fun clearApiKey() {
        prefs.edit().remove("api_key").apply()
    }

    fun isAuthenticated(): Boolean = !prefs.getString("api_key", null).isNullOrEmpty()

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
