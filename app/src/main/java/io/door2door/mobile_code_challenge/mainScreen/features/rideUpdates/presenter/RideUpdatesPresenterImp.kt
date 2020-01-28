package io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.presenter

import android.util.Log
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.VehicleLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.mapper.BookingStatusMapper
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.model.BookingStatusModel
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.view.RideUpdatesView
import io.door2door.mobile_code_challenge.mainScreen.interactor.MainScreenInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class RideUpdatesPresenterImp @Inject constructor(
    private val rideUpdatesView: RideUpdatesView,
    private val mainScreenInteractor: MainScreenInteractor,
    private val bookingStatusMapper: BookingStatusMapper,
    private val vehicleLocationMapper: VehicleLocationMapper
) : RideUpdatesPresenter {

    private val disposables = CompositeDisposable()
    private val tag = RideUpdatesPresenterImp::class.simpleName

    companion object {
        const val DELAY: Long = 10000
    }

    override fun viewAttached() {
        subscribeToBookingStatusUpdates()
        subscribeToVehicleLocationUpdates()
    }

    override fun viewDetached() {
        disposables.dispose()
    }

    private fun subscribeToBookingStatusUpdates() {
        disposables.add(
            mainScreenInteractor.getBookingStatusUpdates(bookingStatusMapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //todo
                    handleStatusUpdate(it)
                }, {
                    Log.d(tag, "Error on getting status updates")
                })
        )
    }

    private fun subscribeToVehicleLocationUpdates() {
        disposables.add(
            mainScreenInteractor.getVehicleLocationUpdates(vehicleLocationMapper)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    rideUpdatesView.updateBearingNavigation(it.latLng)
                }, {
                    Log.d(tag, "Error on getting vehicle location updates")
                })
        )
    }

    private fun handleStatusUpdate(bookingStatus: BookingStatusModel) {
        rideUpdatesView.updateRideStatus(bookingStatus.status)
        if (bookingStatus.dropoffAddress != null && bookingStatus.pickupAddress != null) {
            rideUpdatesView.showRideAddresses(
                bookingStatus.pickupAddress,
                bookingStatus.dropoffAddress
            )
        } else if (bookingStatus.isBookingClosed) {
            Timer().schedule(DELAY){
                rideUpdatesView.hideRideInformation()
            }
        }
    }
}