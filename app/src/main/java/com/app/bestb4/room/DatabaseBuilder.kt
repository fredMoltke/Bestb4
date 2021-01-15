package com.app.bestb4.room

import android.content.Context
import androidx.room.Room

class DatabaseBuilder {
    companion object{
        private var db: AppDatabase? = null

        fun get(context: Context?): AppDatabase {
            if(db == null){
                db = Room.databaseBuilder(
                    context!!.applicationContext,
                    AppDatabase::class.java,
                    "BestB4Database"
                ).fallbackToDestructiveMigration().
                build()
            }
            return db as AppDatabase
        }
    }
}