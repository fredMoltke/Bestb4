package com.app.bestb4

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.ItemListEvent
import com.app.bestb4.room.AppDatabase
import com.app.bestb4.room.DatabaseBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var itemList: ArrayList<ListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseBuilder.get(this)

        val bestAnimation = AnimationUtils.loadAnimation(this, R.anim.best_animation)
        val b4Animation = AnimationUtils.loadAnimation(this, R.anim.b4_animation)

        bestTextView.startAnimation(bestAnimation)
        b4TextView.startAnimation(b4Animation)

        val splashScreenTimeout = 1900
        val homeIntent = Intent(this@MainActivity, MainFragmentActivity::class.java)
        homeIntent.flags = homeIntent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY

        var dbListItems = ArrayList<ListItem>()
        GlobalScope.launch {
            dbListItems = db.listItemDao().getAll() as ArrayList<ListItem>
            itemList = dbListItems
            val itemListEvent: ItemListEvent = ItemListEvent(itemList)
            EventBus.getDefault().postSticky(itemListEvent)
        }

        Handler().postDelayed({
            startActivity(homeIntent)
            finish()
        }, splashScreenTimeout.toLong())

    }
}