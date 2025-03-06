package br.com.alura.panucci.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import br.com.alura.panucci.ui.components.BottomAppBarItem

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

val bottomAppBarItems = listOf(
    BottomAppBarItem.Highlightslist,
    BottomAppBarItem.Menu,
    BottomAppBarItem.Drinks
)