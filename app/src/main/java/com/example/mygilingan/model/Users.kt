package com.example.mygilingan.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
open class Users(
    @SerializedName("nama")
    @Expose
    var nama:String ="",

    @SerializedName("email")
    @Expose
    var email: String ="",

    @SerializedName("password")
    @Expose
    var password : String = "",

    @SerializedName("no_telp")
    @Expose
    var no_telp: String= "",

    @SerializedName("uid")
    @Expose
    var uid:String =""
)