package com.example.mygilingan.pemilik


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygilingan.R
import com.example.mygilingan.adapter.Data_pesan_adapter
import com.example.mygilingan.model.DataPesananPemesan
import com.example.mygilingan.model.Pemilik
import com.example.mygilingan.model.Users
import com.example.mygilingan.utils.App
import com.example.mygilingan.utils.FragmentLocation
import com.example.mygilingan.utils.getAddress
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import com.google.gson.Gson
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.fragment_pesanan_pemilik.*
import lib.alframeworkx.utils.AlStatic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class Pesanan_pemilik : FragmentLocation(), OnMapReadyCallback {

    val TAG = "Pesanan_pemilik"
    private val ROUTE_SOURCE_ID = "route-source-id"
    private val ROUTE_LAYER_ID = "route-layer-id"
    lateinit var permissionsManager: PermissionsManager
    lateinit var mapboxMap: MapboxMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    lateinit var mystyle : Style
    lateinit var ref : DatabaseReference
    lateinit var adapt : Data_pesan_adapter
    lateinit var myloc : LatLng

    private var client: MapboxDirections? = null
    //fungsi bawaan dari Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(getContext()!!, getString(R.string.map_box_access_token))
        val view: View = inflater.inflate(R.layout.fragment_pesanan_pemilik, container, false)
        mapView = view.findViewById<View>(R.id.map_view_pemilik) as MapView
        mapView.onCreate(savedInstanceState)
        return view

    }

    //Fungsi utama yang di jalankan di Fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapt = Data_pesan_adapter(object : Data_pesan_adapter.OnKonfirmasiPesan{
            override fun onKonfirmasiPesan(dataPesan: DataPesananPemesan) {
                KonfirmasiPesananDialog(requireContext(), myloc, object : KonfirmasiPesananDialog.OnKonfirmasiPesanan{
                    override fun onKonfirmasiPesanan(plat: String) {
                        val pemilik = Gson().fromJson(AlStatic.getSPString(context, App.instance.USER_KEY), Pemilik::class.java)
                        pemilik.platnomor = plat
                        pemilik.alamat = getAddress(myloc)
                        pemilik.lat = myloc.latitude
                        pemilik.lng = myloc.longitude
                        dataPesan.pemilik = pemilik

                        ref.child(dataPesan.id).setValue(dataPesan).addOnCompleteListener {
                            mapboxMap.removeAnnotations()
                            initSource(mystyle, dataPesan.id)
                            initLayers(mystyle, dataPesan.id)
                            setMarker(com.mapbox.mapboxsdk.geometry.LatLng(dataPesan.pemesan.lat, dataPesan.pemesan.lng))
                            getRoute(Point.fromLngLat(myloc.longitude, myloc.latitude), Point.fromLngLat(dataPesan.pemesan.lng, dataPesan.pemesan.lat))
                            getDatas()
                            bottom.visibility = View.GONE
                        }
                    }
                })
            }
        })
        rv_layout_pesan_pemilik.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = adapt
        }
        ref = FirebaseDatabase.getInstance().getReference("Pesan")

        //Maps
        initMapView(savedInstanceState)
        initPermissions()

        getDatas()
        requestMyLocation()
    }

    private fun setMarker(latlng: com.mapbox.mapboxsdk.geometry.LatLng){
        Log.d(TAG, "lat = "+latlng.latitude+ " longitude"+latlng.longitude)

        var symbolManager: SymbolManager
        symbolManager = SymbolManager(mapView, mapboxMap, mapboxMap.style!!)
        symbolManager.iconAllowOverlap = true //your choice t/f

        symbolManager.textAllowOverlap = false //your choice t/f

        symbolManager.addClickListener(object : OnSymbolClickListener {
            override fun onAnnotationClick(symbol: Symbol) {
                Toast.makeText(
                    activity,
                    "clicked  " + symbol.textField.toLowerCase(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        var bm: Bitmap = resources.getDrawable(R.drawable.mymarker).toBitmap()
        mapboxMap.getStyle()!!.addImage("my-marker", bm)
        symbolManager.create(SymbolOptions()
            .withLatLng(latlng)
            .withIconImage("my-marker")
            //set the below attributes according to your requirements
            .withIconSize(1.5f)

            .withZIndex(10)

            .withTextHaloColor("rgba(255, 255, 255, 100)")
            .withTextHaloWidth(5.0f)
            .withTextAnchor("top")

            .setDraggable(false))

        val position: CameraPosition = CameraPosition.Builder()
            .target(latlng)
            .zoom(10.0)
            .tilt(20.0)
            .build()
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
    }

    private fun getRoute(origin: Point, destination: Point) {
        Log.d(TAG, "lat origin = "+origin.latitude()+ " longitude origin = "+origin.longitude())
        Log.d(TAG, "lat destination = "+destination.latitude()+ " longitude destination = "+destination.longitude())
        client = MapboxDirections.builder()
            .origin(origin)
            .destination(destination)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .accessToken(getString(R.string.map_box_access_token))
            .build()

        client!!.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onResponse(
                call: Call<DirectionsResponse?>?,
                response: Response<DirectionsResponse?>
            ) {
                Log.d(TAG, "Response code: " + response.code())
                if (response.body() == null) {
                    Log.d(TAG,
                        "No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    Log.d(TAG, "No routes found")
                    return
                }

                val currentRoute = response.body()!!.routes().get(0)
                if (mapboxMap != null) {
                    mapboxMap.getStyle { style ->
                        val source: GeoJsonSource = style.getSourceAs(ROUTE_SOURCE_ID)!!
                        if (source != null) {
                            Log.d(TAG, "onResponse: source != null")
                            source.setGeoJson(
                                FeatureCollection.fromFeature(
                                    Feature.fromGeometry(
                                        LineString.fromPolyline(
                                            currentRoute.geometry()!!,
                                            PRECISION_6
                                        )
                                    )
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DirectionsResponse?>?, throwable: Throwable) {
                Log.d(TAG, "Error: " + throwable.message)
                Toast.makeText(
                    context, "Error: " + throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    override fun onMyLocation(latlng: LatLng) {
        super.onMyLocation(latlng)
        myloc = latlng
    }

    fun getDatas(){
        tv_nodata.visibility = View.GONE
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var list : MutableList<DataPesananPemesan> = ArrayList()
                val user = Gson().fromJson(AlStatic.getSPString(context, App.instance.USER_KEY), Users::class.java)
                for (snap in snapshot.children) {
                    val x = snap.getValue(DataPesananPemesan::class.java)
                    if(x?.pemilik?.uid.equals("") && !x?.pemesan?.uid.equals(user.uid)){
                        list.add(x!!)
                    }
                }

                list.let {
                    adapt.setDatas(it)
                    if(it.size == 0)tv_nodata.visibility = View.VISIBLE
                }
            }

        })
    }


    companion object {
        fun newInstance(): Pesanan_pemilik {
            val fragment =
                Pesanan_pemilik()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
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
        map_view_pemilik?.onDestroy()
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
            mystyle = it
            initSource(it, "")
            initLayers(it, "")
        }
    }

    private fun initLayers(loadedMapStyle: Style, id : String) {
        val routeLayer = LineLayer(ROUTE_LAYER_ID+id, ROUTE_SOURCE_ID+id)
        routeLayer.setProperties(
            lineCap(Property.LINE_CAP_ROUND),
            lineJoin(Property.LINE_JOIN_ROUND),
            lineWidth(5f),
            lineColor(Color.parseColor("#008577"))
        )
        loadedMapStyle.addLayer(routeLayer)
    }

    private fun initSource(loadedMapStyle: Style, id: String) {
        loadedMapStyle.addSource(
            GeoJsonSource(
                ROUTE_SOURCE_ID+id,
                FeatureCollection.fromFeatures(arrayOf())
            )
        )
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