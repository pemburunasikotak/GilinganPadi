package com.example.mygilingan.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.mygilingan.R
import com.example.mygilingan.model.NotifPesanan
import com.example.mygilingan.pemilik.Home_pemilik_utama
import com.google.gson.Gson

class GilinganNotification(val context : Context, val notification: NotifPesanan) {
    var GROUP_KEY: String = "gilingan_" + 1
    var NOTIFICATION_CHANNEL_ID = "" + 1
    var notificationManager: NotificationManager? = null
    var notificationBuilder: NotificationCompat.Builder? = null

    init {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getString(R.string.app_name) + " Notifications", NotificationManager.IMPORTANCE_HIGH)
            // Configure the notification channel.
//            notificationChannel.description = context.getString(R.string.app_name)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = ContextCompat.getColor(context, R.color.colorPrimary)
            notificationChannel.enableVibration(true)
            notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
            notificationManager!!.createNotificationChannel(notificationChannel)
        }

        notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder!!.setAutoCancel(true)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logo)
                .setTicker(context.getString(R.string.app_name)) //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(notification.title)
                .setContentText(notification.description)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(notification.description))
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setContentInfo(context.getString(R.string.app_name))

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder!!.priority = NotificationCompat.PRIORITY_HIGH
        }

//        val resultIntent = Intent(context, KelolaProdukActivity::class.java)
        val resultIntent = Intent(context, Home_pemilik_utama::class.java)
        resultIntent.putExtra("data", Gson().toJson(notification))
        resultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP

        //Set a unique request code for this pending intent

        //Set a unique request code for this pending intent
        val resultPendingIntent = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder!!.setContentIntent(resultPendingIntent)

        notificationManager!!.notify(1, notificationBuilder!!.build())
    }
}