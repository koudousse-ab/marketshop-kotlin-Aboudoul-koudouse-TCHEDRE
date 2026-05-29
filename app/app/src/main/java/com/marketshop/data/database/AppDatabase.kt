package com.marketshop.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marketshop.data.models.CartItem
import com.marketshop.data.models.Order
import com.marketshop.data.models.OrderItem

@Database(entities = [CartItem::class, Order::class, OrderItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "market_shop_db")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
