package br.com.alura.panucci.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.alura.panucci.preferences.dataStore
import br.com.alura.panucci.ui.screens.AuthenticationScreen
import kotlinx.coroutines.launch

internal const val AUTHENTICATION_ROUTE = "authentication"
private const val USER_PREFERENCES = "user_preferences"
private val userPreferences = stringPreferencesKey(USER_PREFERENCES)

fun NavGraphBuilder.authenticationScreen(navController: NavHostController) {
    composable(AUTHENTICATION_ROUTE) {

        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        AuthenticationScreen(
            onEnterClick = { user ->
                scope.launch {
                    context.dataStore.edit {
                        it[userPreferences] = user
                    }
                }
                navController.navigateToHighlightsList()
            }
        )
    }
}

fun NavHostController.navigateToAuthentication() {
    navigate(AUTHENTICATION_ROUTE, {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
    })
}