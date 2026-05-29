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
import com.marketshop.navigation.HistoryViewModelFactory
import com.marketshop.ui.theme.MarketShopTheme
import com.marketshop.ui.viewmodels.HistoryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = viewModel(factory = HistoryViewModelFactory(LocalContext.current))
) {
    val orders by viewModel.orders.collectAsState()
    val selectedOrder by viewModel.selectedOrder.collectAsState()
    val selectedItems by viewModel.selectedOrderItems.collectAsState()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historique des commandes") },
                navigationIcon = {
                    if (selectedOrder != null) {
                        IconButton(onClick = { viewModel.clearSelectedOrder() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
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
                    Text("Aucune commande pour le moment")
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
                                scope.launch { viewModel.loadOrderItems(order) }
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
            val order = selectedOrder ?: return@Scaffold
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Commande #${order.orderNumber}", style = MaterialTheme.typography.titleMedium)
                            Text("Date: ${dateFormat.format(Date(order.date))}")
                            Text("Total: ${String.format("%.2f", order.totalAmount)} €")
                            Spacer(modifier = Modifier.height(8.dp))
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

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    MarketShopTheme {
        HistoryScreen(navController = rememberNavController())
    }
}
