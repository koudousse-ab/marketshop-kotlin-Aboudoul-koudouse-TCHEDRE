package com.marketshop.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marketshop.data.models.CartItem
import com.marketshop.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val repository: ShopRepository) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total

    init {
        viewModelScope.launch {
            repository.getCartItems().collect { items ->
                _cartItems.value = items
                _total.value = items.sumOf { it.price * it.quantity }
            }
        }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) repository.removeFromCart(item)
            else {
                item.quantity = newQuantity
                repository.updateCartItem(item)
            }
        }
    }

    fun removeItem(item: CartItem) = viewModelScope.launch { repository.removeFromCart(item) }
}
