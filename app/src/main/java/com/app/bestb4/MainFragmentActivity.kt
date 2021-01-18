package com.app.bestb4

import com.app.bestb4.fragments.SettingsFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.bestb4.fragments.InfoFragment
import com.app.bestb4.fragments.ListFragment
import kotlinx.android.synthetic.main.activity_main_fragment.*


class MainFragmentActivity : AppCompatActivity() {

    private val settingsFragment = SettingsFragment()
    private val infoFragment = InfoFragment()
    private val listFragment = ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment)
        replaceFragment(listFragment)

        // if statements her checker om det nuværende fragment der vises trykkes på i navigationbar.
        // Sørger for backstack ikke fyldes med fragments
        bottom_nav.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.ic_list -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != listFragment)
                    replaceFragment(listFragment)
                R.id.ic_settings -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != settingsFragment)
                    replaceFragment(settingsFragment)
                R.id.ic_info -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != infoFragment)
                    replaceFragment(infoFragment)

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

    @Override
    override fun onBackPressed() {
        // Sørger for, at man ikke kan forlade appen ved backspace, når man navigerer igennem fragments
        // TODO: Lav setting, som kan ændre det til, at man godt kan lukke appen med backspace
        if(supportFragmentManager.backStackEntryCount == 1){
            replaceFragment(listFragment)
        }
        else super.onBackPressed()
    }
}