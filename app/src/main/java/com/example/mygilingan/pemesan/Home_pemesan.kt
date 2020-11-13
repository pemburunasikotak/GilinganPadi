package com.example.mygilingan.pemesan

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
import kotlinx.android.synthetic.main.fragment_home_pemesan.*

class Home_pemesan : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_pemesan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gantiNama()
    }
    //Fungsi untuk ganti nama Home
    private fun gantiNama() {
        //definisikan Table database
        val dataRef = FirebaseDatabase.getInstance().getReference("USER")
        //validasi user
        var user: Users? = null
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
                    tv_nama_home_pemesan.text = user!!.nama
                }
            })
    }

    companion object {
        fun newInstance():Home_pemesan{
            val fragment = Home_pemesan()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}