package com.marketshop.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marketshop.data.models.Product
import com.marketshop.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CatalogueUiState {
    object Loading : CatalogueUiState()
    data class Success(val products: List<Product>, val categories: List<String>) : CatalogueUiState()
    data class Error(val message: String) : CatalogueUiState()
}

class CatalogueViewModel(private val repository: ShopRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CatalogueUiState>(CatalogueUiState.Loading)
    val uiState: StateFlow<CatalogueUiState> = _uiState

    private var allProducts = listOf<Product>()
    private var selectedCategory: String? = null

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = CatalogueUiState.Loading
            val productsResult = repository.getProducts()
            val categoriesResult = repository.getCategories()
            if (productsResult.isSuccess && categoriesResult.isSuccess) {
                allProducts = productsResult.getOrNull() ?: emptyList()
                val categories = listOf("Tous") + (categoriesResult.getOrNull() ?: emptyList())
                _uiState.value = CatalogueUiState.Success(allProducts, categories)
            } else {
                _uiState.value = CatalogueUiState.Error("Erreur de chargement")
            }
        }
    }

    fun filterByCategory(category: String) {
        selectedCategory = if (category == "Tous") null else category
        val filtered = if (selectedCategory == null) allProducts else allProducts.filter { it.category == selectedCategory }
        val current = _uiState.value
        if (current is CatalogueUiState.Success) {
            _uiState.value = CatalogueUiState.Success(filtered, current.categories)
        }
    }

    fun retry() = loadData()
}
