package com.app.bestb4.room

import androidx.room.*
import com.app.bestb4.data.ListItem

@Dao
interface ListItemDAO {
    @Query("SELECT * FROM ListItem")
    fun getAll(): List<ListItem>

    @Query("SELECT * FROM ListItem WHERE id IN (:ids)")
    fun getAllByIds(ids: IntArray): List<ListItem>

    @Query("SELECT * FROM ListItem WHERE id = :ids")
    fun getById(ids: Int): ListItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(listItem: ListItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(listItems: List<ListItem>)

    @Update
    fun update(listItem: ListItem)

    @Update
    fun updateAll(listItems: List<ListItem>)

    @Delete
    fun delete(listItem: ListItem)

    @Delete
    fun deleteAll(listItems: List<ListItem>)
}