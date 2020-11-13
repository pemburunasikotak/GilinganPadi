package com.example.mygilingan

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mygilingan.pemesan.Home_pemesan_utama
import com.example.mygilingan.pemilik.Home_pemilik_utama


@Suppress("DEPRECATION")
class Login_pilih : AppCompatActivity(){
    //fungsi pertama yang dialankan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login_pilih)
        //deklarasi Button
        val pemesan = findViewById(R.id.btn_masuk_pemesan_login_pilih) as Button
        val pemilik = findViewById(R.id.btn_masuk_pemilik_login_pilih) as Button
        pemilik.setOnClickListener {
            val intent = Intent(this, Home_pemilik_utama::class.java)
            startActivity(intent)
        }
        //Fungsi Tombol pesan ketik di clik
        pemesan.setOnClickListener {
            val intent = Intent(this, Home_pemesan_utama::class.java)
            startActivity(intent)
        }
        //Fungsi FullScreen
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )

    }
}