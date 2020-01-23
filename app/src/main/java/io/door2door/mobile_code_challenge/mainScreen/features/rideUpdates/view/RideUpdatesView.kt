package io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.view

interface RideUpdatesView {
    //todo

    fun updateRideStatus(status: String)

    fun showRideAddresses(pickUpAddress: String, dropOffAddress: String)
}