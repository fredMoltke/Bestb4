package com.app.bestb4.room

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return Date(value!!)
    }

    @TypeConverter
    fun fromDate(date: Date): Long? {
        return date.time
    }
}