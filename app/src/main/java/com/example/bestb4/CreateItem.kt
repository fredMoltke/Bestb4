package com.example.bestb4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.bestb4.data.events.BitmapEvent
import kotlinx.android.synthetic.main.activity_create_item.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CreateItem : AppCompatActivity() {

    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)

        image = findViewById(R.id.create_item_image_preview)
        image.visibility = View.GONE

        confirm_item_btn.setOnClickListener {
            // Opret nyt item
            finish()
        }
        cancel_item_btn.setOnClickListener {
            finish()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onBitmapEvent(bitmapEvent: BitmapEvent){
        image.setImageBitmap(bitmapEvent.bitmap)
        image.visibility = View.VISIBLE
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