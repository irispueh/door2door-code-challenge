package io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.presenter

import android.util.Log
import io.door2door.mobile_code_challenge.mainScreen.features.VISIBILITY_DELAY
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.IntermediateStopLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.StatusLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.VehicleLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.BOOKING_CLOSED
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.BOOKING_OPENED
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.view.MapView
import io.door2door.mobile_code_challenge.mainScreen.interactor.MainScreenInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class MapPresenterImp @Inject constructor(
    private val mapView: MapView,
    private val mainScreenInteractor: MainScreenInteractor,
    private val vehicleLocationMapper: VehicleLocationMapper,
    private val statusLocationMapper: StatusLocationMapper,
    private val intermediateStopLocationMapper: IntermediateStopLocationMapper
) : MapPresenter {

    private val disposables = CompositeDisposable()
    private val tag = MapPresenterImp::class.simpleName

    override fun viewAttached() {
        mapView.obtainGoogleMap()
    }

    override fun viewDetached() {
        disposables.dispose()
    }

    override fun mapLoaded() {
        subscribeToVehicleLocationUpdates()
        subscribeToIntermediateStopsUpdates()
        subscribeToBookingStatusUpdates()
    }

    private fun subscribeToVehicleLocationUpdates() {
        disposables.add(
            mainScreenInteractor.getVehicleLocationUpdates(vehicleLocationMapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mapView.updateVehicleLocation(it.latLng)
                }, {
                    Log.d(tag, "Error on getting vehicle location updates")
                })
        )
    }

    private fun subscribeToIntermediateStopsUpdates() {
        disposables.add(
            mainScreenInteractor.getIntermediateStopUpdates(intermediateStopLocationMapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mapView.updateIntermediateStops(it.intermediateStops)
                }, {
                    Log.d(tag, "Error on getting intermediate stop updates")
                })
        )
    }

    private fun subscribeToBookingStatusUpdates() {
        disposables.add(
            mainScreenInteractor.getBookingStatusUpdates(statusLocationMapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == BOOKING_OPENED) {
                        if (it.pickUpLocation != null && it.dropOffLocation != null) {
                            mapView.showPickUpAndDropOffOnMap(it.pickUpLocation, it.dropOffLocation)
                            it.intermediateStops?.let { stops -> mapView.updateIntermediateStops(stops) }
                        }
                    } else if (it.status == BOOKING_CLOSED) {
                        Timer().schedule(VISIBILITY_DELAY){
                            mapView.clearMap()
                        }
                    }
                }, {
                    Log.d(tag, "Error on getting booking status updates")
                })
        )
    }
}