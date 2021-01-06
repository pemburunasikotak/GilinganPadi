package com.example.mygilingan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mygilingan.model.Users
import com.example.mygilingan.utils.App
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profil_pemesan.*
import kotlinx.android.synthetic.main.layout_login.*
import lib.alframeworkx.utils.AlRequest
import lib.alframeworkx.utils.AlStatic


@Suppress("DEPRECATION")
class Login : AppCompatActivity(){
    lateinit var btnlogin : Button
    lateinit var btndaftar:Button
    lateinit var auth: FirebaseAuth
    lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("USER")

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

    fun getUser(email : String){
        var user: Users? = null
        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("apaini", user?.nama!!)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (snap in p0.children) {
                    user = snap.getValue(Users::class.java)
                }
                Log.d("getUser", Gson().toJson(user))
                AlStatic.setSPString(this@Login, App.instance.USER_KEY, Gson().toJson(user))
            }
        })
    }

    private fun cekLogin() {
        if (input_text_email_login.text.toString().isEmpty()){
            input_text_email_login.error = "masukkan Email"
            input_text_email_login.requestFocus()
        }
        /*if (!Patterns.EMAIL_ADDRESS.matcher(input_text_email_login.text.toString()).matches()){
            input_text_email_login.error= "masukkan Email Valid"
            input_text_email_login.requestFocus()

        }*/
        if (input_text_password_login.text.toString().isEmpty()){
            input_text_password_login.error ="masukkan Paswd yang benar"
            input_text_password_login.requestFocus()
            return
        }
        AlStatic.showLoadingDialog(this, R.drawable.ic_logo)
        auth.signInWithEmailAndPassword(input_text_email_login.text.toString(),input_text_password_login.text.toString() )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    FirebaseApp.initializeApp(this)
                    FirebaseMessaging.getInstance().subscribeToTopic(App.instance.FIREBASEPUSH_KEY)
                        .addOnCompleteListener { task ->
                            AlStatic.hideLoadingDialog(this@Login)
                            Toast.makeText(baseContext, "Sukses Login",
                                Toast.LENGTH_SHORT
                            ).show()
                            val user = auth.currentUser
                            updateUI(user)
                        }
                } else {
                    AlStatic.hideLoadingDialog(this@Login)
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
                getUser(currentUser.email!!)
                startActivity(Intent(this, Login_pilih::class.java))
                finish()
            }else{
                Toast.makeText(baseContext, "Email belum Di Verifikasi silahkan cek Email",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }else{
            Toast.makeText(baseContext, "Login failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}