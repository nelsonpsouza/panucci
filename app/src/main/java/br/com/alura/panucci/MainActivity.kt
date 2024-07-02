package br.com.alura.panucci

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.alura.panucci.navigation.AppDestination
import br.com.alura.panucci.navigation.bottomAppBarItems
import br.com.alura.panucci.preferences.dataStore
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.AuthenticationScreen
import br.com.alura.panucci.ui.screens.CheckoutScreen
import br.com.alura.panucci.ui.screens.DrinksListScreen
import br.com.alura.panucci.ui.screens.HighlightsListScreen
import br.com.alura.panucci.ui.screens.MenuListScreen
import br.com.alura.panucci.ui.screens.ProductDetailsScreen
import br.com.alura.panucci.ui.theme.PanucciTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MainActivity : ComponentActivity() {
    val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = "login")
    val userPreferences = stringPreferencesKey("usuario_logado")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                navController.addOnDestinationChangedListener { _, _, _ ->
                    val routes = navController.backQueue.map {
                        it.destination.route
                    }
                    Log.i("MainActivity", "onCreate: back stack - $routes")
                }
            }
            val backStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntryState?.destination
            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val selectedItem by remember(currentDestination) {
                        val item = currentDestination?.let { destination ->
                            bottomAppBarItems.find {
                                it.destination.route == destination.route
                            }
                        } ?: bottomAppBarItems.first()
                        mutableStateOf(item)
                    }
                    val containsInBottomAppBarItems = currentDestination?.let { destination ->
                        bottomAppBarItems.find {
                            it.destination.route == destination.route
                        }
                    } != null
                    val isShowFab = when (currentDestination?.route) {
                        AppDestination.Menu.route,
                        AppDestination.Drinks.route -> true

                        else -> false
                    }
                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = {
                            val route = it.destination.route
                            navController.navigate(route) {
                                launchSingleTop = true
                                popUpTo(route)
                            }
                        },
                        onFabClick = {
                            navController.navigate(AppDestination.Checkout.route)
                        },
                        onLogout = {
                            scope.launch {
                                context.dataStore.edit {
                                    it.remove(userPreferences)
                                }
                            }
                            navController.navigate(AppDestination.Authentication.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        },
                        isShowTopBar = containsInBottomAppBarItems,
                        isShowBottomBar = containsInBottomAppBarItems,
                        isShowFab = isShowFab
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = AppDestination.HighLight.route
                        ) {
                            composable(AppDestination.Authentication.route) {

                                val context = LocalContext.current
                                val scope = rememberCoroutineScope()
                                AuthenticationScreen(
                                    onEnterClick = { user ->
                                        scope.launch {
                                            context.dataStore.edit {
                                                it[userPreferences] = user
                                            }
                                        }
                                        navController.navigate(AppDestination.HighLight.route) {
                                            popUpTo(navController.graph.id)
                                        }
                                    }
                                )
                            }
                            composable(AppDestination.HighLight.route) {
                                val userPreferences = stringPreferencesKey("usuario_logado")
                                val context = LocalContext.current
                                var user: String? by remember {
                                    mutableStateOf(null)
                                }
                                var dataState by remember {
                                    mutableStateOf("loading")
                                }
                                LaunchedEffect(null) {
                                    user = context.dataStore.data.first()[userPreferences]
                                    dataState = "finished"
                                }
                                when (dataState) {
                                    "loading" -> {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Text(
                                                text = "Carregando...",
                                                Modifier
                                                    .fillMaxWidth()
                                                    .align(Alignment.Center),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    "finished" -> {
                                        user?.let {
                                            HighlightsListScreen(
                                                products = sampleProducts,
                                                onNavigateToDetails = { product ->
                                                    val promoCode = "ALURA"
                                                    navController.navigate(
                                                        "${AppDestination.ProductDetails.route}/${product.id}?promoCode=${promoCode}"
                                                    )
                                                },
                                                onNavigateToCheckout = {
                                                    navController.navigate(AppDestination.Checkout.route)
                                                }
                                            )
                                        } ?: LaunchedEffect(null) {
                                            navController.navigate(AppDestination.Authentication.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            composable(AppDestination.Menu.route) {
                                MenuListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = { product ->
                                        navController.navigate(
                                            "${AppDestination.ProductDetails.route}/${product.id}"
                                        )
                                    },
                                )
                            }
                            composable(AppDestination.Drinks.route) {
                                DrinksListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = { product ->
                                        navController.navigate(
                                            "${AppDestination.ProductDetails.route}/${product.id}"
                                        )
                                    },
                                )
                            }
                            composable(
                                route = "${AppDestination.ProductDetails.route}/{productId}?promoCode={promoCode}",
                                arguments = listOf(navArgument("promoCode") { nullable = true })
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("productId")
                                val promoCode = backStackEntry.arguments?.getString("promoCode")
                                sampleProducts.find { p ->
                                    p.id == id
                                }?.let { product ->
                                    val discount = when (promoCode) {
                                        "ALURA" -> BigDecimal("0.1")
                                        else -> BigDecimal.ZERO
                                    }

                                    val currentPrice = product.price

                                    ProductDetailsScreen(
                                        product = product.copy(price = currentPrice - (currentPrice * discount)),
                                        onNavigateToCheckout = {
                                            navController.navigate(AppDestination.Checkout.route)
                                        }
                                    )
                                } ?: LaunchedEffect(Unit) {
                                    navController.navigateUp()
                                }
                            }
                            composable(AppDestination.Checkout.route) {
                                CheckoutScreen(
                                    products = sampleProducts,
                                    onPopBackStack = {
                                        navController.navigateUp()
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    isShowFab: Boolean = false,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (isShowTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Ristorante Panucci")
                    },
                    actions = {
                        IconButton(onClick = onLogout) {
                            Icon(
                                Icons.Filled.ExitToApp,
                                contentDescription = "sair do app"
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isShowBottomBar) {
                PanucciBottomAppBar(
                    item = bottomAppBarItemSelected,
                    items = bottomAppBarItems,
                    onItemChange = onBottomAppBarItemSelectedChange,
                )
            }
        },
        floatingActionButton = {
            if (isShowFab) {
                FloatingActionButton(
                    onClick = onFabClick
                ) {
                    Icon(
                        Icons.Filled.PointOfSale,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PanucciAppPreview() {
    PanucciTheme {
        Surface {
            PanucciApp {}
        }
    }
}