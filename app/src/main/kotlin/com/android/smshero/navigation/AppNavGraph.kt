package com.android.smshero.navigation

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val apiKey = "api_key_input"
    const val service = "pick_service"
    const val country = "pick_country/{serviceCode}"
    const val operator = "pick_operator/{serviceCode}/{countryId}/{countryName}"
    const val variant = "pick_variant/{serviceCode}/{countryId}/{countryName}/{operator}"
    const val quantity = "input_quantity/{draft}"
    const val monitor = "monitor"
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Routes.apiKey) {
        composable(Routes.apiKey) { Placeholder("Api Key Input") }
        composable(Routes.service) { Placeholder("Pick Service") }
        composable(Routes.country) { Placeholder("Pick Country") }
        composable(Routes.operator) { Placeholder("Pick Operator") }
        composable(Routes.variant) { Placeholder("Pick Variant") }
        composable(Routes.quantity) { Placeholder("Input Quantity") }
        composable(Routes.monitor) { Placeholder("Monitor") }
    }
}

@Composable
private fun Placeholder(label: String) { Text(label) }
