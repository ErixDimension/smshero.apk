package com.android.smshero.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.smshero.data.local.EncryptedPrefs
import com.android.smshero.data.local.ThemePreferences
import com.android.smshero.ui.theme.SmsHeroTheme

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val prefs = remember { EncryptedPrefs(context) }
    val themePrefs = remember { ThemePreferences(context) }
    var apiKey by remember { mutableStateOf(prefs.getApiKey().orEmpty()) }
    var dark by remember { mutableStateOf(themePrefs.isDark()) }
    var hasKey by remember { mutableStateOf(apiKey.isNotBlank()) }
    SmsHeroTheme(darkTheme = dark) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("SMSHERO Terang Bulan", color = MaterialTheme.colorScheme.primary)
                TextButton(onClick = { dark = !dark; themePrefs.saveTheme(dark) }) { Text(if (dark) "Light" else "Dark") }
            }
            if (!hasKey) {
                Text("Masukkan API Key")
                OutlinedTextField(value = apiKey, onValueChange = { apiKey = it }, label = { Text("API Key") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Button(onClick = { prefs.saveApiKey(apiKey.trim()); hasKey = true }, enabled = apiKey.isNotBlank()) { Text("Simpan & Lanjut") }
            } else {
                Text("PILIH LAYANAN")
            }
        }
    }
}
