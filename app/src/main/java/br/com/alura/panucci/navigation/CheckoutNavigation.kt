package br.com.alura.panucci.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.CheckoutScreen

private const val CHECKOUT_ROUTE = "checkout"

fun NavGraphBuilder.checkoutScreen(navController: NavHostController) {
    composable(route = CHECKOUT_ROUTE) {
        CheckoutScreen(
            products = sampleProducts,
            onPopBackStack = {
                navController.navigateUp()
            },
        )
    }
}

fun NavHostController.navigateToCheckout() {
    navigate(CHECKOUT_ROUTE)
}