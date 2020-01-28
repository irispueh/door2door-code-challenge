package io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.view

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.location.Location
import android.util.AttributeSet
import android.util.Property
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import io.door2door.mobile_code_challenge.R
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.dagger.DaggerMapComponent
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.dagger.MapModule
import io.door2door.mobile_code_challenge.mainScreen.features.mapFeature.presenter.MapPresenter
import io.door2door.mobile_code_challenge.mainScreen.view.MainScreenActivity
import kotlinx.android.synthetic.main.feature_map.view.*
import javax.inject.Inject
import io.door2door.mobile_code_challenge.data.events.Location as LocationModel


private const val MARKER_ANIMATION_DURATION = 1000L
private const val VEHICLE_MARKER_ANCHOR = 0.5f
private const val MARKER_ELEVATION = 3f

class MapLayout : MapView, RelativeLayout {

    @Inject
    lateinit var mapPresenter: MapPresenter

    private var googleMap: GoogleMap? = null
    private var markerPositionAnimator: ObjectAnimator? = null
    private var markerRotationAnimator: ObjectAnimator? = null
    private var vehicleMarker: Marker? = null
    private var intermediateStopsMarker: List<Marker?> = emptyList()
    private var pickUpMarker: Marker? = null
    private var dropOffMarker: Marker? = null

    private var zoomLevel: Float = 15f

    constructor(context: Context) : super(context) {
        setUp(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setUp(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs,
        defStyleAttr
    ) {
        setUp(context)
    }

    private fun setUp(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.feature_map, this, true)
        injectDependencies()
        mapPresenter.viewAttached()
    }

    private fun injectDependencies() {
        DaggerMapComponent.builder()
            .mainScreenComponent((context as MainScreenActivity).mainScreenComponent)
            .mapModule(MapModule(this))
            .build()
            .injectMapView(this)
    }

    override fun obtainGoogleMap() {
        mapView.getMapAsync {
            googleMap = it
            setUpMap()
            mapPresenter.mapLoaded()
        }
    }

    override fun clearMap() {
        googleMap?.clear()
    }

    private fun animateMarker(marker: Marker?, finalLatLng: LatLng) {
        marker?.let {
            if (marker.position != null) {
                animateMarkerPosition(marker, finalLatLng)
                animateMarkerRotation(marker, finalLatLng)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(finalLatLng, zoomLevel))
            } else {
                marker.position = finalLatLng
            }
        }
    }

    private fun animateMarkerPosition(marker: Marker, finalLatLng: LatLng) {
        val interpolator = LinearInterpolator()
        val typeEvaluator = TypeEvaluator<LatLng> { fraction, fromLatLng, toLatLng ->
            val interpolation = interpolator.getInterpolation(fraction)
            val lat =
                (toLatLng.latitude - fromLatLng.latitude) * interpolation + fromLatLng.latitude
            val lng =
                (toLatLng.longitude - fromLatLng.longitude) * interpolation + fromLatLng.longitude
            return@TypeEvaluator LatLng(lat, lng)
        }
        val property = Property.of(Marker::class.java, LatLng::class.java, "position")
        markerPositionAnimator?.cancel()
        markerPositionAnimator =
            ObjectAnimator.ofObject(marker, property, typeEvaluator, finalLatLng)
        markerPositionAnimator?.duration = MARKER_ANIMATION_DURATION
        markerPositionAnimator?.start()
    }

    private fun animateMarkerRotation(marker: Marker, finalLatLng: LatLng) {
        val initialLocation = marker.position.convertToLocation()
        val finalLocation = finalLatLng.convertToLocation()
        val newRotation = initialLocation?.bearingTo(finalLocation)
        newRotation?.let {
            val rotationProperty = Property.of(Marker::class.java, Float::class.java, "rotation")
            markerRotationAnimator?.cancel()
            markerRotationAnimator = ObjectAnimator.ofFloat(marker, rotationProperty, it)
            markerRotationAnimator?.duration = MARKER_ANIMATION_DURATION
            markerRotationAnimator?.start()
        }
    }

    private fun LatLng?.convertToLocation(): Location? = this?.let {
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        return location
    }

    private fun LocationModel.convertToLatLng(): LatLng = LatLng(this.lat, this.lng)

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mapPresenter.viewDetached()
    }

    private fun setUpMap() {
        googleMap?.setOnCameraIdleListener {
            zoomLevel = googleMap?.cameraPosition?.zoom ?: 0f
        }
    }

    override fun updateVehicleLocation(location: LatLng) {
        if (vehicleMarker == null) {
            val vehicleIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_vehicle)
            vehicleMarker =
                googleMap?.addMarker(MarkerOptions().icon(vehicleIcon).position(location))
        }
        animateMarker(vehicleMarker, location)
    }

    override fun updateIntermediateStops(intermediateStops: List<LocationModel>) {
        intermediateStopsMarker.forEach {
            it?.remove()
        }

        val intermediateStopIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_intermediatestop)
        intermediateStopsMarker = intermediateStops.map {
            createMarker(icon = intermediateStopIcon, position = it.convertToLatLng())
        }
    }

    override fun showPickUpAndDropOffOnMap(
        pickUpAddress: LocationModel,
        dropOffAddress: LocationModel
    ) {
        if (pickUpMarker == null) {
            val pickUpIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_pickup)
            pickUpMarker = createMarker(pickUpAddress.address, pickUpIcon, pickUpAddress.convertToLatLng())
        }

        if (dropOffMarker == null) {
            val dropOffIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_dropoff)
            dropOffMarker = createMarker(dropOffAddress.address, dropOffIcon, dropOffAddress.convertToLatLng())
        }
    }

    private fun createMarker(title: String? = null, icon: BitmapDescriptor, position: LatLng): Marker? {
        return googleMap?.addMarker(MarkerOptions().icon(icon).position(position).title(title))
    }
}