package com.mapdemo

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mapdemo.Utils.addCircle
import com.mapdemo.Utils.animateCameraToPosition
import com.mapdemo.Utils.dataList
import com.mapdemo.databinding.ActivityMainBinding
import com.mapdemo.databinding.ActivityMapBinding
import java.util.*
import kotlin.collections.ArrayList

class MapActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, SeekBar.OnSeekBarChangeListener {

    private lateinit var binding: ActivityMapBinding

    private var googleMap: GoogleMap? = null

    private var markerList: ArrayList<Marker> = ArrayList()
    private var polygon: Polygon? = null
    private var circle: Circle? = null
    private var polyline: Polyline? = null

    private var mapEditMode = ""
    private var myLatitude: String? = null
    private var myLongitude: String? = null
    private var progress: Double = 100.0
    private var area: Area? = null
    private var editPos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.backBtn.setOnClickListener(this)
        binding.doneBtn.setOnClickListener(this)
        binding.seekbar.setOnSeekBarChangeListener(this)
        mapEditMode = intent.getStringExtra("type").toString()
        myLatitude = intent.getStringExtra("myLatitude")
        myLongitude = intent.getStringExtra("myLongitude")
        editPos = intent.getIntExtra("pos", -1)
        if (editPos != -1)
            area = dataList[editPos]
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        println("Map onCreate")
    }

    override fun onStart() {
        super.onStart()
        println("Map onStart")
    }

    override fun onResume() {
        super.onResume()
        println("Map onResume")
    }
    override fun onPause() {
        super.onPause()
        println("Map onPause")
    }

    override fun onRestart() {
        super.onRestart()
        println("Map onRestart")
    }

    override fun onStop() {
        super.onStop()
        println("Map onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        println("Map onDestroy")
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isCompassEnabled = false
        map.setOnMapClickListener(this)
        map.setOnMarkerDragListener(this)
        if (!myLatitude.isNullOrEmpty() && !myLongitude.isNullOrEmpty())
            showLocation()
        if (area != null)
            addArea()
    }

    private fun showLocation() {
        if (!myLatitude.isNullOrEmpty() && !myLongitude.isNullOrEmpty()) {
            val markerOptions = MarkerOptions()
                .position(LatLng(myLatitude!!.toDouble(), myLongitude!!.toDouble()))
                .icon(Utils.bitmapDescriptorFromVector(this, R.drawable.ic_my_location))
                .draggable(false)
                .title("Its me")
            googleMap?.addMarker(markerOptions)
            animateCameraToPosition(
                LatLng(myLatitude!!.toDouble(), myLongitude!!.toDouble()),
                googleMap
            )
        }
    }

    private fun addArea() {
        area?.let {
            when (it.type) {
                "circle" -> {
                    it.circle?.let { circle ->
                        binding.circleRadiusLayout.visibility = View.VISIBLE
                        binding.seekbar.progress = circle.radius.toInt()
                        if (markerList.isEmpty()) {
                            val marker = Utils.addMarker(circle.center, googleMap)
                            marker?.let { marker -> markerList.add(marker) }
                        }
                        this.circle?.remove()
                        this.circle = addCircle(circle.center, circle.radius, googleMap)
                    }
                }
                "polyline" -> {
                    it.polyline?.let { polyline ->
                        polyline.points.forEach { latlon ->
                            val marker = Utils.addMarker(latlon, googleMap)
                            marker?.let { marker -> markerList.add(marker) }
                        }
                        addOrUpdatePolyLine()
                    }
                }
                "polygon" -> {
                    it.polygon?.let { polygon ->
                        polygon.points.forEach { latlon ->
                            val marker = Utils.addMarker(latlon, googleMap)
                            marker?.let { marker -> markerList.add(marker) }
                        }
                        addOrUpdatePolygon()
                    }
                }
                else -> {
                    Log.e("error", "type error")
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_btn -> {
                finish()
            }
            R.id.done_btn -> {
                when (mapEditMode) {
                    "circle" -> {
                        circle?.let { updatedCircle ->
                            if (editPos != -1) {
                                dataList[editPos].circle = updatedCircle
                            } else {
                                val area = Area(Date().time.toString(), "circle")
                                area.circle = updatedCircle
                                dataList.add(area)
                            }

                        }
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    "polyline" -> {
                        polyline?.let {
                            if (editPos != -1) {
                                dataList[editPos].polyline = it
                            } else {
                                val area = Area(Date().time.toString(), "polyline")
                                area.polyline = it
                                dataList.add(area)
                            }
                        }
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    "polygon" -> {
                        polygon?.let {
                            if (editPos != -1) {
                                dataList[editPos].polygon = it
                            } else {
                                val area = Area(Date().time.toString(), "polygon")
                                area.polygon = it
                                dataList.add(area)
                            }
                        }
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        }
    }

    override fun onMapClick(latLng: LatLng) {
        mapWork(latLng)
    }

    private fun mapWork(latLng: LatLng) {
        when (mapEditMode) {
            "circle" -> {
                binding.circleRadiusLayout.visibility = View.VISIBLE
                if (markerList.isEmpty()) {
                    val marker = Utils.addMarker(latLng, googleMap)
                    marker?.let { markerList.add(it) }
                    addOrUpdateCircle()
                }
            }
            "polyline" -> {
                val marker = Utils.addMarker(latLng, googleMap)
                marker?.let { markerList.add(it) }
                addOrUpdatePolyLine()
            }
            "polygon" -> {
                val marker = Utils.addMarker(latLng, googleMap)
                marker?.let { markerList.add(it) }
                addOrUpdatePolygon()
            }
        }
    }

    private fun addOrUpdateCircle(progress: Double = 100.0) {
        circle?.remove()
        if (markerList.isNotEmpty())
            circle = addCircle(markerList[0].position, progress, googleMap)
    }

    private fun addOrUpdatePolyLine() {
        if (markerList.size > 1) {
            val list: ArrayList<LatLng> = ArrayList()
            markerList.forEach { list.add(it.position) }
            polyline?.remove()
            polyline = Utils.addPolyLine(list, googleMap)
        }
    }

    private fun addOrUpdatePolygon() {
        if (markerList.size > 2) {
            MarkerSorter.sort(markerList)
            val list: ArrayList<LatLng> = ArrayList()
            markerList.forEach { list.add(it.position) }
            polygon?.remove()
            polygon = Utils.addPolygon(list, googleMap)
        }
    }

    override fun onMarkerDragEnd(p0: Marker) {
    }

    override fun onMarkerDragStart(p0: Marker) {
    }

    override fun onMarkerDrag(p0: Marker) {
        when (mapEditMode) {
            "circle" -> {
                addOrUpdateCircle(progress)
            }
            "polyline" -> {
                addOrUpdatePolyLine()
            }
            "polygon" -> {
                addOrUpdatePolygon()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        this.progress = progress.toDouble()
        addOrUpdateCircle(this.progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}