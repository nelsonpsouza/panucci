package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun PanucciNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AUTHENTICATION_ROUTE
    ) {
        authenticationScreen(navController)
        highlightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)
        productDetailsScreen(navController)
        checkoutScreen(navController)
    }
}