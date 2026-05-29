package com.marketshop.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Product(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(val rate: Double, val count: Int)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    var quantity: Int
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val orderId: Int = 0,
    val orderNumber: String,
    val date: Long,
    val totalAmount: Double,
    val totalItems: Int,
    val customerName: String,
    val phone: String,
    val address: String,
    val city: String
)

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderId: Int,
    val productId: Int,
    val title: String,
    val price: Double,
    val quantity: Int
)
