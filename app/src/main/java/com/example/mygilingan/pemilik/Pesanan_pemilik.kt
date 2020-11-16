package com.example.mygilingan.pemilik


import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygilingan.R
import com.example.mygilingan.TestMaps
import com.example.mygilingan.adapter.Data_pesan_adapter
import com.example.mygilingan.model.Data_pesan
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.fragment_pesanan_pemesan.*
import kotlinx.android.synthetic.main.fragment_pesanan_pemilik.*

@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class Pesanan_pemilik : Fragment(), OnMapReadyCallback {

    lateinit var permissionsManager: PermissionsManager
    lateinit var mapboxMap: MapboxMap
    private lateinit var mapView: MapView

    //data harusnya dari dataBase
    private val data = listOf(
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" ),
        Data_pesan(nama = "TEST", alamat ="TEST" )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
    //fungsi bawaan dari Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pesanan_pemilik, container, false)
        Data_pesan_adapter(data)

        Mapbox.getInstance(getContext()!!, getString(R.string.map_box_access_token))
        val view: View = inflater.inflate(R.layout.fragment_pesanan_pemilik, container, false)
        mapView = view.findViewById<View>(R.id.map_view_pemilik) as MapView
        mapView.onCreate(savedInstanceState)
        return view

    }

    //Fungsi utama yang di jalankan di Fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_layout_pesan_pemilik.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = Data_pesan_adapter(data)
        }

        //Maps
        initMapView(savedInstanceState)
        initPermissions()
    }

    interface RecyclerViewClickListener {
        //TestMap Activity

//        btn_ambil_layout_login.setOn
//        val intent = Intent (getActivity(), TestMaps::class.java)
//        getActivity()?.startActivity(intent)

    }
    companion object {

    }
    //Maps View Fungsi
    private fun initPermissions() {
        val permissionListener = object : PermissionsListener {
            override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
                /* Nothing to do in here */
            }

            override fun onPermissionResult(granted: Boolean) {
                if (granted) {
                    syncMapbox()
                } else {
                    val alertDialogInfo = context?.let {
                        androidx.appcompat.app.AlertDialog.Builder(it)
                            .setTitle(getString(R.string.info))
                            .setCancelable(false)
                            .setMessage(getString(R.string.permissions_denied))
                            .setPositiveButton(getString(R.string.dismiss)) { _, _ ->
                                onDestroy()
                            }
                            .create()
                    }
                    alertDialogInfo?.show()
                }
            }
        }
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            syncMapbox()
        } else {
            permissionsManager = PermissionsManager(permissionListener)
            permissionsManager.requestLocationPermissions(activity)
        }
    }

    override fun onStart() {
        super.onStart()
        map_view_pemilik.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view_pemilik.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_view_pemilik.onPause()
    }

    override fun onStop() {
        super.onStop()
        map_view_pemilik.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view_pemilik.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view_pemilik.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map_view_pemilik.onSaveInstanceState(outState)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        this.mapboxMap.setStyle(Style.MAPBOX_STREETS){
            //LifeLokasi
            showingDeviceLocation(mapboxMap)
        }
    }
    private fun initMapView(savedInstanceState: Bundle?) {
        map_view_pemilik.onCreate(savedInstanceState)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun syncMapbox() {
        map_view_pemilik.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun showingDeviceLocation(mapboxMap: MapboxMap) {
        val locationComponent = mapboxMap.locationComponent
        context?.let { locationComponent.activateLocationComponent(it, mapboxMap.style!!) }
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationComponent.isLocationComponentEnabled = true
        locationComponent.cameraMode = CameraMode.TRACKING
        locationComponent.renderMode = RenderMode.COMPASS
    }

}