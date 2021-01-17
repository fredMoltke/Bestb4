package com.app.bestb4

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationSchedule (var context: Context, var params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val data = params.inputData
        val title = "A title"
        val body = data.getString("A msg")

        TriggerNotification(context, title, body)

        return Result.success()
    }
}