package com.app.bestb4.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.bestb4.data.ListItem

@Database(entities = [ListItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listItemDao(): ListItemDAO
}