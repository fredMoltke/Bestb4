package com.app.bestb4.data

import android.graphics.Bitmap
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ListItem(
    @PrimaryKey(autoGenerate = true) val id : Long,
    var name: String,
    var expiration: Int,
    var uri: Uri,
    var date: Date,
    var daysLeft : Int
    )