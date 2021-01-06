package com.example.mygilingan.utils

import android.util.Log
import com.example.mygilingan.model.NotifPesanan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class GilinganFirebaseMessaging : FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("ola",  "onMessageReceived data : " + remoteMessage.getData())

        val notification = Gson().fromJson(remoteMessage.getData().get("data").toString(), NotifPesanan::class.java)
        notification.title = remoteMessage.getData().get("title").toString()
        notification.description = remoteMessage.getData().get("description").toString()
        Log.d("ola",  "onMessageReceived data : " + notification.title+" = "+notification.description+" = "+notification.estimasi)
        if(!notification.pemesan.uid.equals(FirebaseAuth.getInstance().currentUser!!.uid)){
            GilinganNotification(applicationContext, notification)
        }
    }
}