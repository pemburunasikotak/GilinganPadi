package com.example.mygilingan.pemilik

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mygilingan.R
import kotlinx.android.synthetic.main.activity_home_pemilik.*

class Home_pemilik_utama : AppCompatActivity() {
    private var content: FrameLayout? = null
    private var home : Home_pemilik? = null
    private var pesanan : Pesanan_pemilik? = null
    private var profil : Profil_pemilik? = null

    var active_fragment = 0
    var after_active_fragment = 0

    internal var mFragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pemilik)

        home = Home_pemilik.newInstance()
        pesanan = Pesanan_pemilik.newInstance()
        profil = Profil_pemilik.newInstance()

        initializeNavFragment(home!!)

        button_nav_pemilik.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home-> {
                    initializeNavFragment(home!!)
                }
                R.id.navigation_pesanan-> {
                    initializeNavFragment(pesanan!!)
                }
                R.id.navigation_profil -> {
                    initializeNavFragment(profil!!)
                }
            }
            true
        }
    }

    //digunakan untuk mengganti Tampilan Fragment
    private fun initializeNavFragment(curFragment: Fragment) {
        val transaction: FragmentTransaction = mFragmentManager.beginTransaction()
        if (mFragmentManager.findFragmentByTag(curFragment.javaClass.getSimpleName()) == null) {
            transaction.add(
                R.id.page_container,
                curFragment,
                curFragment.javaClass.getSimpleName()
            )
        }
        Log.d("hi", ""+home!!.javaClass.getSimpleName())
        val tagMain: Fragment? = mFragmentManager.findFragmentByTag(home!!.javaClass.getSimpleName())
        val tagPesanan: Fragment? = mFragmentManager.findFragmentByTag(pesanan!!.javaClass.getSimpleName())
        val tagProfil: Fragment? = mFragmentManager.findFragmentByTag(profil!!.javaClass.getSimpleName())
        hideFragment(transaction, tagMain, tagPesanan, tagProfil)
        showFragment(curFragment, transaction, tagMain, tagPesanan, tagProfil)
        after_active_fragment = active_fragment
        transaction.commitAllowingStateLoss()
    }

    private fun showFragment(
        curFragment: Fragment,
        transaction: FragmentTransaction,
        tagMain: Fragment?,
        tagPesanan: Fragment?,
        tagProfil: Fragment?
    ) {
        if (curFragment.javaClass.getSimpleName()
                .equals(home?.javaClass?.getSimpleName())
        ) {
            if (tagMain != null) {
                transaction.show(tagMain)
            }
        }

        if (curFragment.javaClass.getSimpleName()
                .equals(pesanan?.javaClass?.getSimpleName())
        ) {
            if (tagPesanan != null) {
                transaction.show(tagPesanan)
            }
        }

        if (curFragment.javaClass.getSimpleName()
                .equals(profil?.javaClass?.getSimpleName())
        ) {
            if (tagProfil != null) {
                transaction.show(tagProfil)
            }
        }
    }

    private fun hideFragment(
        transaction: FragmentTransaction,
        tagMain: Fragment?,
        tagPesanan: Fragment?,
        tagProfil: Fragment?
    ) {
        if (tagMain != null) {
            transaction.hide(tagMain)
        }
        if (tagPesanan != null) {
            transaction.hide(tagPesanan)
        }
        if (tagProfil != null) {
            transaction.hide(tagProfil)
        }
    }

}