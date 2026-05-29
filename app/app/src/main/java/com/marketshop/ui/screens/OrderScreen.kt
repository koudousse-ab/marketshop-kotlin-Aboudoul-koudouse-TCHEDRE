package com.marketshop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marketshop.navigation.OrderViewModelFactory
import com.marketshop.ui.theme.MarketShopTheme
import com.marketshop.ui.viewmodels.OrderViewModel
import kotlinx.coroutines.launch

@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = viewModel(factory = OrderViewModelFactory(LocalContext.current))
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val total by viewModel.total.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val isOrderPlaced by viewModel.isOrderPlaced.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isOrderPlaced) {
        if (isOrderPlaced) {
            snackbarHostState.showSnackbar("Commande confirmée!")
            navController.navigate("history") {
                popUpTo("cart") { inclusive = true }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Validation commande") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Récapitulatif", style = MaterialTheme.typography.titleMedium)
                        cartItems.forEach { item ->
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("${item.title} x${item.quantity}")
                                Text("${String.format("%.2f", item.price * item.quantity)} €")
                            }
                        }
                        Divider()
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Total:", style = MaterialTheme.typography.titleLarge)
                            Text(
                                "${String.format("%.2f", total)} €",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            item {
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = formState.customerName,
                            onValueChange = { viewModel.updateFormField("customerName", it) },
                            label = { Text("Nom complet") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = formState.errors.containsKey("customerName"),
                            supportingText = {
                                if (formState.errors.containsKey("customerName"))
                                    Text(formState.errors["customerName"]!!)
                            }
                        )
                        OutlinedTextField(
                            value = formState.phone,
                            onValueChange = { viewModel.updateFormField("phone", it) },
                            label = { Text("Téléphone") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            isError = formState.errors.containsKey("phone"),
                            supportingText = {
                                if (formState.errors.containsKey("phone"))
                                    Text(formState.errors["phone"]!!)
                            }
                        )
                        OutlinedTextField(
                            value = formState.address,
                            onValueChange = { viewModel.updateFormField("address", it) },
                            label = { Text("Adresse") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = formState.errors.containsKey("address"),
                            supportingText = {
                                if (formState.errors.containsKey("address"))
                                    Text(formState.errors["address"]!!)
                            }
                        )
                        OutlinedTextField(
                            value = formState.city,
                            onValueChange = { viewModel.updateFormField("city", it) },
                            label = { Text("Ville") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = formState.errors.containsKey("city"),
                            supportingText = {
                                if (formState.errors.containsKey("city"))
                                    Text(formState.errors["city"]!!)
                            }
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        if (viewModel.validateForm()) {
                            scope.launch { viewModel.placeOrder() }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = cartItems.isNotEmpty()
                ) {
                    Text("Confirmer la commande")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Order Preview")
@Composable
fun PreviewOrderScreen() {
    MarketShopTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Récapitulatif")
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Produit x1")
                        Text("19.99 €")
                    }
                    Divider()
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Total:")
                        Text("19.99 €", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Nom") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Téléphone") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Adresse") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Ville") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Confirmer") }
        }
    }
}
