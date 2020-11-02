package com.app.bestb4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.app.bestb4.data.events.BitmapEvent
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    //    private lateinit var button: Button
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Realm.init(applicationContext)

//        button = findViewById<Button>(R.id.openActivity_btn)

        val bestAnimation = AnimationUtils.loadAnimation(this, R.anim.best_animation)
        val b4Animation = AnimationUtils.loadAnimation(this, R.anim.b4_animation)

        bestTextView.startAnimation(bestAnimation)
        b4TextView.startAnimation(b4Animation)

        val splashScreenTimeout = 2200
        val homeIntent = Intent(this@MainActivity, ListActivity::class.java)

        Handler().postDelayed({
            startActivity(homeIntent)
            finish()
        }, splashScreenTimeout.toLong())


    }
}