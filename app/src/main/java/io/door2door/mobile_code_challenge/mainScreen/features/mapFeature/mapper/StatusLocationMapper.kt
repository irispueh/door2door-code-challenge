package io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper

import com.google.android.gms.maps.model.LatLng
import io.door2door.mobile_code_challenge.data.BaseBookingMapper
import io.door2door.mobile_code_challenge.data.BookingClosed
import io.door2door.mobile_code_challenge.data.BookingOpened
import io.door2door.mobile_code_challenge.data.Event
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.BOOKING_CLOSED
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.BOOKING_OPENED
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.CLEAR
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.StatusLocationModel
import javax.inject.Inject

class StatusLocationMapper @Inject constructor() : BaseBookingMapper<StatusLocationModel> {
    override fun mapDataModelToViewModel(dataModel: Event): StatusLocationModel {
        return when (dataModel) {
            is BookingOpened -> StatusLocationModel(
                BOOKING_OPENED,
                LatLng(dataModel.data.vehicleLocation.lat, dataModel.data.vehicleLocation.lng),
                dataModel.data.pickupLocation,
                dataModel.data.dropoffLocation,
                dataModel.data.intermediateStopLocations
            )
            is BookingClosed -> StatusLocationModel(BOOKING_CLOSED)
            else -> StatusLocationModel(CLEAR)
        }
    }
}