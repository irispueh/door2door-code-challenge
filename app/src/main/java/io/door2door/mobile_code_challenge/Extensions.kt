package io.door2door.mobile_code_challenge

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun LatLng?.convertToLocation(): Location? = this?.let {
    val location = Location("")
    location.latitude = latitude
    location.longitude = longitude
    return location
}