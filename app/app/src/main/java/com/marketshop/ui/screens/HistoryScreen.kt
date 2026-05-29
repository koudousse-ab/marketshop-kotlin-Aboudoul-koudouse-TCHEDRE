package com.marketshop.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.marketshop.data.models.Order
import com.marketshop.navigation.HistoryViewModelFactory
import com.marketshop.ui.theme.MarketShopTheme
import com.marketshop.ui.viewmodels.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = viewModel(factory = HistoryViewModelFactory(LocalContext.current))
) {
    val orders by viewModel.orders.collectAsState()
    val selectedOrder by viewModel.selectedOrder.collectAsState()
    val selectedItems by viewModel.selectedOrderItems.collectAsState()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historique des commandes") },
                navigationIcon = {
                    if (selectedOrder != null) {
                        IconButton(onClick = { viewModel.clearSelectedOrder() }) {
                            Icon(Icons.Default.ArrowBack, null)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (selectedOrder == null) {
            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aucune commande")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(orders) { order ->
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable {
                                viewModel.loadOrderItems(order)
                            }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(order.orderNumber, style = MaterialTheme.typography.titleMedium)
                                    Text(dateFormat.format(Date(order.date)), style = MaterialTheme.typography.bodySmall)
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("${order.totalItems} article(s)")
                                    Text(
                                        "${String.format("%.2f", order.totalAmount)} €",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Commande #${selectedOrder.orderNumber}", style = MaterialTheme.typography.titleMedium)
                            Text("Date: ${dateFormat.format(Date(selectedOrder.date))}")
                            Text("Total: ${String.format("%.2f", selectedOrder.totalAmount)} €")
                            Spacer(Modifier.height(8.dp))
                            Text("Produits commandés:", style = MaterialTheme.typography.titleSmall)
                            selectedItems.forEach { item ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("${item.title} x${item.quantity}")
                                    Text("${String.format("%.2f", item.price * item.quantity)} €")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "History Preview")
@Composable
fun PreviewHistoryScreen() {
    MarketShopTheme {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item {
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("ORD-20240521120000", style = MaterialTheme.typography.titleMedium)
                            Text("21/05/2024 12:00")
                        }
                        Text("2 article(s)")
                        Text("39.98 €", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
