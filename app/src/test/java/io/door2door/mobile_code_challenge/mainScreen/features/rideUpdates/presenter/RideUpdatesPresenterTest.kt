package io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.presenter

import android.content.res.Resources
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.VehicleLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.VehicleLocationModel
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.mapper.BookingStatusMapper
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.model.BookingStatusModel
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.view.RideUpdatesView
import io.door2door.mobile_code_challenge.mainScreen.interactor.MainScreenInteractor
import io.reactivex.Observable
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito

class RideUpdatesPresenterTest {

    private val rideUpdatesView = mock<RideUpdatesView>()
    private val mainScreenInteractor = mock<MainScreenInteractor>()
    private val resources = mock<Resources>()

    private lateinit var presenter: RideUpdatesPresenter

    @Before
    fun setUp() {
        presenter = RideUpdatesPresenterImp(
            rideUpdatesView,
            mainScreenInteractor,
            BookingStatusMapper(resources),
            VehicleLocationMapper()
        )

        setDefaultMockReturnValues()
    }

    private fun setDefaultMockReturnValues() {
        Mockito.`when`(mainScreenInteractor.getBookingStatusUpdates(any<BookingStatusMapper>()))
            .thenReturn(Observable.empty())
        Mockito.`when`(mainScreenInteractor.getVehicleLocationUpdates(any<VehicleLocationMapper>()))
            .thenReturn(Observable.empty())
    }

    @Test
    fun shouldSubscribeToUpdates() {
        presenter.viewAttached()
        verify(mainScreenInteractor).getBookingStatusUpdates(any<BookingStatusMapper>())
        verify(mainScreenInteractor).getVehicleLocationUpdates(any<VehicleLocationMapper>())
    }

    @Test
    fun shouldCallViewMethods() {
        Mockito.`when`(mainScreenInteractor.getVehicleLocationUpdates(any<VehicleLocationMapper>()))
            .thenReturn(
                Observable.just(
                    VehicleLocationModel(LatLng(52.521992025025575,13.416967391967772))
                )
            )
        Mockito.`when`(mainScreenInteractor.getBookingStatusUpdates(any<BookingStatusMapper>()))
            .thenReturn(
                Observable.just(BookingStatusModel("bookingOpened", "Hagenberg", "Berlin")))

        presenter.viewAttached()
        verify(rideUpdatesView).updateBearingNavigation(any())
        verify(rideUpdatesView).showRideAddresses(any(), any())
        verify(rideUpdatesView).updateRideStatus(any())
    }

    @Test
    @Ignore("Fix timer delay")
    fun shouldCallHideRideInformation() {
        Mockito.`when`(mainScreenInteractor.getBookingStatusUpdates(any<BookingStatusMapper>()))
            .thenReturn(
                Observable.just(BookingStatusModel("bookingClosed", isBookingClosed = true)))

        presenter.viewAttached()
        verify(rideUpdatesView).hideRideInformation()
    }
}