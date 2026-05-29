package com.marketshop.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.marketshop.navigation.CatalogueViewModelFactory
import com.marketshop.ui.components.ProductCard
import com.marketshop.ui.theme.MarketShopTheme
import com.marketshop.ui.viewmodels.CatalogueUiState
import com.marketshop.ui.viewmodels.CatalogueViewModel

@Composable
fun CatalogueScreen(
    navController: NavController,
    viewModel: CatalogueViewModel = viewModel(factory = CatalogueViewModelFactory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf("Tous") }

    Column(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is CatalogueUiState.Success -> {
                val categories = (uiState as CatalogueUiState.Success).categories
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                                viewModel.filterByCategory(category)
                            },
                            label = { Text(category) }
                        )
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((uiState as CatalogueUiState.Success).products) { product ->
                        ProductCard(
                            product = product,
                            onClick = { navController.navigate("product_detail/${product.id}") }
                        )
                    }
                }
            }
            is CatalogueUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CatalogueUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text((uiState as CatalogueUiState.Error).message)
                        Button(onClick = { viewModel.retry() }) { Text("Réessayer") }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCatalogueScreen() {
    MarketShopTheme {
        Text("Aperçu Catalogue")
    }
}
