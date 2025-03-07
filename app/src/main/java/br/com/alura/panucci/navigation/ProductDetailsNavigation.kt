package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.ProductDetailsScreen

private const val PRODUCT_DETAILS_ROUTE = "productDetails"
private const val PRODUCT_ID_ARGUMENT = "productId"

fun NavGraphBuilder.productDetailsScreen(navController: NavHostController) {
    composable(
        "$PRODUCT_DETAILS_ROUTE/{$PRODUCT_ID_ARGUMENT}"
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString(PRODUCT_ID_ARGUMENT)
        sampleProducts.find {
            it.id == id
        }?.let { product ->
            ProductDetailsScreen(
                product = product,
                onNavigateToCheckout = {
                    navController.navigateToCheckout()
                },
            )
        } ?: LaunchedEffect(Unit) {
            navController.navigateUp()
        }
    }
}

fun NavController.navigateToProductDetails(productId: String) {
    navigate("$PRODUCT_DETAILS_ROUTE/$productId")
}