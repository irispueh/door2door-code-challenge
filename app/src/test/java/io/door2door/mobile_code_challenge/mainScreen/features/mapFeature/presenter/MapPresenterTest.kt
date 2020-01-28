package io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.presenter

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.door2door.mobile_code_challenge.data.events.Location
import io.door2door.mobile_code_challenge.mainScreen.TestUtils
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.IntermediateStopLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.StatusLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.VehicleLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.model.*
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.view.MapView
import io.door2door.mobile_code_challenge.mainScreen.interactor.MainScreenInteractor
import io.reactivex.Observable
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito

class MapPresenterTest {

    private val mapView = mock<MapView>()
    private val mainScreenInteractor = mock<MainScreenInteractor>()

    private lateinit var presenter: MapPresenter

    @Before
    fun setUp() {
        presenter = MapPresenterImp(
            mapView,
            mainScreenInteractor,
            VehicleLocationMapper(),
            StatusLocationMapper(),
            IntermediateStopLocationMapper()
        )

        TestUtils.setupRxJavaScheduler()

        setDefaultMockReturnValues()
    }

    private fun setDefaultMockReturnValues() {
        Mockito.`when`(mainScreenInteractor.getVehicleLocationUpdates(any<VehicleLocationMapper>()))
            .thenReturn(Observable.empty())
        Mockito.`when`(mainScreenInteractor.getBookingStatusUpdates(any<StatusLocationMapper>()))
            .thenReturn(Observable.empty())
        Mockito.`when`(mainScreenInteractor.getIntermediateStopUpdates(any<IntermediateStopLocationMapper>()))
            .thenReturn(Observable.empty())
    }

    @Test
    fun shouldObtainGoogleMap() {
        presenter.viewAttached()
        verify(mapView).obtainGoogleMap()
    }

    @Test
    fun shouldSubscribeToUpdates() {
        presenter.mapLoaded()
        verify(mainScreenInteractor).getVehicleLocationUpdates(any<VehicleLocationMapper>())
        verify(mainScreenInteractor).getBookingStatusUpdates(any<StatusLocationMapper>())
        verify(mainScreenInteractor).getIntermediateStopUpdates(any<IntermediateStopLocationMapper>())
    }

    @Test
    fun shouldCallViewMethods() {
        Mockito.`when`(mainScreenInteractor.getVehicleLocationUpdates(any<VehicleLocationMapper>()))
            .thenReturn(
                Observable.just(
                    VehicleLocationModel(LatLng(52.521992025025575,13.416967391967772))
                )
            )
        Mockito.`when`(mainScreenInteractor.getBookingStatusUpdates(any<StatusLocationMapper>()))
            .thenReturn(
                Observable.just(StatusLocationModel(BOOKING_OPENED, LatLng(0.0,0.0),
                    Location(address = null, lat = 52.519061, lng = 13.426789),
                    Location(address = null, lat = 52.519061, lng = 15.426789))))
        Mockito.`when`(mainScreenInteractor.getIntermediateStopUpdates(any<IntermediateStopLocationMapper>()))
            .thenReturn(Observable.just(IntermediateStopLocationModel(emptyList())))


        presenter.mapLoaded()
        verify(mapView).updateVehicleLocation(any())
        verify(mapView).showPickUpAndDropOffOnMap(any(), any())
        verify(mapView).updateIntermediateStops(any())
    }

    @Test
    @Ignore("Fix timer delay")
    fun shouldCallClearMapInMapView() {
        Mockito.`when`(mainScreenInteractor.getBookingStatusUpdates(any<StatusLocationMapper>()))
            .thenReturn(
                Observable.just(StatusLocationModel(BOOKING_CLOSED)))

        presenter.mapLoaded()
        verify(mapView).clearMap()
    }
}