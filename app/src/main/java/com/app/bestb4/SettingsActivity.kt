package com.app.bestb4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        open_info_btn.setOnClickListener{
            var infoIntent = Intent(this@SettingsActivity, InfoActivity::class.java)
            startActivity(infoIntent)
        }

        open_list_btn.setOnClickListener {
            var listIntent = Intent(this@SettingsActivity, ListActivity::class.java)
            startActivity(listIntent)
        }

        open_info_btn.setOnClickListener {
            var settingsIntent =  Intent(this@SettingsActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }
}