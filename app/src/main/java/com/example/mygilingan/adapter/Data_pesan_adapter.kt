package com.example.mygilingan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygilingan.model.Data_pesan
import com.example.mygilingan.pemilik.Pesanan_pemilik

class Data_pesan_adapter(private val pesan : List<Data_pesan>):
    RecyclerView.Adapter<Data_pesan_holder>() {

    var listener: Pesanan_pemilik.RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Data_pesan_holder {
        val inflater = LayoutInflater.from(parent.context)
        return Data_pesan_holder(inflater,parent)

    }

    override fun onBindViewHolder(holder: Data_pesan_holder, position: Int) {
        val dataPesan: Data_pesan = pesan[position]
        holder.bind(dataPesan)
    }

    override fun getItemCount(): Int = pesan.size
}