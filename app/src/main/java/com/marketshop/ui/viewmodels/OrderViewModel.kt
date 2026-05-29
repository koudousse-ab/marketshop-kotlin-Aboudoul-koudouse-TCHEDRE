package com.marketshop.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.marketshop.data.models.CartItem
import com.marketshop.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class OrderFormState(
    val customerName: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val errors: Map<String, String> = emptyMap()
)

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopRepository(application)
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total
    private val _formState = MutableStateFlow(OrderFormState())
    val formState: StateFlow<OrderFormState> = _formState
    private val _isOrderPlaced = MutableStateFlow(false)
    val isOrderPlaced: StateFlow<Boolean> = _isOrderPlaced

    init {
        viewModelScope.launch {
            repository.getCartItems().collect { items ->
                _cartItems.value = items
                _total.value = items.sumOf { it.price * it.quantity }
            }
        }
    }

    fun updateFormField(field: String, value: String) {
        _formState.value = when (field) {
            "customerName" -> _formState.value.copy(customerName = value)
            "phone" -> _formState.value.copy(phone = value)
            "address" -> _formState.value.copy(address = value)
            "city" -> _formState.value.copy(city = value)
            else -> _formState.value
        }
    }

    fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()
        if (_formState.value.customerName.isBlank()) errors["customerName"] = "Nom requis"
        if (_formState.value.phone.isBlank()) errors["phone"] = "Téléphone requis"
        else if (!_formState.value.phone.matches(Regex("^[0-9+\\s-]{8,}$"))) errors["phone"] = "Téléphone invalide"
        if (_formState.value.address.isBlank()) errors["address"] = "Adresse requise"
        if (_formState.value.city.isBlank()) errors["city"] = "Ville requise"
        _formState.value = _formState.value.copy(errors = errors)
        return errors.isEmpty()
    }

    suspend fun placeOrder(): Result<Unit> {
        val result = repository.placeOrder(
            _formState.value.customerName,
            _formState.value.phone,
            _formState.value.address,
            _formState.value.city
        )
        if (result.isSuccess) _isOrderPlaced.value = true
        return result.map { Unit }
    }
}
