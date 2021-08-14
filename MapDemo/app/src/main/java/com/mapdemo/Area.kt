package com.mapdemo

import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.Polyline
import java.util.*

data class Area(
    val name: String,
    val type: String,
    val id: Long = Date().time
) {
    var polygon: Polygon? = null
    var polyline: Polyline? = null
    var circle: Circle? = null
}
