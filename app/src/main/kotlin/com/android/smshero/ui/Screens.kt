package com.android.smshero.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.smshero.api.ApiClient
import com.android.smshero.data.local.EncryptedPrefs
import com.android.smshero.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ApiKeyInputScreen(nav: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { EncryptedPrefs(context) }
    val scope = rememberCoroutineScope()
    var key by remember { mutableStateOf(prefs.getApiKey().orEmpty()) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(BlackBg).padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("SMSHERO TERANG BULAN", color = RazerGreenBright, fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
        Text("Masukkan API Key HeroSMS Anda", color = DimGray, modifier = Modifier.padding(top = 8.dp, bottom = 32.dp))
        Row(Modifier.fillMaxWidth().padding(bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("> _", color = RazerGreen, fontSize = 18.sp)
            BasicTextField(value = key, onValueChange = { key = it; error = null }, enabled = !loading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), singleLine = true,
                textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 17.sp), modifier = Modifier.weight(1f).padding(start = 8.dp))
        }
        HorizontalDivider(color = if (error == null) RazerGreenDark else AlertRed, thickness = 2.dp)
        error?.let { Text(it, color = AlertRed, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) }
        Button(onClick = {
            if (key.trim().isEmpty()) { error = "API Key tidak boleh kosong"; return@Button }
            loading = true
            scope.launch {
                val result = withContext(Dispatchers.IO) { ApiClient().getBalance(key.trim()) }
                loading = false
                result.fold({ prefs.saveApiKey(key.trim()); nav.navigate("pick_service") }, { error = "API Key tidak valid atau server gagal" })
            }
        }, enabled = !loading, colors = ButtonDefaults.buttonColors(containerColor = RazerGreenDark, contentColor = BlackBg), modifier = Modifier.fillMaxWidth().padding(top = 24.dp), shape = RoundedCornerShape(4.dp)) {
            if (loading) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = BlackBg) else Text("LOGIN / SUBMIT", fontWeight = FontWeight.Bold)
        }
        Text("Belum punya API key? Buka hero-sms.com/profile/safety", color = DimGray, fontSize = 11.sp, modifier = Modifier.padding(top = 18.dp))
    }
}

@Composable
fun PickServiceScreen(nav: NavController) = CatalogScreen("PILIH LAYANAN", listOf("WhatsApp", "Telegram", "Google", "Facebook", "Instagram", "TikTok", "Twitter"), nav, "pick_country")

@Composable
fun PickCountryScreen(nav: NavController) = CatalogScreen("PILIH NEGARA", listOf("Indonesia (+62)", "Malaysia (+60)", "United States (+1)", "United Kingdom (+44)", "India (+91)"), nav, "pick_operator")

@Composable
fun PickOperatorScreen(nav: NavController) = CatalogScreen("PILIH OPERATOR", listOf("Semua operator", "Telkomsel", "Indosat", "XL", "Tri"), nav, "pick_variant")

@Composable
fun PickVariantScreen(nav: NavController) = CatalogScreen("PILIH HARGA", listOf("Rp 2.000 — 1 nomor", "Rp 5.000 — 3 nomor", "Rp 10.000 — 10 nomor"), nav, "input_quantity")

@Composable
private fun CatalogScreen(title: String, items: List<String>, nav: NavController, next: String) {
    var query by remember { mutableStateOf("") }
    val shown = items.filter { it.contains(query, ignoreCase = true) }
    Column(Modifier.fillMaxSize().background(BlackBg).padding(16.dp)) {
        Text(title, color = RazerGreenBright, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(query, { query = it }, label = { Text("Cari...") }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp))
        LazyColumn(Modifier.weight(1f)) { items(shown) { item -> Card(onClick = { nav.navigate(next) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) { Text(item, modifier = Modifier.padding(18.dp), color = MaterialTheme.colorScheme.onSurface) } } }
    }
}

@Composable
fun InputQuantityScreen(nav: NavController) {
    var quantity by remember { mutableStateOf(1) }
    Column(Modifier.fillMaxSize().background(BlackBg).padding(24.dp), Arrangement.Center) {
        Text("KONFIRMASI ORDER", color = RazerGreenBright, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Saldo: memuat...\nHarga: Rp 2.000 / nomor", color = Color.White, modifier = Modifier.padding(vertical = 24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button({ quantity = (quantity - 1).coerceAtLeast(1) }) { Text("−") }; Text("  $quantity  ", color = Color.White, fontSize = 24.sp); Button({ quantity++ }) { Text("+") }
        }
        Button({ nav.navigate("monitor") }, modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) { Text("BELI NOMOR") }
    }
}

@Composable
fun MonitorScreen(nav: NavController) {
    val orders = remember { mutableStateListOf("Menunggu SMS — +62 812 **** 1234", "Menunggu SMS — +62 813 **** 5678") }
    Column(Modifier.fillMaxSize().background(BlackBg).padding(16.dp)) {
        Text("MONITOR ORDER", color = RazerGreenBright, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        LazyColumn(Modifier.weight(1f).padding(top = 12.dp)) { items(orders) { order -> Card(Modifier.fillMaxWidth().padding(vertical = 5.dp)) { Column(Modifier.padding(16.dp)) { Text(order); Text("00:00", color = AlertYellow); Row { TextButton({}) { Text("FINISH") }; TextButton({ orders.remove(order) }) { Text("CANCEL") } } } } } }
        Button({ nav.popBackStack("pick_service", false) }, modifier = Modifier.fillMaxWidth()) { Text("BUY AGAIN") }
    }
}
