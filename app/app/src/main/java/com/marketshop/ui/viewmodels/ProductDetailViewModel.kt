package com.marketshop.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marketshop.data.models.Product
import com.marketshop.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}

class ProductDetailViewModel(
    private val repository: ShopRepository,
    private val productId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity

    init { loadProduct() }

    private fun loadProduct() {
        viewModelScope.launch {
            val result = repository.getProducts()
            if (result.isSuccess) {
                val product = result.getOrNull()?.find { it.id == productId }
                if (product != null) {
                    _uiState.value = ProductDetailUiState.Success(product)
                } else {
                    _uiState.value = ProductDetailUiState.Error("Produit non trouvé")
                }
            } else {
                _uiState.value = ProductDetailUiState.Error(result.exceptionOrNull()?.message ?: "Erreur")
            }
        }
    }

    fun incrementQuantity() { if (_quantity.value < 99) _quantity.value++ }
    fun decrementQuantity() { if (_quantity.value > 1) _quantity.value-- }
    suspend fun addToCart(product: Product) = repository.addToCart(product, _quantity.value)
}
