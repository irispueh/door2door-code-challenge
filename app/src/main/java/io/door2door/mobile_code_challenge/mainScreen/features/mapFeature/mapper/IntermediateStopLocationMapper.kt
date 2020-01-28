package io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper

import io.door2door.mobile_code_challenge.data.BaseBookingMapper
import io.door2door.mobile_code_challenge.data.Event
import io.door2door.mobile_code_challenge.data.IntermediateStopLocationsChanged
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.IntermediateStopLocationModel
import javax.inject.Inject

class IntermediateStopLocationMapper @Inject constructor() :
    BaseBookingMapper<IntermediateStopLocationModel> {
    override fun mapDataModelToViewModel(dataModel: Event): IntermediateStopLocationModel {
        val eventData = dataModel as IntermediateStopLocationsChanged
        return IntermediateStopLocationModel(eventData.data)
    }

}