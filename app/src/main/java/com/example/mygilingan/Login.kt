package com.example.mygilingan

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.layout_login.*


@Suppress("DEPRECATION")
class Login : AppCompatActivity(){
    lateinit var btnlogin : Button
    lateinit var btndaftar:Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        auth = FirebaseAuth.getInstance()

        //inisialisasi
        btndaftar = findViewById(R.id.btn_daftar_layout_login)
        btnlogin = findViewById(R.id.btn_masuk_layout_login)
        //fungsi btn login
        btnlogin.setOnClickListener{
            cekLogin()
        }
        //fungsi btn daftar
        btndaftar.setOnClickListener {
            val intent = Intent(this, Registrasi::class.java)
            startActivity(intent)
            finish()
        }
        //fullscreen
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
    }

    private fun cekLogin() {
        if (input_text_email_login.text.toString().isEmpty()){
            input_text_email_login.error = "masukkan Email"
            input_text_email_login.requestFocus()
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(input_text_email_login.text.toString()).matches()){
            input_text_email_login.error= "masukkan Email Valid"
            input_text_email_login.requestFocus()

        }
        if (input_text_password_login.text.toString().isEmpty()){
            input_text_password_login.error ="masukkan Paswd yang benar"
            input_text_password_login.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(input_text_email_login.text.toString(),input_text_password_login.text.toString() )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }

            }
    }

    //fungsi untuk Verifikasi Email
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser:FirebaseUser?) {
        if (currentUser != null){
            if (currentUser.isEmailVerified){
                startActivity(Intent(this, Login_pilih::class.java))
                finish()
            }else{
                Toast.makeText(baseContext, "Email belum Di Verifikasi silahkan cek Email",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else{
//            Toast.makeText(baseContext, "Login failed.",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }
}