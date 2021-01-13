package com.app.bestb4

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bestAnimation = AnimationUtils.loadAnimation(this, R.anim.best_animation)
        val b4Animation = AnimationUtils.loadAnimation(this, R.anim.b4_animation)

        bestTextView.startAnimation(bestAnimation)
        b4TextView.startAnimation(b4Animation)

        val splashScreenTimeout = 1900
        val homeIntent = Intent(this@MainActivity, MainFragmentActivity::class.java)
        homeIntent.flags = homeIntent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY

        Handler().postDelayed({
            startActivity(homeIntent)
            finish()
        }, splashScreenTimeout.toLong())

    }
}