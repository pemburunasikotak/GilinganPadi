package com.example.mygilingan.pemilik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mygilingan.R
import com.example.mygilingan.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home_pemilik.*

class Home_pemilik : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_pemilik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gantinama()
    }
    //ganti nama Home
    private fun gantinama() {
        val dataRef = FirebaseDatabase.getInstance().getReference("USER")
        var user: Users? = null
        //var pengguna = FirebaseAuth.getInstance().currentUser
        //var test = pengguna?.uid.toString()

        val currentuser = FirebaseAuth.getInstance().currentUser?.email.toString()
        dataRef.orderByChild("email").equalTo(currentuser)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    //Log.d("apaini", user?.nama!!)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (snap in p0.children) {
                        user = snap.getValue(Users::class.java)
                    }
                    //Log.d("apaini", user?.nama!!)
                    // tvalamat_profil_pemilik_bawah.text = user!!.nama
                   // tvnotelfon_profil_pemilik_bawah.text = user!!.no_telp
                    tv_nama_home_pemilik.text = user!!.nama

                }
            })

    }

    companion object {
        fun newInstance():Home_pemilik{
            val fragment = Home_pemilik()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}