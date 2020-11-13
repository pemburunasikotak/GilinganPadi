package com.example.mygilingan.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataPesananPemesan(
    @SerializedName("id")
    @Expose
    var id:String ="",


    @SerializedName("lokasi")
    @Expose
    var lokasi:String ="",

    @SerializedName("jumlah")
    @Expose
    var jumlah: String ="",

    @SerializedName("estimasi")
    @Expose
    var estimasi : String = "",
)