package com.app.bestb4

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.bestb4.fragments.InfoFragment
import com.app.bestb4.fragments.ListFragment
import com.app.bestb4.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main_fragment.*

class NotificationHandler : AppCompatActivity() {

    val CHANNEL_ID_1DAY = "channelID"
    val CHANNEL_ID_DAYS = "channelID2"
    val CHANNEL_NAME = "notificationChannel"
    val NOTIFICATION_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        val intent = Intent(this, MainFragmentActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationOneDayleft = NotificationCompat.Builder(this, CHANNEL_ID_1DAY)
            .setContentTitle("Expiration date reached tomorrow")
            .setContentText("ListIteam")
            .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationDaysleft = NotificationCompat.Builder(this, CHANNEL_ID_DAYS)
            .setContentTitle("Expiration date almost reached")
            .setContentText("ListIteam")
            .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        // test for at se om notifficationen virker
        notificationManager.notify(NOTIFICATION_ID, notificationOneDayleft)
        notificationManager.notify(NOTIFICATION_ID, notificationDaysleft)

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // notification importance high lets you play a sound when sending a notification.
            val channel = NotificationChannel(CHANNEL_ID_1DAY, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH).apply {
                lightColor = Color.RED
                enableLights(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}