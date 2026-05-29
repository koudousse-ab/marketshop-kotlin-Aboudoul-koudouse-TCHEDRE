package com.marketshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.marketshop.navigation.NavGraph
import com.marketshop.ui.theme.MarketShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MarketShopApp() }
    }
}

@Composable
fun MarketShopApp() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("Catalogue", "catalogue", Icons.Default.Home),
        BottomNavItem("Panier", "cart", Icons.Default.ShoppingCart),
        BottomNavItem("Historique", "history", Icons.Default.History),
        BottomNavItem("Profil", "profile", Icons.Default.Person)
    )

    MarketShopTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavGraph(modifier = Modifier.padding(innerPadding), navController = navController, context = LocalContext.current)
        }
    }
}

data class BottomNavItem(val title: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Preview(showBackground = true)
@Composable
fun PreviewMarketShopApp() {
    MarketShopTheme {
        Text("Preview OK")
    }
}
