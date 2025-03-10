package br.com.alura.panucci.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import br.com.alura.panucci.ui.viewmodels.ProductDetailsViewModel

private const val PRODUCT_DETAILS_ROUTE = "productDetails"
private const val PRODUCT_ID_ARGUMENT = "productId"

fun NavGraphBuilder.productDetailsScreen(navController: NavHostController) {
    composable(
        "$PRODUCT_DETAILS_ROUTE/{$PRODUCT_ID_ARGUMENT}"
    ) { backStackEntry ->

        val viewModel = viewModel<ProductDetailsViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        backStackEntry.arguments?.getString(PRODUCT_ID_ARGUMENT)?.let { id ->
            LaunchedEffect(Unit) {
                viewModel.findProductById(id)
            }

            ProductDetailsScreen(
                uiState = uiState,
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