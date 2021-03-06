package io.door2door.mobile_code_challenge.network

import io.door2door.mobile_code_challenge.data.Event
import io.reactivex.Observable

interface BookingsWebSocket {

  fun connectToWebSocket()

  fun getStatusUpdates(): Observable<Event>

  fun getVehicleLocationUpdates(): Observable<Event>

  fun getIntermediateStopUpdates() : Observable<Event>
}