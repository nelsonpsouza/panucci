package br.com.alura.panucci.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.alura.panucci.ui.screens.HighlightsListScreen
import br.com.alura.panucci.ui.viewmodels.HighlightsListViewModel

internal const val HIGHLIGHTS_LIST_ROUTE = "highlights"

fun NavGraphBuilder.highlightsListScreen(navController: NavHostController) {
    composable(HIGHLIGHTS_LIST_ROUTE) {
        val viewModel = viewModel<HighlightsListViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        HighlightsListScreen(
            uiState = uiState,
            onNavigateToDetails = { product ->
                navController.navigateToProductDetails(product.price.toPlainString())
            },
            onNavigateToCheckout = {
                navController.navigateToCheckout()
            },
        )
    }
}

fun NavController.navigateToHighlightsList(navOptions: NavOptions? = null) {
    navigate(HIGHLIGHTS_LIST_ROUTE, navOptions)
}