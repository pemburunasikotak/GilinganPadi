package com.example.mygilingan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mygilingan.R
import com.example.mygilingan.model.Data_pesan

class Data_pesan_holder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_pesanan_pemilik, parent,false)){
    private var nama:TextView? = null
    private var alamat:TextView?=null

    init {
        nama=itemView.findViewById(R.id.item_nama_recyclerview)
        alamat = itemView.findViewById(R.id.item_alamat_recyclerview)
    }
    fun bind(dataPesan: Data_pesan){
        nama?.text= dataPesan.nama
        alamat?.text = dataPesan.alamat
    }
}