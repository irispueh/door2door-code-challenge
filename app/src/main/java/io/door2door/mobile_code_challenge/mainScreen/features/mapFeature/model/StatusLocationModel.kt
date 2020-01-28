package io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model

import com.google.android.gms.maps.model.LatLng
import io.door2door.mobile_code_challenge.data.events.Location

data class StatusLocationModel(
    val status: String,
    val vehiclePosition: LatLng? = null,
    val pickUpLocation: Location? = null,
    val dropOffLocation: Location? = null
)

const val BOOKING_OPENED = "opened"
const val BOOKING_CLOSED = "closed"
const val CLEAR = ""