package com.app.bestb4.fragments

import android.content.Intent
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
import kotlinx.android.synthetic.main.fragment_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ListFragment : Fragment() {

    private var itemList = ArrayList<ListItem>()
    private var adapter = ListAdapter(itemList)
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerState: Parcelable


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

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onItemEvent(itemEvent: ItemEvent){
        insertItem(itemEvent.item)
        EventBus.getDefault().removeStickyEvent(itemEvent)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        // TODO: Check om nuværende item liste (til recyclerview) er tomt. Hvis ja, hent fra database
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        itemList = insertionSort(itemList)
    }

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
}
