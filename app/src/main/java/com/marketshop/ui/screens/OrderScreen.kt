package com.marketshop.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.marketshop.ui.theme.MarketShopTheme

@Composable
fun OrderScreen(navController: NavController) {
    Text("Order Screen")
}

@Preview(showBackground = true)
@Composable
fun PreviewOrderScreen() {
    MarketShopTheme {
        Text("Order Preview")
    }
}
