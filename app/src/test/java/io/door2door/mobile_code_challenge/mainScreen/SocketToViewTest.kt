package io.door2door.mobile_code_challenge.mainScreen

import android.content.res.Resources
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.door2door.mobile_code_challenge.data.events.*
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.IntermediateStopLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.StatusLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.VehicleLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.presenter.MapPresenter
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.presenter.MapPresenterImp
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.view.MapView
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.mapper.BookingStatusMapper
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.presenter.RideUpdatesPresenter
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.presenter.RideUpdatesPresenterImp
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.view.RideUpdatesView
import io.door2door.mobile_code_challenge.mainScreen.interactor.MainScreenInteractorImp
import io.door2door.mobile_code_challenge.network.BookingsWebSocket
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.internal.verification.Times

class SocketToViewTest {
    private val webSocket = mock<BookingsWebSocket>()
    private val mainScreenInteractor = MainScreenInteractorImp(webSocket)

    private val mapView = mock<MapView>()
    private val rideUpdatesView = mock<RideUpdatesView>()

    private val resources = mock<Resources>()

    private lateinit var mapPresenter: MapPresenter
    private lateinit var rideUpdatePresenter: RideUpdatesPresenter

    @Before
    fun setUp() {
        mapPresenter = MapPresenterImp(
            mapView,
            mainScreenInteractor,
            VehicleLocationMapper(),
            StatusLocationMapper(),
            IntermediateStopLocationMapper()
        )

        rideUpdatePresenter = RideUpdatesPresenterImp(
            rideUpdatesView,
            mainScreenInteractor,
            BookingStatusMapper(resources),
            VehicleLocationMapper()
        )

        TestUtils.setupRxJavaScheduler()
        whenever(resources.getString(any())).thenReturn("")

        setUpVehicleLocationUpdates()
        setUpBookingUpdates()
        setUpIntermediateStopUpdates()

        rideUpdatePresenter.viewAttached()
        mapPresenter.mapLoaded()
    }

    @Test
    fun shouldUpdateVehicleLocationInMapOnVehicleLocationUpdate() {
        verify(mapView, Times(3)).updateVehicleLocation(any<LatLng>())
    }

    @Test
    fun shouldUpdatePickUpAndDropOffAddress() {
        verify(mapView).showPickUpAndDropOffOnMap(any(), any())
    }

    @Test
    fun shouldUpdateIntermediateStopsInMapOnIntermediateStopUpdate() {
        verify(mapView, Times(2)).updateIntermediateStops(any())
    }

    @Test
    fun shouldUpdateBookingStatus() {
        verify(rideUpdatesView, Times(3)).updateRideStatus(any())
    }

    @Test
    fun shouldUpdateNavigationBearing() {
        verify(rideUpdatesView, Times(3)).updateBearingNavigation(any())
    }

    private fun setUpVehicleLocationUpdates() {
        val vehicleLocationUpdates = listOf(
            VehicleLocationUpdated(
                "vehicleLocationUpdated",
                Location(null, 52.520046627821515, 13.423404693603516)
            ),
            VehicleLocationUpdated(
                "vehicleLocationUpdated",
                Location(null, 52.52114337240909, 13.41982126235962)
            ),
            VehicleLocationUpdated(
                "vehicleLocationUpdated",
                Location(null, 52.521992025025575, 13.416967391967772)
            )
        )

        Mockito.`when`(webSocket.getVehicleLocationUpdates())
            .thenReturn(Observable.fromIterable(vehicleLocationUpdates))
    }

    private fun setUpIntermediateStopUpdates() {
        val locations1 = listOf(
            Location("Gendarmenmarkt", 52.513763, 13.393208),
            Location("The Sixties Diner", 52.523728, 13.399356)
        )
        val locations2 = listOf(
            Location("Friedrichstraße Station", 52.519485, 13.388238),
            Location("The Sixties Diner", 52.523728, 13.399356)
        )

        val intermediateStopUpdates = listOf(
            IntermediateStopLocationsChanged("intermediateStopLocationsChanged", locations1),
            IntermediateStopLocationsChanged("intermediateStopLocationsChanged", locations2)
        )

        Mockito.`when`(webSocket.getIntermediateStopUpdates())
            .thenReturn(Observable.fromIterable(intermediateStopUpdates))
    }

    private fun setUpBookingUpdates() {
        val bookingOpened = BookingOpened(
            "bookingOpened",
            BookingOpened.Data(
                status = "waitingForPickup",
                vehicleLocation = Location(address = null, lat = 52.519061, lng = 13.426789),
                pickupLocation = Location(
                    address = "Volksbühne Berlin",
                    lat = 52.52663,
                    lng = 13.411632
                ),
                dropoffLocation = Location(
                    address = "Gendarmenmarkt",
                    lat = 52.513763,
                    lng = 13.393208
                ),
                intermediateStopLocations = listOf(
                    Location(address = "The Sixties Diner", lat = 52.523728, lng = 13.399356),
                    Location(address = "Friedrichstraße Station", lat = 52.519485, lng = 13.388238)
                )
            )
        )

        val bookingClosed = BookingClosed("bookingClosed", null)

        val bookingStatusUpdated = StatusUpdated("statusUpdated", "inVehicle")

        val bookingUpdates = listOf(bookingOpened, bookingStatusUpdated, bookingClosed)

        Mockito.`when`(webSocket.getStatusUpdates())
            .thenReturn(Observable.fromIterable(bookingUpdates))
    }
}