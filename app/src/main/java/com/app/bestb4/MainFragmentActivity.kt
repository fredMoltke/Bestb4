package com.app.bestb4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.app.bestb4.fragments.InfoFragment
import com.app.bestb4.fragments.ListFragment
import com.app.bestb4.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main_fragment.*

class MainFragmentActivity : AppCompatActivity() {


    private val settingsFragment = SettingsFragment()
    private val infoFragment = InfoFragment()
    private val listFragment = ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment)
        replaceFragment(listFragment)


        bottom_nav.setOnNavigationItemSelectedListener{

            when(it.itemId){
                R.id.ic_list -> replaceFragment(listFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
                R.id.ic_info -> replaceFragment(infoFragment)

            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

    }
}