package com.app.bestb4

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.ClickEvent
import com.app.bestb4.data.events.ItemEvent
import kotlinx.android.synthetic.main.activity_create_item.*
import kotlinx.android.synthetic.main.activity_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class ListActivity : AppCompatActivity() {

    private var itemList = ArrayList<ListItem>()
    private var adapter = ListAdapter(itemList)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.recycler_view)

        itemList = ArrayList<ListItem>()
        adapter = ListAdapter(itemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        open_camera_btn.setOnClickListener {
            var cameraIntent = Intent(this@ListActivity, CameraActivity::class.java)
            startActivity(cameraIntent)
        }

    }

    fun insertItem(item: ListItem){
        itemList.add(item)
        itemList = insertionSort(itemList)
        adapter.notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        itemList.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    @Subscribe
    fun onClickEvent(clickEvent: ClickEvent){
        Toast.makeText(this, "Trykket på item ${clickEvent.position+1}", Toast.LENGTH_SHORT).show()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onItemEvent(itemEvent: ItemEvent){
        insertItem(itemEvent.item)
    }

    // Funktion der opretter liste med dummy items
//    private fun generateDummyList(size: Int): ArrayList<ListItem> {
//        val list = ArrayList<ListItem>()
//
//        for (i in 0 until size) {
//            val item = ListItem(R.drawable.ic_baseline_fastfood_24, "Item $i", "Line 2")
//            list += item
//        }
//        return list
//    }

//    private fun generateSortingTest(): ArrayList<ListItem> {
//        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
//        val d1 = simpleDateFormat.parse("05-11-2020")
//        val d2 = simpleDateFormat.parse("04-11-2020")
//        val d3 = simpleDateFormat.parse("06-11-2020")
//        val d4 = simpleDateFormat.parse("07-11-2020")
//        val d5 = simpleDateFormat.parse("01-11-2020")
//        val d6 = simpleDateFormat.parse("03-11-2020")
//        val d7 = simpleDateFormat.parse("05-11-2020")
//
//        val testBitmap : Bitmap = generateBitmap()
//
//        val i1 = ListItem(1, "Et", 4, testBitmap, testBitmap, d1)
//        val i2 = ListItem(2, "Fire", 9, testBitmap, testBitmap, d2)
//        val i3 = ListItem(3, "To", 4, testBitmap, testBitmap, d3)
//        val i4 = ListItem(4, "Tre", 4, testBitmap, testBitmap, d4)
//        val i5 = ListItem(5, "Fem", 22, testBitmap, testBitmap, d5)
//        val i6 = ListItem(6, "Syv", 23, testBitmap, testBitmap, d6)
//        val i7 = ListItem(7, "Seks", 19, testBitmap, testBitmap, d7)
//
//        var arrayList = ArrayList<ListItem>()
//        arrayList.add(i1)
//        arrayList.add(i2)
//        arrayList.add(i3)
//        arrayList.add(i4)
//        arrayList.add(i5)
//        arrayList.add(i6)
//        arrayList.add(i7)
//
//        return arrayList
//    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

 //   https://chercher.tech/kotlin/insertion-sort-kotlin TODO: check
 // Sorter liste fra kortest til længest holdbarhed (relativt til åbningsdato og holdbarhed efter åbning)
    private fun insertionSort(list: ArrayList<ListItem>) : ArrayList<ListItem>{
        if (list.isEmpty() || list.size<2) return list

        val currentDate : Date = Calendar.getInstance().time
        for (count in 1 until list.count()){
            val item = list[count]
            var i = count
            while (i > 0 && calculateDaysLeft(item, currentDate) < calculateDaysLeft(
                    list[i - 1],
                    currentDate
                )){
                list[i] = list[i - 1]
                i -= 1
            }
            list[i] = item
        }
        return list
    }

    // Beregner antal dage en var stadig kan holde sig ud fra åbningsdato og holdbarhed efter åbning
    private fun calculateDaysLeft(item: ListItem, currentDate: Date) : Int{
        var differenceInMilliseconds : Long = currentDate.time - item.date.time
        var differenceInDays : Long = TimeUnit.DAYS.convert(
            differenceInMilliseconds,
            TimeUnit.MILLISECONDS
        )
        return item.expiration - differenceInDays.toInt()
    }

//    fun generateBitmap() : Bitmap{
//        val w: Int = 320
//        val h: Int = 320
//
//        val conf = Bitmap.Config.ARGB_8888 // see other conf types
//
//        return Bitmap.createBitmap(w, h, conf)
//    }
}