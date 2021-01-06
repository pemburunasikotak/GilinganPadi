package com.example.mygilingan.utils

import android.Manifest
import android.location.Geocoder
import android.util.Base64
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_pesanan_pemesan.*
import java.io.IOException
import java.util.*
import java.util.Base64.getEncoder

fun toBase64(data : String): String{
     return Base64.encodeToString(data.toByteArray(), Base64.NO_WRAP)
}

val GPSPermissions = arrayOf(
     Manifest.permission.ACCESS_FINE_LOCATION,
     Manifest.permission.ACCESS_COARSE_LOCATION
)

fun getAddress(latlng : LatLng) : String{
     val geocoder = Geocoder(App.instance.applicationContext, Locale.getDefault())
     try {
          val addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
          val obj = addresses[0]
          var add = obj.getAddressLine(0)

          Log.v("IGA", "Address$add")
          if (add.contains("-")) add = add.split("-").toTypedArray()[0]
          return add
//          et_ldp_inputlokasi.setText(add)
     } catch (e: IOException) {
          e.printStackTrace()
     } catch (e: IndexOutOfBoundsException) {

     }

     return ""
}