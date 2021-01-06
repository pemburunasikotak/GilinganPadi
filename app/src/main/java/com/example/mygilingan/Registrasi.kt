package com.example.mygilingan

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mygilingan.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_registrasi.*
import lib.alframeworkx.utils.AlRequest
import lib.alframeworkx.utils.AlStatic


@Suppress("DEPRECATION")
class Registrasi : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
     //   val registrasi = findViewById(R.id.btn_daftar_layout_registrasi) as Button
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_registrasi)
        auth = FirebaseAuth.getInstance()

        //save ke realtime database
        this.ref = FirebaseDatabase.getInstance().getReference("USER")
        btn_daftar_layout_registrasi.setOnClickListener {
            fungsiRegistarasi()
        }
        //fungsi agar dapat fullscreen
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
    }
    //save data Firebase Realtime database
    private fun savedata() {

        //ambil data uid dari auth
        //var uid = auth.currentUser
        var User = FirebaseAuth.getInstance().currentUser
        var uid = User?.uid.toString()

        //inisialisasi
        val nama = edit_text_name_registrasi.text.toString()
        var email = edit_text_email_registrasi.text.toString()
        val password = edit_text_password_registrasi.text.toString()
        val no_telp = edit_text_no_telp_registrasi.text.toString()
        val user = Users(nama, email, password, no_telp, uid)

        if (email.contains("@")) {
            email = email.split("@")[0];

            ref.child(email).setValue(user).addOnCompleteListener {
                AlStatic.hideLoadingDialog(this@Registrasi)
                Toast.makeText(this, "Successs",Toast.LENGTH_SHORT).show()
                edit_text_name_registrasi.setText("")
                edit_text_email_registrasi.setText("")
                edit_text_password_registrasi.setText("")
                this.edit_text_no_telp_registrasi.setText("")
                startActivity(Intent(this, Login::class.java))
                finish()
                //uid

            }
        } else {
            AlStatic.hideLoadingDialog(this@Registrasi)
        }
    }

    //registrasi ke authdatabes
    private fun fungsiRegistarasi() {
        //Validasi untuk data
        if (edit_text_name_registrasi.text.toString().isEmpty()) {
            edit_text_name_registrasi.error = "Masukkan Nama"
            edit_text_name_registrasi.requestFocus()
        }
        if (edit_text_email_registrasi.text.toString().isEmpty()) {
            edit_text_email_registrasi.error = "Masukkan Email"
            edit_text_email_registrasi.requestFocus()
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edit_text_email_registrasi.text.toString()).matches()) {
            edit_text_email_registrasi.error = "Masukkan Email Valid"
            edit_text_email_registrasi.requestFocus()
        }
        if (edit_text_no_telp_registrasi.text.toString().isEmpty()) {
            edit_text_no_telp_registrasi.error = "Masukkan No Telfon"
            edit_text_no_telp_registrasi.requestFocus()
        }
        if (edit_text_password_registrasi.toString().isEmpty()) {
            edit_text_password_registrasi.error = "Masukkan Paswd yang benar"
            edit_text_password_registrasi.requestFocus()
            return
        }

        //masuk ke Firebase Auth
        AlStatic.showLoadingDialog(this, R.drawable.ic_logo)
        auth.createUserWithEmailAndPassword(
            edit_text_email_registrasi.text.toString(),
            edit_text_password_registrasi.text.toString(),
        )
            .addOnCompleteListener(this@Registrasi) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { //task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    baseContext,
                                    "Silahkan Cek Email",
                                    Toast.LENGTH_SHORT
                                ).show()
                                savedata()
                            }
                        }
                } else {
                    AlStatic.hideLoadingDialog(this@Registrasi)
                    Toast.makeText(
                        baseContext, "Ulang Lagi",
                        Toast.LENGTH_SHORT
                    ).show()
//                    finish()
                }
            }

    }
}