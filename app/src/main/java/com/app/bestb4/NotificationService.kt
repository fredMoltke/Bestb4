package com.app.bestb4

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.app.bestb4.data.ListItem
import java.util.*
import java.util.logging.Handler
import java.util.logging.LogRecord
import kotlin.collections.ArrayList

//https://stackoverflow.com/questions/20501225/using-service-to-run-background-and-create-notification
//https://www.youtube.com/watch?v=urn355_ymNA&ab_channel=PhilippLackner

class NotificationService : Service() {
    lateinit var timer: Timer
    lateinit var timerTask: TimerTask
    lateinit var listitems: ArrayList<ListItem>

    val TAG = "Timers"
    val Your_X_SECS = 5

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        startTimer()
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        stoptimertask()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    //we are going to use a handler to be able to run in our TimerTask
    val handler: Handler = object : Handler() {
        override fun publish(record: LogRecord) {}
        override fun flush() {}

        @Throws(SecurityException::class)
        override fun close() {
        }
    }

    fun startTimer() {
        //set a new Timer
        timer = Timer()
        //initialize the TimerTask's job
        initializeTimerTask()
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, Your_X_SECS * 1000.toLong())
    }

    fun stoptimertask() {
        //stop the timer, if it's not already null
        timer.cancel()

    }

    fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                //TODO CALL NOTIFICATION FUNC
                NotificationHandler()

            }
        }
    }
}