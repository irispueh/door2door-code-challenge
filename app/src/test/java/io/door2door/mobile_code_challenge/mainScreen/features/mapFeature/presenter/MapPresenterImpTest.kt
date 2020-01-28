package io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.presenter

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.door2door.mobile_code_challenge.mainScreen.TestUtils
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.IntermediateStopLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.StatusLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.mapper.VehicleLocationMapper
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.view.MapView
import io.door2door.mobile_code_challenge.mainScreen.interactor.MainScreenInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class MapPresenterImpTest {

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
    }

    @Test
    fun shouldObtainGoogleMap() {
        presenter.viewAttached()
        verify(mapView).obtainGoogleMap()
    }

    @Test
    fun shouldSubscribeToUpdates() {
        Mockito.`when`(mainScreenInteractor.getVehicleLocationUpdates(any<VehicleLocationMapper>()))
            .thenReturn(Observable.empty())
        Mockito.`when`(mainScreenInteractor.getBookingStatusUpdates(any<StatusLocationMapper>()))
            .thenReturn(Observable.empty())
        Mockito.`when`(mainScreenInteractor.getIntermediateStopUpdates(any<IntermediateStopLocationMapper>()))
            .thenReturn(Observable.empty())

        presenter.mapLoaded()
        verify(mainScreenInteractor).getVehicleLocationUpdates(any<VehicleLocationMapper>())
        verify(mainScreenInteractor).getBookingStatusUpdates(any<StatusLocationMapper>())
        verify(mainScreenInteractor).getIntermediateStopUpdates(any<IntermediateStopLocationMapper>())
    }
}