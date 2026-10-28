package com.android.smshero.data.local

import android.content.Context

class ThemePreferences(context: Context) {
    private val prefs = context.getSharedPreferences("smshero_theme", Context.MODE_PRIVATE)
    fun saveTheme(dark: Boolean) { prefs.edit().putBoolean("dark", dark).apply() }
    fun isDark(): Boolean = prefs.getBoolean("dark", true)
}
