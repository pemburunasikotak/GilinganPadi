package com.example.mygilingan.pemesan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.mygilingan.R
import com.example.mygilingan.R.string.map_box_access_token
import com.example.mygilingan.model.*
import com.example.mygilingan.utils.App
import com.example.mygilingan.utils.FragmentLocation
import com.example.mygilingan.utils.getAddress
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
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
import lib.alframeworkx.utils.AlRequest
import lib.alframeworkx.utils.AlStatic
import org.json.JSONObject
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList


class Pesanan_pemesan : FragmentLocation(), OnMapReadyCallback {
    private lateinit var auth: FirebaseAuth
    lateinit var ref : DatabaseReference
    var harga = BigInteger("3000")


    lateinit var permissionsManager: PermissionsManager
    lateinit var mapboxMap: MapboxMap
    private lateinit var mapView: MapView
    private val markers = ArrayList<Marker>()
    private lateinit var currentRoute: DirectionsRoute
    private var navigationMapRoute: NavigationMapRoute? = null
    lateinit var originPosition: Point

    lateinit var myloc : LatLng

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
        auth = FirebaseAuth.getInstance()
        //save ke realtime database Tabel Pesan
        this.ref = FirebaseDatabase.getInstance().getReference("Pesan")
        //Panggil Fungsi
        hitungJumlah()
        btn_pesan_lpp.setOnClickListener {
            pesanGilingan()
            /*val intent = Intent (getActivity(), TestMaps::class.java)
            getActivity()?.startActivity(intent)*/
        }

        initMapView(savedInstanceState)
        initPermissions()
    }

    private fun pesanGilingan() {
        val id = ref.push().key.toString()
        val lokasi = et_ldp_inputlokasi.text.toString().trim()
        val jumlah = et_ldp_jumlah.text.toString().trim()
        val estimasi = tvbiyayapesananPemilik.text.toString().trim()
        val pemesan = Gson().fromJson(AlStatic.getSPString(context, App.instance.USER_KEY), Pemesan::class.java)
        pemesan.alamat = lokasi
        pemesan.lat = myloc.latitude
        pemesan.lng = myloc.longitude
        val pesanan = DataPesananPemesan(id, pemesan, jumlah, estimasi)
        ref.child(id).setValue(pesanan, { error, ref ->
            if(error == null){
                AlRequest.POSTRaw("https://fcm.googleapis.com/fcm/send", context, object : AlRequest.OnPostRawRequest{
                    override fun onSuccess(response: JSONObject?) {
                        AlStatic.hideLoadingDialog(context)
                        Log.d("success", "Hi Success "+response)
                        AlStatic.ToastShort(context, "Berhasil request pesanan")
                        et_ldp_jumlah.setText("")
                        tvbiyayapesananPemilik.setText("0")
                    }

                    override fun onFailure(error: String?) {
                        AlStatic.hideLoadingDialog(context)
                        AlStatic.ToastShort(context, error)
                        Log.d("error", "Hi Error "+error)
                    }

                    override fun onPreExecuted() {
                        AlStatic.showLoadingDialog(context, R.drawable.ic_logo)
                    }

                    override fun requestParam(): String {
                        var notifPesanan = NotifPesanan("Pesanan Baru", "Ada pesanan baru sejumlah "+jumlah+" dengan estimasi biaya "+estimasi+" .Buka Gilinganpadi sekarang", pesanan)
                        Log.d("data", Gson().toJson(FcmData("/topics/"+App.instance.FIREBASEPUSH_KEY, notifPesanan)))
                        return Gson().toJson(FcmData("/topics/"+App.instance.FIREBASEPUSH_KEY, notifPesanan))
                    }

                    override fun requestHeaders(): MutableMap<String, String> {
                        val params: MutableMap<String, String> = HashMap()
                        params.put("Content-Type", "application/json")
                        params.put("Authorization", "key="+getString(R.string.firebase_key))
                        return params
                    }

                })
            }
        })
    }


    private fun hitungJumlah() {
        btnEstimasi.setOnClickListener {
            if (et_ldp_jumlah.text.toString().isEmpty()){
                et_ldp_jumlah.setError("harus di isi")
            }else{
                val jml = et_ldp_jumlah.text.toString()
                tvbiyayapesananPemilik.setText(kali(jml).toString())
            }
        }
    }

    //hitung Jummlah
    private fun kali(jml: String): BigInteger {
        return jml.toBigInteger() * harga
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

        requestMyLocation()
    }

    override fun onMyLocation(latlng: LatLng) {
        super.onMyLocation(latlng)
        myloc = latlng
        et_ldp_inputlokasi.setText(getAddress(latlng))
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


