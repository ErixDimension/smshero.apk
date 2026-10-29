package com.android.smshero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.smshero.data.local.EncryptedPrefs
import com.android.smshero.navigation.Routes

@Composable
fun DashboardScreen(nav: NavController, prefs: EncryptedPrefs) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("SMSHERO", style = MaterialTheme.typography.titleLarge)
            Button(onClick = {
                prefs.clearApiKey()
                nav.navigate(Routes.apiKey) { popUpTo(0) }
            }) { Text("Logout / Ganti Akun") }
        }
        Text("PILIH LAYANAN", style = MaterialTheme.typography.titleLarge)
        Button(onClick = { nav.navigate(Routes.service) }, modifier = Modifier.fillMaxWidth()) {
            Text("Lihat layanan")
        }
    }
}
