package com.app.bestb4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bestb4.data.events.ClickEvent
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.ItemEvent
import kotlinx.android.synthetic.main.activity_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ListActivity : AppCompatActivity() {

    private var itemList = ArrayList<ListItem>()
    private var adapter = ListAdapter(itemList)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.recycler_view)


//        itemList = generateDummyList(100)
        adapter = ListAdapter(itemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    fun insertItem(position: Int, item: ListItem){
        itemList.add(position, item)
        adapter.notifyItemInserted(position)
    }

    fun removeItem(position: Int){
        itemList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    @Subscribe
    fun onClickEvent(clickEvent: ClickEvent){
        Toast.makeText(this, "Item clicked at position ${clickEvent.position}", Toast.LENGTH_SHORT).show()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onItemEvent(itemEvent: ItemEvent){
//        itemList.add(itemEvent.item)
//        adapter.notifyDataSetChanged()
        insertItem(0, itemEvent.item)
    }

    // Funktion der opretter liste med dummy items
    private fun generateDummyList(size: Int): ArrayList<ListItem> {
        val list = ArrayList<ListItem>()

        for (i in 0 until size) {
//            val item = ListItem(R.drawable.ic_baseline_fastfood_24, "Item $i", "Line 2")
//            list += item
        }
        return list
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}