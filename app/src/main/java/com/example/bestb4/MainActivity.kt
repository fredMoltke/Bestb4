package com.example.bestb4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.openActivity_btn)
        image = findViewById<ImageView>(R.id.imageView)

        button.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    @Subscribe
    fun onEvent(event: CustomEvent){
        Toast.makeText(this@MainActivity, "Event received", Toast.LENGTH_LONG).show()
        image.setImageBitmap(event.bitmap)
    }

    override fun onStart() {
        super.onStart()
        // Fortæl EventBus at denne aktivitet er interesseret i EventBus events
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}