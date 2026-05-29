package com.marketshop.data.database

import androidx.room.*
import com.marketshop.data.models.Order
import com.marketshop.data.models.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY date DESC") fun getAllOrders(): Flow<List<Order>>
    @Insert suspend fun insertOrder(order: Order): Long
    @Insert suspend fun insertOrderItems(items: List<OrderItem>)
    @Query("SELECT * FROM order_items WHERE orderId = :orderId") suspend fun getOrderItems(orderId: Int): List<OrderItem>
}
