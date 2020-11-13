package com.example.mygilingan.pemilik

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mygilingan.R
import kotlinx.android.synthetic.main.activity_home_pemilik.*

class Home_pemilik_utama : AppCompatActivity() {
    private var content: FrameLayout? = null
    private val home = Home_pemilik()
    private val pesanan = Pesanan_pemilik()
    private val profil = Profil_pemilik()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pemilik)
        replaceFragment(home)
        //Fragement yang diganti  berdasarkan ID
        button_nav_pemilik.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home->replaceFragment(home)
                R.id.navigation_pesanan-> replaceFragment(pesanan)
                R.id.navigation_profil -> replaceFragment(profil)
            }
            true
        }
    }
    //digunakan untuk mengganti Tampilan Fragment
    private fun  replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.page_container, fragment)
        transaction.commit()
    }
}