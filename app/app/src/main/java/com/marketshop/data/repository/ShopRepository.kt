package com.marketshop.data.repository

import android.content.Context
import com.marketshop.data.api.RetrofitInstance
import com.marketshop.data.database.AppDatabase
import com.marketshop.data.models.CartItem
import com.marketshop.data.models.Order
import com.marketshop.data.models.OrderItem
import com.marketshop.data.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ShopRepository(context: Context) {
    private val api = RetrofitInstance.api
    private val db = AppDatabase.getDatabase(context)
    private val cartDao = db.cartDao()
    private val orderDao = db.orderDao()

    suspend fun getProducts(): Result<List<Product>> = runCatching { api.getProducts() }
    suspend fun getCategories(): Result<List<String>> = runCatching { api.getCategories() }

    fun getCartItems(): Flow<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun addToCart(product: Product, quantity: Int) {
        val existing = cartDao.getCartItemByProductId(product.id)
        if (existing != null) {
            existing.quantity += quantity
            cartDao.updateCartItem(existing)
        } else {
            cartDao.insertCartItem(CartItem(
                productId = product.id,
                title = product.title,
                price = product.price,
                image = product.image,
                quantity = quantity
            ))
        }
    }

    suspend fun updateCartItem(item: CartItem) = cartDao.updateCartItem(item)
    suspend fun removeFromCart(item: CartItem) = cartDao.deleteCartItem(item)
    suspend fun clearCart() = cartDao.clearCart()

    suspend fun placeOrder(customerName: String, phone: String, address: String, city: String): Result<Order> = runCatching {
        val cartItems = cartDao.getAllCartItems().first()
        if (cartItems.isEmpty()) throw Exception("Panier vide")
        val total = cartItems.sumOf { it.price * it.quantity }
        val totalItems = cartItems.sumOf { it.quantity }
        val orderNumber = "ORD-${SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())}"
        val order = Order(
            orderNumber = orderNumber,
            date = System.currentTimeMillis(),
            totalAmount = total,
            totalItems = totalItems,
            customerName = customerName,
            phone = phone,
            address = address,
            city = city
        )
        val orderId = orderDao.insertOrder(order).toInt()
        val orderItems = cartItems.map {
            OrderItem(
                orderId = orderId,
                productId = it.productId,
                title = it.title,
                price = it.price,
                quantity = it.quantity
            )
        }
        orderDao.insertOrderItems(orderItems)
        cartDao.clearCart()
        order
    }

    fun getOrders(): Flow<List<Order>> = orderDao.getAllOrders()
    suspend fun getOrderItems(orderId: Int): List<OrderItem> = withContext(Dispatchers.IO) {
        orderDao.getOrderItems(orderId)
    }
}
