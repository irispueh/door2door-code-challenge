package io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.view

import android.content.Context
import android.location.Location
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import io.door2door.mobile_code_challenge.R
import io.door2door.mobile_code_challenge.convertToLocation
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.dagger.DaggerRideUpdatesComponent
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.dagger.RideUpdatesModule
import io.door2door.mobile_code_challenge.mainScreen.features.rideUpdates.presenter.RideUpdatesPresenter
import io.door2door.mobile_code_challenge.mainScreen.view.MainScreenActivity
import javax.inject.Inject

class RideUpdatesLayout : RelativeLayout, RideUpdatesView {

    @Inject
    lateinit var rideUpdatesPresenter: RideUpdatesPresenter

    private var statusTextView: TextView? = null
    private var pickUpAddressTextView: TextView? = null
    private var dropOffAddressTextView: TextView? = null
    private var bearingImageView: ImageView? = null

    private var previousVehicleLocation: Location? = null

    constructor(context: Context) : super(context) {
        setUp(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setUp(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setUp(context)
    }

    private fun setUp(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.feature_ride_updates, this, true)
        injectDependencies()
    }

    private fun injectDependencies() {
        DaggerRideUpdatesComponent.builder()
            .mainScreenComponent((context as MainScreenActivity).mainScreenComponent)
            .rideUpdatesModule(RideUpdatesModule(this))
            .build()
            .injectRideUpdatesView(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        statusTextView = findViewById(R.id.statusTextView)
        pickUpAddressTextView = findViewById(R.id.pickUpAddressTextView)
        dropOffAddressTextView = findViewById(R.id.dropOffAddressTextView)
        bearingImageView = findViewById(R.id.bearingImageView)

        rideUpdatesPresenter.viewAttached()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rideUpdatesPresenter.viewDetached()
    }

    override fun updateRideStatus(status: String) {
        val statusText = resources.getString(R.string.status, status)
        statusTextView?.text = statusText
        statusTextView?.visibility = View.VISIBLE
    }

    override fun showRideAddresses(pickUpAddress: String, dropOffAddress: String) {
        val pickUpText = resources.getString(R.string.pickup_at, pickUpAddress)
        pickUpAddressTextView?.text = pickUpText
        pickUpAddressTextView?.visibility = View.VISIBLE

        val dropOffText = resources.getString(R.string.dropoff_at, dropOffAddress)
        dropOffAddressTextView?.text = dropOffText
        dropOffAddressTextView?.visibility = View.VISIBLE
    }

    override fun hideRideInformation() {
        statusTextView?.visibility = View.INVISIBLE
        pickUpAddressTextView?.visibility = View.INVISIBLE
        dropOffAddressTextView?.visibility = View.INVISIBLE
    }

    override fun updateBearingNavigation(vehicleLocation: LatLng) {
        val newLocation = vehicleLocation.convertToLocation()
        if (previousVehicleLocation != null) {
            val bearing = previousVehicleLocation?.bearingTo(newLocation) ?: 0f
            bearingImageView?.apply {
                animate().rotation(bearing).start()
            }
        }
        previousVehicleLocation = newLocation
    }
}