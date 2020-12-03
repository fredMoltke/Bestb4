package com.app.bestb4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)


        open_info_btn.setOnClickListener{
            var infoIntent = Intent(this@InfoActivity, InfoActivity::class.java)
            startActivity(infoIntent)
        }

        open_list_btn.setOnClickListener {
            var listIntent = Intent(this@InfoActivity, ListActivity::class.java)
            startActivity(listIntent)
        }

        open_info_btn.setOnClickListener {
            var settingsIntent =  Intent(this@InfoActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}