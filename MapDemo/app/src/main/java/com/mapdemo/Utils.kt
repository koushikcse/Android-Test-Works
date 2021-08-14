package com.mapdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

object Utils {
    var dataList = ArrayList<Area>()

    fun addMarker(latlon: LatLng, googleMap: GoogleMap?): Marker? {
        val markerOptions = MarkerOptions()
            .position(latlon)
            .draggable(true)
            .title("marker")
        return googleMap?.addMarker(markerOptions)
    }

    fun addCircle(latlon: LatLng, radius: Double, googleMap: GoogleMap?): Circle? {
        val circleOptions = CircleOptions()
            .center(latlon)
            .fillColor(Color.parseColor("#4dff0000"))
            .strokeColor(Color.parseColor("#FF0000"))
            .radius(radius)
        return googleMap?.addCircle(circleOptions)
    }

    fun addPolyLine(latlons: ArrayList<LatLng>, googleMap: GoogleMap?): Polyline? {
        val polylineOptions = PolylineOptions()
            .color(Color.parseColor("#FF0000"))
            .addAll(latlons)
            .width(20f)
            .visible(true)

        return googleMap?.addPolyline(polylineOptions)
    }

    fun addPolygon(latlons: ArrayList<LatLng>, googleMap: GoogleMap?): Polygon? {
        val polygonOptions = PolygonOptions()
            .fillColor(Color.parseColor("#4dff0000"))
            .strokeColor(Color.parseColor("#FF0000"))
            .addAll(latlons)
            .clickable(true)
            .visible(true)

        return googleMap?.addPolygon(polygonOptions)
    }

    fun animateCameraToPosition(latlon: LatLng, googleMap: GoogleMap?) {
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latlon, 13f
            )
        )

        val cameraPosition: CameraPosition = CameraPosition
            .Builder()
            .target(latlon) // Sets the center of the map to location user
            .zoom(17f) // Sets the zoom
            .bearing(90f) // Sets the orientation of the camera to east
            .tilt(40f) // Sets the tilt of the camera to 30 degrees
            .build() // Creates a CameraPosition from the builder

        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    fun addAllShapes(googleMap: GoogleMap?) {
        googleMap?.clear()
        dataList.forEach {
            when(it.type){
                "circle"->{
                    it.circle?.let {it1->
                        addCircle(it1.center, it1.radius, googleMap)
                    }
                }
                "polyline"->{
                    it.polyline?.let{it2->
                        addPolyLine(it2.points as ArrayList<LatLng>,googleMap)
                    }
                }
                "polygon"->{
                    it.polygon?.let{it3->
                        addPolygon(it3.points as ArrayList<LatLng>,googleMap)
                    }
                }
            }
        }
    }
}