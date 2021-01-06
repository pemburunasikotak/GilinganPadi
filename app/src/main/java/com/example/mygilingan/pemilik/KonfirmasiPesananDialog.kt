package com.example.mygilingan.pemilik

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import com.example.mygilingan.R
import com.example.mygilingan.adapter.Data_pesan_adapter
import com.example.mygilingan.utils.getAddress
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.dialog_konfirmasi_pesanan.*
import lib.alframeworkx.utils.DialogBuilder

class KonfirmasiPesananDialog(context: Context, val latLng: LatLng, val onKonfirmasiPesan: OnKonfirmasiPesanan) : DialogBuilder(context, R.layout.dialog_konfirmasi_pesanan) {
    init {

        with(dialog){
            setAnimation(R.style.DialogBottomAnimation)
            setFullScreen(lay_dialog)
            setGravity(Gravity.BOTTOM)

            edt_lokasi.setText(getAddress(latLng))

            btn_pesan.setOnClickListener({
                if(edt_plat.text.toString().equals("")) edt_plat.setError("Isi plat dulu")
                else {
                    onKonfirmasiPesan.onKonfirmasiPesanan(edt_plat.text.toString())
                    dismiss()
                }
            })

            show()
        }
    }

    interface OnKonfirmasiPesanan{
        fun onKonfirmasiPesanan(plat : String)
    }
}