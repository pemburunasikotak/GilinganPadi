package com.example.mygilingan.pemesan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mygilingan.Login
import com.example.mygilingan.R
import com.example.mygilingan.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profil_pemesan.*

class Profil_pemesan : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil_pemesan, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Baca dara dari Firebase untuk mengisi Fild Profile
        getUserProfile()

        btn_keluar_profilPemesan.setOnClickListener {
            menuKeluar()
        }
    }
    //menu Keluar
    private fun menuKeluar() {
        //Mendapatkan Instance dari Database
        FirebaseAuth.getInstance().signOut()
        requireActivity().run{
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
    fun getUserProfile() {
        val dataRef = FirebaseDatabase.getInstance().getReference("USER")
        var user: Users? = null
        //FirebaseAuth.getInstance().currentUser
       // var test = pengguna?.uid.toString()

        val currentuser = FirebaseAuth.getInstance().currentUser?.email.toString()

        print(currentuser)
        Log.d("Halo", currentuser)

        dataRef.orderByChild("email").equalTo(currentuser)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.d("apaini", user?.nama!!)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (snap in p0.children) {
                        user = snap.getValue(Users::class.java)
                    }
                    //Log.d("apaini", user?.nama!!)
                    // tvalamat_profil_pemilik_bawah.text = user!!.nama


                    tvnotelfon_profil_pemesan_bawah.text = user!!.no_telp
                    tvemail_profil_pemesan_bawah.text = user!!.email
                    tvemail_profil_pemesan.text = user!!.email
                    tvnama_profil_pemesan.text = user!!.nama
                }
            })
    }

    companion object {
        fun newInstance():Profil_pemesan{
            val fragment = Profil_pemesan()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}