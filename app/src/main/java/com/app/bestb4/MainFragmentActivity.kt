package com.app.bestb4


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.fragment.app.Fragment
import com.app.bestb4.fragments.InfoFragment
import com.app.bestb4.fragments.ListFragment
import com.app.bestb4.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe



class MainFragmentActivity : AppCompatActivity() {

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME="notificationChannel"
    val NOTIFICATION_ID = 201

    private val settingsFragment = SettingsFragment()
    private val infoFragment = InfoFragment()
    private val listFragment = ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment)
        replaceFragment(listFragment)

        // if statements her checker om det nuværende fragment der vises trykkes på i navigationbar.
        // Sørger for backstack ikke fyldes med fragments

        createNotificationChannel()

        val intent = Intent(this, MainFragmentActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Expiration date almost reached")
            .setContentText("Eat this: ListIteam")
            .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

       // OnclickListener for bottom navigation bar.
        bottom_nav.setOnNavigationItemSelectedListener{

            when(it.itemId){
                R.id.ic_list -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != listFragment) replaceFragment(listFragment)
                R.id.ic_settings -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != settingsFragment) replaceFragment(settingsFragment)
                R.id.ic_info -> if (supportFragmentManager.findFragmentById(R.id.fragment_container) != infoFragment) replaceFragment(infoFragment)

            }
            true
        }

    }

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // notification importance high lets you play a sound when sending a notification.
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH).apply {
                lightColor = Color.RED
                enableLights(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
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