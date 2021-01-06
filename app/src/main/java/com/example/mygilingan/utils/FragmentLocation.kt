package com.example.mygilingan.utils

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import lib.alframeworkx.Activity.FragmentPermission
import lib.alframeworkx.Activity.Interfaces.PermissionResultInterface

open class FragmentLocation : FragmentPermission(), OnLocationChanged {

    var locationRequest: LocationRequest? = null
    var googleApiClient: GoogleApiClient? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun requestMyLocation(){
        askCompactPermissions(GPSPermissions, object : PermissionResultInterface {
            override fun permissionDenied() {

            }

            override fun permissionGranted() {
                googleApiClient = GoogleApiClient.Builder(requireContext()).addApi(LocationServices.API)
                    .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks{
                        override fun onConnected(p0: Bundle?) {
                            startLocationUpdates()
                        }

                        override fun onConnectionSuspended(p0: Int) {

                        }

                    })
                    .addOnConnectionFailedListener(object : GoogleApiClient.OnConnectionFailedListener{
                        override fun onConnectionFailed(p0: ConnectionResult) {

                        }

                    }).build()
                googleApiClient!!.connect()
            }

        })
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        //        locationRequest.setInterval(UPDATE_INTERVAL);
//        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, object :
            LocationListener {
            override fun onLocationChanged(p0: Location?) {
                val latlng = LatLng(
                    p0!!.latitude,
                    p0!!.longitude
                );

                onMyLocation(latlng)
//
            }
        })
    }

    override fun onMyLocation(latlng : LatLng) {

    }
}

interface OnLocationChanged{
    fun onMyLocation(latlng : LatLng)
}