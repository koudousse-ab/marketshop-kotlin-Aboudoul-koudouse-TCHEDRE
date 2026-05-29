package com.marketshop.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.marketshop.ui.screens.*

@Composable
fun NavGraph(modifier: Modifier = Modifier, navController: NavHostController, context: Context) {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = !prefs.getString("user_name", "").isNullOrEmpty()
    val startDestination = if (isLoggedIn) "catalogue" else "login"

    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable("login") { LoginScreen(navController) }
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
