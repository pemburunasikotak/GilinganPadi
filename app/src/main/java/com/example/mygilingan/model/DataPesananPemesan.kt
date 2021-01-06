package com.example.mygilingan.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class DataPesananPemesan(
    @SerializedName("id")
    @Expose
    var id:String ="",

    @SerializedName("pemesan")
    @Expose
    var pemesan : Pemesan = Pemesan(),
/*
    @SerializedName("lokasi")
    @Expose
    var lokasi:String ="",*/

    @SerializedName("jumlah")
    @Expose
    var jumlah: String ="",

    @SerializedName("estimasi")
    @Expose
    var estimasi : String = "",
    @SerializedName("pemilik")
    @Expose
    var pemilik:Pemilik = Pemilik(),
)