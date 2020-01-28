package io.door2door.mobile_code_challenge

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import io.door2door.mobile_code_challenge.data.events.Location as LocationModel

fun LatLng?.convertToLocation(): Location? = this?.let {
    val location = Location("")
    location.latitude = latitude
    location.longitude = longitude
    return location
}

fun LocationModel.convertToLatLng() = LatLng(this.lat, this.lng)