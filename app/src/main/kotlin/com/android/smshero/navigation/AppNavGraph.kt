package com.android.smshero.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.smshero.data.local.EncryptedPrefs
import com.android.smshero.ui.*

object Routes {
    const val apiKey = "api_key_input"
    const val dashboard = "dashboard"
    const val service = "pick_service"
    const val country = "pick_country"
    const val operator = "pick_operator"
    const val variant = "pick_variant"
    const val quantity = "input_quantity"
    const val monitor = "monitor"
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val prefs = remember { EncryptedPrefs(context) }
    val apiKey by prefs.apiKey.collectAsStateWithLifecycle()
    NavHost(navController, startDestination = Routes.apiKey) {
        composable(Routes.apiKey) {
            LaunchedEffect(apiKey) {
                if (!apiKey.isNullOrBlank()) {
                    navController.navigate(Routes.dashboard) { popUpTo(Routes.apiKey) { inclusive = true } }
                }
            }
            ApiKeyInputScreen(navController)
        }
        composable(Routes.dashboard) { DashboardScreen(navController, prefs) }
        composable(Routes.service) { PickServiceScreen(navController) }
        composable(Routes.country) { PickCountryScreen(navController) }
        composable(Routes.operator) { PickOperatorScreen(navController) }
        composable(Routes.variant) { PickVariantScreen(navController) }
        composable(Routes.quantity) { InputQuantityScreen(navController) }
        composable(Routes.monitor) { MonitorScreen(navController) }
    }
}
