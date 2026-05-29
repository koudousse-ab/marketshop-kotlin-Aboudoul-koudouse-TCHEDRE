package com.marketshop.data.database

import androidx.room.*
import com.marketshop.data.models.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items") fun getAllCartItems(): Flow<List<CartItem>>
    @Insert suspend fun insertCartItem(item: CartItem)
    @Update suspend fun updateCartItem(item: CartItem)
    @Delete suspend fun deleteCartItem(item: CartItem)
    @Query("DELETE FROM cart_items") suspend fun clearCart()
    @Query("SELECT * FROM cart_items WHERE productId = :productId") suspend fun getCartItemByProductId(productId: Int): CartItem?
}