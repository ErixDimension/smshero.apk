package com.android.smshero.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.smshero.storage.ConfigStorage
import androidx.compose.ui.platform.LocalContext

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val storage = remember { ConfigStorage(context) }
    var apiKey by remember { mutableStateOf(storage.getApiKey().orEmpty()) }
    var saved by remember { mutableStateOf(apiKey.isNotBlank()) }
    var dark by remember { mutableStateOf(true) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = if (dark) Color.Black else Color(0xFFF5F5F5)) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SMSHERO Terang Bulan", color = if (dark) Color(0xFF00FF00) else Color(0xFF008700))
                    Button(onClick = { dark = !dark }) { Text(if (dark) "Light" else "Dark") }
                }
                if (!saved) {
                    Text("Masukkan API key", color = if (dark) Color.White else Color.Black)
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("API key") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (apiKey.isNotBlank()) {
                                storage.saveApiKey(apiKey.trim())
                                saved = true
                            }
                        },
                        enabled = apiKey.isNotBlank()
                    ) { Text("Simpan & Lanjut") }
                } else {
                    Text("PILIH LAYANAN", color = if (dark) Color(0xFF00FF00) else Color.Black)
                    Text("API key tersimpan aman di Android Keystore", color = if (dark) Color.LightGray else Color.DarkGray)
                    LazyColumn {
                        items(listOf("Google / YouTube / Gmail (go)", "Telegram (tg)", "WhatsApp (wa)")) { item ->
                            Text(item, modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), color = if (dark) Color(0xFF00D700) else Color.Black)
                        }
                    }
                }
            }
        }
    }
}
