package com.example.mygilingan.model

data class FcmData(var to : String = "", var data : NotifPesanan){
    data class NotifData(val body : String, val title : String)
}