package com.app.bestb4.room

import android.net.Uri
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

    @TypeConverter
    fun toUri(value: String?): Uri?{
        return if (value == null) null else Uri.parse(value)
    }

    @TypeConverter
    fun fromUri(uri: Uri?): String?{
        return uri.toString()
    }
}