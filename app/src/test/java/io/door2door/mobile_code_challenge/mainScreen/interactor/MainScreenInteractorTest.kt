package io.door2door.mobile_code_challenge.mainScreen.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.IntermediateStopLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.StatusLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.VehicleLocationMapper
import io.door2door.mobile_code_challenge.network.BookingsWebSocket
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MainScreenInteractorTest {

    private val bookingsWebSocket = mock<BookingsWebSocket>()

    private lateinit var interactor: MainScreenInteractor

    @Before
    fun setUp() {
        interactor = MainScreenInteractorImp(bookingsWebSocket)
    }

    @Test
    fun shouldGetVehicleLocationUpdates() {
        Mockito.`when`(bookingsWebSocket.getVehicleLocationUpdates()).thenReturn(Observable.empty())

        interactor.getVehicleLocationUpdates(VehicleLocationMapper())
        verify(bookingsWebSocket).getVehicleLocationUpdates()
    }

    @Test
    fun shouldGetBookingStatusUpdates() {
        Mockito.`when`(bookingsWebSocket.getStatusUpdates()).thenReturn(Observable.empty())

        interactor.getBookingStatusUpdates(StatusLocationMapper())
        verify(bookingsWebSocket).getStatusUpdates()
    }

    @Test
    fun shouldGetIntermediateStopUpdates() {
        Mockito.`when`(bookingsWebSocket.getIntermediateStopUpdates()).thenReturn(Observable.empty())

        interactor.getIntermediateStopUpdates(IntermediateStopLocationMapper())
        verify(bookingsWebSocket).getIntermediateStopUpdates()
    }

    @Test
    fun shouldConnectToWebSocket() {
        interactor.connectToWebSocket()
        verify(bookingsWebSocket).connectToWebSocket()
    }
}