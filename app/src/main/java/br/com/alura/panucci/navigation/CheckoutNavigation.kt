package br.com.alura.panucci.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.ui.screens.CheckoutScreen
import br.com.alura.panucci.ui.viewmodels.CheckoutViewModel

private const val CHECKOUT_ROUTE = "checkout"

fun NavGraphBuilder.checkoutScreen(navController: NavHostController) {
    composable(route = CHECKOUT_ROUTE) {
        val viewModel = viewModel<CheckoutViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        CheckoutScreen(
            uiState = uiState,
            onPopBackStack = {
                navController.navigateUp()
            },
        )
    }
}

fun NavHostController.navigateToCheckout() {
    navigate(CHECKOUT_ROUTE)
}