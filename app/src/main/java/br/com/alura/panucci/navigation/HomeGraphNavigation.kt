package br.com.alura.panucci.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import androidx.navigation.navigation
import br.com.alura.panucci.ui.components.BottomAppBarItem

internal const val HOME_GRAPH_ROUTE = "home"

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(
        startDestination = HIGHLIGHTS_LIST_ROUTE,
        route = HOME_GRAPH_ROUTE
    ) {
        highlightsListScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)
    }
}

fun NavController.navigateToHomeGraph(){
    navigate(HOME_GRAPH_ROUTE)
}

fun NavController.navigateWithSingleTopAndPopUpTo(
    item: BottomAppBarItem
) {
    val (route, navigate) = when (item) {
        BottomAppBarItem.Menu -> Pair(
            MENU_ROUTE,
            ::navigateToMenu
        )

        BottomAppBarItem.Drinks -> Pair(
            DRINKS_ROUTE,
            ::navigateToDrinks
        )

        BottomAppBarItem.Highlightslist -> Pair(
            HIGHLIGHTS_LIST_ROUTE,
            ::navigateToHighlightsList
        )
    }

    val navOptions = navOptions {
        launchSingleTop = true
        popUpTo(route)
    }

    navigate(navOptions)
}