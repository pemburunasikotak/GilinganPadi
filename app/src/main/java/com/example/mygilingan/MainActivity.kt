package com.example.mygilingan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    //fungsi yangn pertama kali di jalankan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splas)
        splasScreen()
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
    }
    //digunakan untuk memangil SpaassCreen selamaa 1000 mili detik
    private fun splasScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@MainActivity, Login::class.java))
            finish()
        },1000)

    }
}