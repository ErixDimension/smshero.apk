package com.android.smshero.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.smshero.ui.*

object Routes {
    const val apiKey = "api_key_input"
    const val service = "pick_service"
    const val country = "pick_country"
    const val operator = "pick_operator"
    const val variant = "pick_variant"
    const val quantity = "input_quantity"
    const val monitor = "monitor"
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Routes.apiKey) {
        composable(Routes.apiKey) { ApiKeyInputScreen(navController) }
        composable(Routes.service) { PickServiceScreen(navController) }
        composable(Routes.country) { PickCountryScreen(navController) }
        composable(Routes.operator) { PickOperatorScreen(navController) }
        composable(Routes.variant) { PickVariantScreen(navController) }
        composable(Routes.quantity) { InputQuantityScreen(navController) }
        composable(Routes.monitor) { MonitorScreen(navController) }
    }
}
