package com.marketshop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marketshop.navigation.CartViewModelFactory
import com.marketshop.ui.components.CartItemRow
import com.marketshop.ui.theme.MarketShopTheme
import com.marketshop.ui.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = viewModel(factory = CartViewModelFactory(LocalContext.current))
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val total by viewModel.total.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Mon panier") }) }) { padding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Votre panier est vide")
            }
        } else {
            Column(modifier = Modifier.padding(padding)) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            cartItem = item,
                            onQuantityChange = { newQuantity ->
                                viewModel.updateQuantity(item, newQuantity)
                            },
                            onRemove = { viewModel.removeItem(item) }
                        )
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Total:", style = MaterialTheme.typography.titleLarge)
                            Text(
                                "${String.format("%.2f", total)} €",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate("order") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Passer commande")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    MarketShopTheme {
        CartScreen(navController = rememberNavController())
    }
}
