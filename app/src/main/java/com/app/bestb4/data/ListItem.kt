package com.app.bestb4.data

import android.graphics.Bitmap
import java.util.*

data class ListItem(val id : Long, var name: String, var expiration: Int, var bitmap: Bitmap, var thumbnail: Bitmap, var date: Date)