package com.app.bestb4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.app.bestb4.data.events.BitmapEvent
import kotlinx.android.synthetic.main.activity_create_item.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CreateItem : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var animationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)

        image = findViewById(R.id.create_item_image_preview)
        image.visibility = View.GONE
        animationView = findViewById(R.id.create_item_loading_animation)
        animationView.visibility = View.VISIBLE

        confirm_item_btn.setOnClickListener {
            // Opret nyt item
            addToList() //TODO: TOM FUNKTION, UDARBEJD NOGET. REALM?
        }
        cancel_item_btn.setOnClickListener {
            finish()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onBitmapEvent(bitmapEvent: BitmapEvent){
        image.setImageBitmap(bitmapEvent.bitmap)
        animationView.visibility = View.GONE
        image.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        // TODO: Fjern billede så gammelt billede ikke vises når man åbner CreateItem igen
        EventBus.getDefault().unregister(this)
    }

    private fun addToList(){
        val intent = Intent(this@CreateItem, ListActivity::class.java)
        startActivity(intent)
    }
}