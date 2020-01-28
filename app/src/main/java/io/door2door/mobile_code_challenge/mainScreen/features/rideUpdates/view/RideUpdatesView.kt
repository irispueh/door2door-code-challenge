package io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.view

import com.google.android.gms.maps.model.LatLng

interface RideUpdatesView {

    fun updateRideStatus(status: String)

    fun showRideAddresses(pickUpAddress: String, dropOffAddress: String)

    fun hideRideInformation()

    fun updateBearingNavigation(vehicleLocation: LatLng)
}