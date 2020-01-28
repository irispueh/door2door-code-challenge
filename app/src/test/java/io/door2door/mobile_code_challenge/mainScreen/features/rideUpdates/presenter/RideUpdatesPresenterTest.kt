package io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.presenter

import android.content.res.Resources
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.IntermediateStopLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.StatusLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.VehicleLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.presenter.MapPresenter
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.view.MapView
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.mapper.BookingStatusMapper
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.view.RideUpdatesView
import io.door2door.mobile_code_challenge.mainScreen.interactor.MainScreenInteractor
import io.reactivex.Observable
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class RideUpdatesPresenterTest {

    private val rideUpdatesView = mock<RideUpdatesView>()
    private val mainScreenInteractor = mock<MainScreenInteractor>()
    private val resources = mock<Resources>()

    private lateinit var presenter: RideUpdatesPresenter

    @Before
    fun setUp() {
        presenter = RideUpdatesPresenterImp(rideUpdatesView, mainScreenInteractor, BookingStatusMapper(resources))
    }

    @Test
    fun shouldSubscribeToUpdates() {
        Mockito.`when`(mainScreenInteractor.getBookingStatusUpdates(any<BookingStatusMapper>()))
            .thenReturn(Observable.empty())

        presenter.viewAttached()
        verify(mainScreenInteractor).getBookingStatusUpdates(any<BookingStatusMapper>())
    }
}