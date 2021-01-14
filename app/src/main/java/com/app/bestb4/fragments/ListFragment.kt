package com.app.bestb4.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bestb4.*
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.ClickEvent
import com.app.bestb4.data.events.ItemEvent
import com.app.bestb4.data.realmObjects.RealmListItem
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmQuery
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayInputStream
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ListFragment : Fragment() {

    private var itemList = ArrayList<ListItem>()
    private var adapter = ListAdapter(itemList)
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerState: Parcelable
    private val LIST_STATE_KEY: String = "LIST_STATE"
    val realm by lazy { Realm.getDefaultInstance() }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       Realm.init(activity)
        val config = RealmConfiguration.Builder()
            .name("bestb4.realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)



        // TODO: Check om nuværende item liste (til recyclerview) er tomt. Hvis ja, hent fra database

        recyclerView = view.findViewById(R.id.recycler_view)
        adapter = ListAdapter(itemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        recyclerState = (recyclerView.layoutManager as LinearLayoutManager).onSaveInstanceState()!!

        open_camera_btn.setOnClickListener {
            var cameraIntent = Intent(activity, CameraActivity::class.java)
            startActivity(cameraIntent)
        }


    }

    fun insertItem(item: ListItem){
        itemList.add(item)
        adapter.notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        itemList.removeAt(position)
        adapter.notifyItemRemoved(position)
        // TODO: item skal også fjernes fra database
    }

    @Subscribe
    fun onClickEvent(clickEvent: ClickEvent){
        Toast.makeText(activity, "Trykket på item ${clickEvent.position + 1}", Toast.LENGTH_SHORT).show()
    }

    // IGNORE
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onItemEvent(itemEvent: ItemEvent){
        insertItem(itemEvent.item)
        EventBus.getDefault().removeStickyEvent(itemEvent)
    }

    // IGNORE
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        // TODO: Check om nuværende item liste (til recyclerview) er tomt. Hvis ja, hent fra database
    }

    // IGNORE
    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    // IGNORE
    override fun onPause() {
        super.onPause()
//        recyclerState = (recyclerView.layoutManager as LinearLayoutManager).onSaveInstanceState()!!
    }

    // IGNORE
    override fun onResume() {
        super.onResume()
        itemList = insertionSort(itemList)
//        recyclerView.layoutManager!!.onRestoreInstanceState(recyclerState)
    }

    // IGNORE
    //   https://chercher.tech/kotlin/insertion-sort-kotlin
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

    // TODO: Gammel databasemetode
    private fun getItemsFromRealm(): ArrayList<ListItem>{
        var list = ArrayList<ListItem>()
        val items: RealmQuery<RealmListItem>? = realm.where(RealmListItem::class.java)
        items?.findAll()?.forEach{
            var item : ListItem = ListItem(it.id, it.name, it.expiration,
                byteArrayToBitmap(it.bitmapByteArray), byteArrayToBitmap(it.thumbnailByteArray),
                it.date, it.daysLeft)
        }
        return list
    }

    // IGNORE
    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap {
        val arrayInputStream = ByteArrayInputStream(byteArray)
        return BitmapFactory.decodeStream(arrayInputStream)
    }
//    fun generateBitmap() : Bitmap{
//        val w: Int = 320
//        val h: Int = 320
//
//        val conf = Bitmap.Config.ARGB_8888 // see other conf types
//
//        return Bitmap.createBitmap(w, h, conf)
//    }

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

}
