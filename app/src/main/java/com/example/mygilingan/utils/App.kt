package com.example.mygilingan.utils

import com.example.mygilingan.R
import com.example.mygilingan.model.Users
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import lib.alframeworkx.SuperUser.RequestHandler
import lib.alframeworkx.utils.AlStatic


class App : RequestHandler() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val FIREBASEPUSH_KEY = "gilinganpadi"
    val USER_KEY = "gilinganpadi_user"

    companion object {
        @JvmStatic
        lateinit var instance: App
            private set
    }

    fun getFcmToken() : String{
        return getString(R.string.firebase_key)
    }

    fun logout(){
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().unsubscribeFromTopic(FIREBASEPUSH_KEY)
        AlStatic.setSPString(this, USER_KEY, "")
        FirebaseAuth.getInstance().signOut()
    }

    fun editUser(phone : String){
        val user = Gson().fromJson(AlStatic.getSPString(this, USER_KEY), Users::class.java)
        user.no_telp = phone
        FirebaseDatabase.getInstance().getReference("USER").child(user.email.split("@")[0]).setValue(user)
    }
}