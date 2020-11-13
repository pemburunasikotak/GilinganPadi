package com.example.mygilingan.pemilik


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygilingan.R
import com.example.mygilingan.adapter.Data_pesan_adapter
import com.example.mygilingan.model.Data_pesan
import kotlinx.android.synthetic.main.fragment_pesanan_pemilik.*

@Suppress("UNREACHABLE_CODE")
class Pesanan_pemilik : Fragment() {
    lateinit var btnambi: Button


    //data harusnya dari dataBase
    private val data = listOf(
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
    //fungsi bawaan dari Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pesanan_pemilik, container, false)
        val pesan = Data_pesan_adapter(data)

    }

    //Fungsi utama yang di jalankan di Fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_layout_pesan_pemilik.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = Data_pesan_adapter(data)
        }
    }

    interface RecyclerViewClickListener {
        fun onItemClicked(view: View, dataPesan: Data_pesan)

    }
    companion object {
            fun newInstance(): Pesanan_pemilik = Pesanan_pemilik()
    }
}