package com.marketshop.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.marketshop.data.models.Order
import com.marketshop.data.models.OrderItem
import com.marketshop.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopRepository(application)

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder: StateFlow<Order?> = _selectedOrder

    private val _selectedOrderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val selectedOrderItems: StateFlow<List<OrderItem>> = _selectedOrderItems

    init {
        viewModelScope.launch {
            repository.getOrders().collect { _orders.value = it }
        }
    }

    suspend fun loadOrderItems(order: Order) {
        _selectedOrder.value = order
        _selectedOrderItems.value = repository.getOrderItems(order.orderId)
    }

    fun clearSelectedOrder() {
        _selectedOrder.value = null
        _selectedOrderItems.value = emptyList()
    }
}
