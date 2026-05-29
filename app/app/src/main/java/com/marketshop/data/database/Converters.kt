package com.marketshop.data.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun fromTimestamp(value: Long?): Long? = value
    @TypeConverter fun dateToTimestamp(date: Long?): Long? = date
}
