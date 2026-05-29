package com.marketshop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.marketshop.ui.screens.*

@Composable
fun NavGraph(modifier: Modifier = Modifier, navController: NavController) {
    NavHost(navController = navController, startDestination = "catalogue", modifier = modifier) {
        composable("catalogue") { CatalogueScreen(navController) }
        composable("cart") { CartScreen(navController) }
        composable("order") { OrderScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable(
            "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(navController, id)
        }
    }
}
