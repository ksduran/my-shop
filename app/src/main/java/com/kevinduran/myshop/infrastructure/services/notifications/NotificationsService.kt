package com.kevinduran.myshop.infrastructure.services.notifications

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.kevinduran.myshop.R

object NotificationsService {

    fun getSilentNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, "silent_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("")
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setSilent(true)
            .build()
    }

}