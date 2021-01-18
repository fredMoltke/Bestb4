package com.app.bestb4.data.events

import com.app.bestb4.data.ListItem

data class UpdateItemEvent(var item: ListItem, var positionInList: Int) {

}