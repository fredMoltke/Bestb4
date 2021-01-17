package com.app.bestb4.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.bestb4.*
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.ClickEvent
import com.app.bestb4.data.events.ItemEvent
import com.app.bestb4.data.events.ItemListEvent
import com.app.bestb4.room.DatabaseBuilder
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ListFragment : Fragment() {

    private var itemList = ArrayList<ListItem>()
    private var adapter = ListAdapter(itemList)
    private lateinit var recyclerView: RecyclerView
    private lateinit var icon: ImageView
    private lateinit var background: ImageView
    private lateinit var shadow: ImageView
    private lateinit var welcomeTitle: TextView
    private lateinit var welcomeText: TextView

//    private lateinit var db: AppDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_list, container, false)

        icon = view.findViewById(R.id.listWelcomeIcon)
        background = view.findViewById(R.id.listWelcomeBackground)
        welcomeTitle = view.findViewById(R.id.listWelcomeTitleTextView)
        welcomeText = view.findViewById(R.id.listWelcomeTextView)
        shadow = view.findViewById(R.id.listWelcomeShadow)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        db = DatabaseBuilder.get(view.context)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        adapter = ListAdapter(itemList)
        recyclerView.adapter = adapter

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
        GlobalScope.launch {
            val db = DatabaseBuilder.get(activity?.applicationContext)
            val filePathToDelete = itemList[position].filePath
            val id : Long = itemList[position].id
            db.listItemDao().deleteById(id)
            if (db.listItemDao().getById(id) != null){
                db.listItemDao().deleteById(id)
            }
            itemList.removeAt(position)
            adapter.notifyItemRemoved(position)
            var file: File = File(filePathToDelete)
            try {
                file.delete()
            }catch (e: Exception){
                Toast.makeText(activity, "Image failed to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @Subscribe
    fun onClickEvent(clickEvent: ClickEvent){
        removeItem(clickEvent.position)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onItemEvent(itemEvent: ItemEvent){
        insertItem(itemEvent.item)
        itemList = insertionSort(itemList)
        showWelcome(itemList.isEmpty())
        adapter.notifyDataSetChanged()
        EventBus.getDefault().removeStickyEvent(itemEvent)
    }

    // Eventbus henter listitems sendt fra splash screen
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onItemListEvent(itemListEvent: ItemListEvent){
        itemList = insertionSort(itemListEvent.items)
        EventBus.getDefault().removeStickyEvent(itemListEvent)
        adapter = ListAdapter(itemList)
        recyclerView.adapter = adapter
        // TODO: Skift boolean ud med first time boolean
        showWelcome(itemList.isEmpty())
        adapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
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

    private fun showWelcome(boolean: Boolean){
        if (boolean){
            icon.visibility = View.VISIBLE
            background.visibility = View.VISIBLE
            welcomeText.visibility = View.VISIBLE
            welcomeTitle.visibility = View.VISIBLE
            shadow.visibility = View.VISIBLE
        } else {
            icon.visibility = View.GONE
            background.visibility = View.GONE
            welcomeText.visibility = View.GONE
            welcomeTitle.visibility = View.GONE
            shadow.visibility = View.GONE
        }
    }

}
