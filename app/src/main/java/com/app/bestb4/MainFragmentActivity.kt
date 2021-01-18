package com.app.bestb4


import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.app.bestb4.fragments.InfoFragment
import com.app.bestb4.fragments.ListFragment
import com.app.bestb4.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main_fragment.*
import java.util.*
import java.util.concurrent.TimeUnit


class MainFragmentActivity : AppCompatActivity() {

    private val settingsFragment = SettingsFragment()
    private val infoFragment = InfoFragment()
    private val listFragment = ListFragment()
    private lateinit var triggerNotification : TriggerNotification



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment)
        replaceFragment(listFragment)


        // test at notfications virker
      // triggerNotification = TriggerNotification(this,"HEJ", "mainfragment open")

        // if statements her checker om det nuværende fragment der vises trykkes på i navigationbar.
        // Sørger for backstack ikke fyldes med fragments

       // OnclickListener for bottom navigation bar.
        bottom_nav.setOnNavigationItemSelectedListener{

            when(it.itemId){
                R.id.ic_list -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != listFragment) replaceFragment(listFragment)
                R.id.ic_settings -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != settingsFragment) replaceFragment(settingsFragment)
                R.id.ic_info -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != infoFragment) replaceFragment(infoFragment)

            }
            true
        }
        loadSettings()

    }

    private fun loadSettings() {

        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val help: Boolean = sp.getBoolean("help",false)
        val darkMode: Boolean = sp.getBoolean("dark mode",false)


        if(darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean("dark mode", true)
            editor.apply()

        }else{

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            val editor: SharedPreferences.Editor = sp.edit()
            editor.putBoolean("dark mode", false)
            editor.apply()

        }

        if(help){

        }else{

        }
    }

    private fun replaceFragment(fragment: Fragment) {

            loadSettings()
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun scheduleNotification(timeDelay: Long, tag: String, body: String) {

        val data = Data.Builder().putString("body", body)

        val work = OneTimeWorkRequestBuilder<NotificationSchedule>()
            .setInitialDelay(timeDelay, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.Builder().setTriggerContentMaxDelay(1000, TimeUnit.MILLISECONDS).build()) // API Level 24
            .setInputData(data.build())
            .addTag(tag)
            .build()

        WorkManager.getInstance().enqueue(work)
    }

}


