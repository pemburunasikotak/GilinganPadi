package com.example.mygilingan.model

import android.icu.text.CaseMap
import com.google.gson.annotations.SerializedName

data class Pemesan(
    @SerializedName("alamat")
    var alamat: String = "",
    @SerializedName("lat")
    var lat : Double = 0.0,
    @SerializedName("lng")
    var lng : Double = 0.0) : Users() {
}