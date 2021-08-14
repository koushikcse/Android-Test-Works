package com.mapdemo

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.mapdemo.Utils.addAllShapes
import com.mapdemo.Utils.animateCameraToPosition
import com.mapdemo.Utils.bitmapDescriptorFromVector
import com.mapdemo.Utils.dataList
import com.mapdemo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    LocationListener, ListCallback {

    private lateinit var binding: ActivityMainBinding

    private var googleMap: GoogleMap? = null
    private var mapEditMode = "circle" //circle, rect, line, polygon

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    val fields = listOf(Place.Field.ID, Place.Field.NAME)

    private val PERMISSION_REQUEST_CODE: Int = 101
    private val INTENT_REQUEST_CODE: Int = 102

    var locationManager: LocationManager? = null
    private var myLocationMarker: Marker? = null
    private var myLatitude: String? = null
    private var myLongitude: String? = null
    private var bottomSheetFragment: AreaListBottomSheetFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.satelliteView.setOnClickListener(this)
        binding.normalView.setOnClickListener(this)
        binding.hybridView.setOnClickListener(this)
        binding.terrainView.setOnClickListener(this)
        binding.circleBtn.setOnClickListener(this)
        binding.polylineBtn.setOnClickListener(this)
        binding.polygonBtn.setOnClickListener(this)
        binding.cleanBtn.setOnClickListener(this)
        binding.listBtn.setOnClickListener(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        setDefaultButtons()
        setDefaultMapButtons()
        binding.normalView.setBackgroundColor(resources.getColor(R.color.light))

        Places.initialize(applicationContext, "please add your map key");
//        val placesClient: PlacesClient = Places.createClient(this)

        binding.search.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        if (!checkPermission())
            requestPermission()
        else {
            getLocation()
        }
        println("Main onCreate")
    }

    override fun onStart() {
        super.onStart()
        println("Main onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Main onResume")
    }
    override fun onPause() {
        super.onPause()
        println("Main onPause")
    }

    override fun onRestart() {
        super.onRestart()
        println("Main onRestart")
    }

    override fun onStop() {
        super.onStop()
        println("Main onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        println("Main onDestroy")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.e("####", "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.e("####", status.statusMessage.toString())
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        } else if (requestCode == INTENT_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    addAllShapes(googleMap)
                    myLocationMarker = null
                    getLocation()
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isCompassEnabled = false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.satellite_view -> {
                setDefaultMapButtons()
                binding.satelliteView.setBackgroundColor(resources.getColor(R.color.light))
                googleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            R.id.normal_view -> {
                setDefaultMapButtons()
                binding.normalView.setBackgroundColor(resources.getColor(R.color.light))
                googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            R.id.hybrid_view -> {
                setDefaultMapButtons()
                binding.hybridView.setBackgroundColor(resources.getColor(R.color.light))
                googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
            R.id.terrain_view -> {
                setDefaultMapButtons()
                binding.terrainView.setBackgroundColor(resources.getColor(R.color.light))
                googleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
            R.id.circle_btn -> {
                setDefaultButtons()
                binding.circleBtn.setBackgroundColor(resources.getColor(R.color.light))
                mapEditMode = "circle"
                startActivityForResult(
                    Intent(this, MapActivity::class.java).putExtra(
                        "type",
                        mapEditMode
                    )
                        .putExtra("myLatitude", myLatitude.toString())
                        .putExtra("myLongitude", myLongitude.toString()), INTENT_REQUEST_CODE
                )
            }
            R.id.polyline_btn -> {
                setDefaultButtons()
                binding.polylineBtn.setBackgroundColor(resources.getColor(R.color.light))
                mapEditMode = "polyline"
                startActivityForResult(
                    Intent(this, MapActivity::class.java).putExtra(
                        "type",
                        mapEditMode
                    )
                        .putExtra("myLatitude", myLatitude.toString())
                        .putExtra("myLongitude", myLongitude.toString()), INTENT_REQUEST_CODE
                )
            }
            R.id.polygon_btn -> {
                setDefaultButtons()
                binding.polygonBtn.setBackgroundColor(resources.getColor(R.color.light))
                mapEditMode = "polygon"
                startActivityForResult(
                    Intent(this, MapActivity::class.java).putExtra(
                        "type",
                        mapEditMode
                    )
                        .putExtra("myLatitude", myLatitude.toString())
                        .putExtra("myLongitude", myLongitude.toString()), INTENT_REQUEST_CODE
                )
            }
            R.id.clean_btn -> {
                dataList.clear()
                googleMap?.clear()
                myLocationMarker = null
                getLocation()
            }
            R.id.list_btn -> {
                openBottomList()
            }
        }
    }

    private fun setDefaultMapButtons() {
        binding.satelliteView.setBackgroundColor(resources.getColor(R.color.dark))
        binding.normalView.setBackgroundColor(resources.getColor(R.color.dark))
        binding.hybridView.setBackgroundColor(resources.getColor(R.color.dark))
        binding.terrainView.setBackgroundColor(resources.getColor(R.color.dark))
    }

    private fun setDefaultButtons() {
        binding.circleBtn.setBackgroundColor(resources.getColor(R.color.dark))
        binding.polylineBtn.setBackgroundColor(resources.getColor(R.color.dark))
        binding.polygonBtn.setBackgroundColor(resources.getColor(R.color.dark))
        binding.cleanBtn.setBackgroundColor(resources.getColor(R.color.dark))
    }


    /**
     *    permission works
     */
    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                getLocation()
            } else {
                Toast.makeText(this, " Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun getLocation() {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        setMyLocationOnMap(location)
        animateCameraToPosition(
            LatLng(
                location.latitude,
                location.longitude
            ), googleMap
        )
        myLatitude = location.latitude.toString()
        myLongitude = location.longitude.toString()
    }

    private fun setMyLocationOnMap(location: Location) {
        if (myLocationMarker != null) {
            myLocationMarker?.position = LatLng(location.latitude, location.longitude)
        } else {
            val markerOptions = MarkerOptions()
                .position(LatLng(location.latitude, location.longitude))
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_my_location))
                .draggable(false)
                .title("Its me")
            myLocationMarker = googleMap?.addMarker(markerOptions)
        }
    }

    private fun openBottomList() {
        if (dataList.size != 0) {
            bottomSheetFragment = AreaListBottomSheetFragment(this)
            bottomSheetFragment?.show(supportFragmentManager, "bottomfrag")
        } else {
            Toast.makeText(this, "Please draw area first!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemClick(pos: Int) {
        bottomSheetFragment?.dismiss()
        startActivityForResult(
            Intent(this, MapActivity::class.java).putExtra(
                "type",
                dataList[pos].type
            )
                .putExtra("pos", pos)
                .putExtra("myLatitude", myLatitude.toString())
                .putExtra("myLongitude", myLongitude.toString()), INTENT_REQUEST_CODE
        )
    }

    override fun onDeleteClick(area: Area) {
        if (dataList.size == 1)
            bottomSheetFragment?.dismiss()
        dataList.remove(area)
        addAllShapes(googleMap)
        bottomSheetFragment?.areaAdapter?.notifyDataSetChanged()

    }

}

