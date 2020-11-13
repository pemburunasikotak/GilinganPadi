package com.example.mygilingan.pemesan

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mygilingan.R
import kotlinx.android.synthetic.main.activity_home_pemesan.*

class Home_pemesan_utama : AppCompatActivity() {
    private var content: FrameLayout? = null
    private val home = Home_pemesan()
    private val pesanan = Pesanan_pemesan()
    private val profil = Profil_pemesan()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pemesan)
        replaceFragment(home)

        button_nav_pemesan.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home->replaceFragment(home)
                R.id.navigation_pesanan-> replaceFragment(pesanan)
                R.id.navigation_profil -> replaceFragment(profil)
            }
            true
        }
    }
    private fun  replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}