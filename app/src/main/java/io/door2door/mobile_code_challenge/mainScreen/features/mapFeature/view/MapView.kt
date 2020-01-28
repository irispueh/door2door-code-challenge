package io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.view

import com.google.android.gms.maps.model.LatLng
import io.door2door.mobile_code_challenge.data.Location

interface MapView {

  fun obtainGoogleMap()

  fun clearMap()

  fun updateVehicleLocation(location: LatLng)

  fun updateIntermediateStops(intermediateStops: List<Location>)

  fun showPickUpAndDropOffOnMap(pickUpAddress: Location, dropOffAddress: Location)
}