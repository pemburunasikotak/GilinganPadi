package com.example.mygilingan.pemesan

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mygilingan.R
import com.example.mygilingan.R.string.map_box_access_token
import com.example.mygilingan.TestMaps
import com.example.mygilingan.model.DataPesananPemesan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.type.LatLng
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import kotlinx.android.synthetic.main.fragment_pesanan_pemesan.*
import kotlinx.android.synthetic.main.fragment_pesanan_pemesan.map_view
import kotlinx.android.synthetic.main.item_pesanan_pemilik.*
import kotlinx.android.synthetic.main.testmaps.*


class Pesanan_pemesan : Fragment(), OnMapReadyCallback {
    private lateinit var auth: FirebaseAuth
    lateinit var ref : DatabaseReference
    var harga : Int = 0

    lateinit var permissionsManager: PermissionsManager
    lateinit var mapboxMap: MapboxMap
    private lateinit var mapView: MapView
    private val markers = ArrayList<Marker>()
    private lateinit var currentRoute: DirectionsRoute
    private var navigationMapRoute: NavigationMapRoute? = null
    lateinit var originPosition: Point

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(getContext()!!, getString(map_box_access_token))
        val view: View = inflater.inflate(R.layout.fragment_pesanan_pemesan, container, false)
        mapView = view.findViewById<View>(R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        harga = 3000
        auth = FirebaseAuth.getInstance()
        //save ke realtime database Tabel Pesan
        this.ref = FirebaseDatabase.getInstance().getReference("Pesan")
        //Panggil Fungsi
        hitungJumlah()
        btn_pesan_lpp.setOnClickListener {
            pesanGilingan()
            val intent = Intent (getActivity(), TestMaps::class.java)
            getActivity()?.startActivity(intent)
        }

        //Mapbox.getInstance(getContext()!!, getString(map_box_access_token))
        //inisialisasi Permision di MapsBox
        initMapView(savedInstanceState)
        initPermissions()
    }

    private fun pesanGilingan() {
        var loglat :LatLng
    //   originPosition = Point.fromLngLat(loglat.longitude,loglat.longitude)
        //inisialisasi
        val id = ref.push().key.toString()
        val lokasi = et_ldp_inputlokasi.text.toString().trim()
        val jumlah = et_ldp_jumlah.text.toString().trim()
        val estimasi = tvbiyayapesananPemilik.text.toString().trim()
        val pesan = DataPesananPemesan(id, lokasi, jumlah, estimasi)
        ref.child(id).setValue(pesan)
    }


    private fun hitungJumlah() {
      //  et_ldp_jumlah.addTextChangedListener(textWatcher)

        btnEstimasi.setOnClickListener {
            if (et_ldp_jumlah.text.toString().isEmpty()){
                et_ldp_jumlah.setError("harus di isi")
            }else{
                val jml = et_ldp_jumlah.text.toString().toDouble()
                val jumlah = this.kali(jml)
                tvbiyayapesananPemilik.setText(jumlah.toString())
            }
        }
    }

//    private val textWatcher = object : TextWatcher {
//        override fun afterTextChanged(s: Editable?) {
//            tvbiyayapesananPemilik.setText(kali(et_ldp_jumlah.text.toString().toDouble()))
//        }
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//        }
//    }

    //hitung Jummlah
    private fun kali(jml: Double): Any {
        return jml* harga
    }

    companion object {
        fun newInstance():Pesanan_pemesan{
            val fragment = Pesanan_pemesan()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
       }

    //maps Box

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
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        this.mapboxMap.setStyle(Style.MAPBOX_STREETS){
            showingDeviceLocation(mapboxMap)
        }
    }
    private fun initMapView(savedInstanceState: Bundle?) {
        map_view.onCreate(savedInstanceState)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun syncMapbox() {
        map_view.getMapAsync(this)
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


