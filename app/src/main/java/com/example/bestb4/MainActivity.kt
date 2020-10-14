package com.example.bestb4

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.bestb4.data.events.BitmapEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {


    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME="notificationChannel"
    val NOTIFICATION_ID = 201

//    private lateinit var button: Button
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Expiration date almost reached")
            .setContentText("Eat this: ListIteam")
            .setSmallIcon(R.drawable.ic_foodnotification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        notifi_btn.setOnClickListener {
            notificationManager.notify(NOTIFICATION_ID, notification)
        }


//        button = findViewById<Button>(R.id.openActivity_btn)
        image = findViewById<ImageView>(R.id.imageView)

        openActivity_btn.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
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

    @Subscribe
    fun onEvent(event: BitmapEvent){
        image.setImageBitmap(event.bitmap)
        if (event.bitmap.height < event.bitmap.width){
            val rotation = 90
            image.rotation = rotation.toFloat()
        }
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