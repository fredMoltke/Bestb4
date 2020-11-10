package com.app.bestb4.data.realmObjects

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class RealmListItem: RealmObject() {
    @PrimaryKey
    var id = 0L
    var name = ""
    var expiration = 0
    var bitmapByteArray : ByteArray? = null
    var thumbnailByteArray : ByteArray? = null
    var date = Date()
    var daysLeft = 0
}