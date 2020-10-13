package com.example.bestb4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val exampleList = generateDummyList(100)
        recycler_view.adapter = ListAdapter(exampleList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
    }

    // Funktion der opretter liste med dummy items
    private fun generateDummyList(size: Int): List<ListItem> {
        val list = ArrayList<ListItem>()

        for (i in 0 until size) {
            val item = ListItem(R.drawable.ic_baseline_fastfood_24, "Item $i", "Line 2")
            list += item
        }
        return list
    }
}