package com.marketshop.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.marketshop.data.models.Product
import com.marketshop.data.models.Rating
import com.marketshop.navigation.ProductDetailViewModelFactory
import com.marketshop.ui.theme.MarketShopTheme
import com.marketshop.ui.viewmodels.ProductDetailUiState
import com.marketshop.ui.viewmodels.ProductDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: Int,
    viewModel: ProductDetailViewModel = viewModel(factory = ProductDetailViewModelFactory(productId, LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Détail produit") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is ProductDetailUiState.Loading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            }
            is ProductDetailUiState.Success -> {
                val product = (uiState as ProductDetailUiState.Success).product
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = product.image,
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(product.title, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "${product.price} €",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row {
                        Text("Note: ${product.rating.rate}/5")
                        Spacer(Modifier.width(16.dp))
                        Text("Catégorie: ${product.category}")
                    }
                    Text("Description", style = MaterialTheme.typography.titleMedium)
                    Text(product.description)
                    Spacer(Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.decrementQuantity() },
                                enabled = quantity > 1
                            ) { Icon(Icons.Default.Remove, null) }
                            Text(quantity.toString(), style = MaterialTheme.typography.titleLarge)
                            IconButton(
                                onClick = { viewModel.incrementQuantity() },
                                enabled = quantity < 99
                            ) { Icon(Icons.Default.Add, null) }
                        }
                        Button(onClick = {
                            scope.launch {
                                viewModel.addToCart(product)
                                snackbarHostState.showSnackbar("Ajouté au panier")
                                navController.navigateUp()
                            }
                        }) { Text("Ajouter au panier") }
                    }
                }
            }
            is ProductDetailUiState.Error -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text((uiState as ProductDetailUiState.Error).message)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Product Detail Preview")
@Composable
fun PreviewProductDetailScreen() {
    MarketShopTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Titre produit", style = MaterialTheme.typography.headlineSmall)
            Text("49.99 €", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            Text("Note: 4.5/5")
            Spacer(Modifier.height(16.dp))
            Text("Description", style = MaterialTheme.typography.titleMedium)
            Text("Description fictive pour l'aperçu.")
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = {}) { Icon(Icons.Default.Remove, null) }
                    Text("1")
                    IconButton(onClick = {}) { Icon(Icons.Default.Add, null) }
                }
                Button(onClick = {}) { Text("Ajouter") }
            }
        }
    }
}
