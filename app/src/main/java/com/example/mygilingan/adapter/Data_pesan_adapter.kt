package com.example.mygilingan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygilingan.R
import com.example.mygilingan.model.DataPesananPemesan
import kotlinx.android.synthetic.main.item_pesanan_pemilik.view.*

class Data_pesan_adapter(val onKonfirmasiPesan: OnKonfirmasiPesan): RecyclerView.Adapter<Data_pesan_adapter.Holder>() {

    var data : MutableList<DataPesananPemesan> = ArrayList()

    fun setDatas(list : MutableList<DataPesananPemesan>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater,parent)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataPesan: DataPesananPemesan = data.get(position)
        holder.bindTo(dataPesan, onKonfirmasiPesan)
    }

    override fun getItemCount(): Int = data.size

    class Holder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_pesanan_pemilik, parent,false)){
        fun bindTo(dataPesan: DataPesananPemesan, onKonfirmasiPesan: OnKonfirmasiPesan) : Unit = with(itemView) {
            item_nama_recyclerview.text = dataPesan.pemesan.nama
            item_alamat_recyclerview.text = dataPesan.pemesan.alamat
            btn_ambil.setOnClickListener({
                onKonfirmasiPesan.onKonfirmasiPesan(dataPesan)
            })
        }
    }

    interface OnKonfirmasiPesan{
        fun onKonfirmasiPesan(dataPesan: DataPesananPemesan)
    }
}