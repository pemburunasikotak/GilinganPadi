package com.example.mygilingan.pemesan

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.example.mygilingan.R
import com.example.mygilingan.model.DataPesananPemesan
import com.example.mygilingan.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_pesanan_pemesan.*
import kotlinx.android.synthetic.main.layout_registrasi.*

class Pesanan_pemesan : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var ref : DatabaseReference
    var harga : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pesanan_pemesan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        harga = 3000
        auth = FirebaseAuth.getInstance()

        //save ke realtime database Tabel Pesan
        this.ref = FirebaseDatabase.getInstance().getReference("Pesan")

        //Panggil Fungsi
        hitungJumlah()
        btn_pesan_lpp.setOnClickListener {
            pesanGilingan()
        }

    }

    private fun pesanGilingan() {
        //inisialisasi
        val id = ref.push().key.toString()
        val lokasi = et_ldp_inputlokasi.text.toString().trim()
        val jumlah = et_ldp_jumlah.text.toString().trim()
        val estimasi = tvbiyayapesananPemilik.text.toString().trim()
        val pesan = DataPesananPemesan(id,lokasi,jumlah, estimasi )
        ref.child(id).setValue(pesan)
    }


    private fun hitungJumlah() {
      //  et_ldp_jumlah.addTextChangedListener(textWatcher)

        btnEstimasi.setOnClickListener {
            if (et_ldp_jumlah.text.toString().isEmpty()){
                et_ldp_jumlah.setError("harus di isi")
            }else{
                val jml = et_ldp_jumlah.text.toString().toDouble()
                val jumlah = this.kali(jml)
                tvbiyayapesananPemilik.setText(jumlah.toString())
            }
        }
    }

//    private val textWatcher = object : TextWatcher {
//        override fun afterTextChanged(s: Editable?) {
//            tvbiyayapesananPemilik.setText(kali(et_ldp_jumlah.text.toString().toDouble()))
//        }
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//        }
//    }

    //hitung Jummlah
    private fun kali(jml: Double): Any {
        return jml* harga
    }

    companion object {
        fun newInstance():Pesanan_pemesan{
            val fragment = Pesanan_pemesan()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
       }
}

