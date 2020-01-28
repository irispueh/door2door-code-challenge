# Mobile Code Challenge Android Solution

### Installation

1. Open Project in Android Studio
2. Add Google API Key to `google_maps_api.xml`
3. Run `app` configuration on device or emulator


### Tested devices

* Samsung Galaxy S7

### Implementation

The design pattern used in this project is the MVP (Model-View-Presenter). The data flow is based around three components.

* _Model_: are the entities and business rules
* _View_: is the UI component
* _Presenter_: is the middleware between model and view

View and Presenter conform to their corresponding interface and communicate with each other via it. 

The concrete implementation in the application is:

The `BookingWebSocket` connects to the WebSocket provided by Door2Door and receives `Events`, which are parsed with the third-party library `Moshi`. The `BookingWebSocket` differentiates between the events and creates Observables for each of them. The `MainScreenInteractor` is the centerpiece of all presenters. Each feature (RideUpdates, MapFeature) implements a presenter which receives the `Observable` from the `MainScreenInteractor`, who is connected to the `BookingWebSocket`. The presenter has the corresponding View Interface injected and can call methods to present data on the UI.

While implementing the challenge I tried to do my best to follow the MVP pattern and extend the given structure.

##### MapFeature

Main functions of the `MapPresenter`:

`subscribeToVehicleLocationUpdates()` &rightarrow; In `.subscribe(...)` the `updateVehicleLocation` method of the MapView is executed. In the implementation of the `MapView` the vehicle marker then is animated and moves to the new location.

`subscribeToIntermediateStopsUpdates()` &rightarrow; In the `subscribe(...)` the `updateIntermediateStops` method of the MapView is executed. In the implementation of the `MapView` the old intermediate stops are removed, and the new ones are displayed.

`subscribeToBookingStatusUpdates()` &rightarrow; This method is needed to either mark your own pick up and drop off address, or clear the map. Both options are handled by the corresponding methods in the MapView implementation. The decision is based on the status of the booking. 

##### RideUpdates

Main functions of the `RideUpdatesPresenter`:

`subscribeToBookingStatusUpdates()` &rightarrow; This method is needed to either display your own pick up and drop off address, or clear all UI components. Both options are handled by the corresponding methods in the RideUpdatesView implementation. The decision is based on the status of the booking. 

`subscribeToVehicleLocationUpdates()` &rightarrow;  In `.subscribe(...)` the `updateBearingNavigation` method of the RideUpdatesView is executed. In the implementation of the `RideUpdatesView` the current vehicle rotation is calculated and is indicated by an arrow image.

### Missing

* Rotating the map causes the bearing navigation to be incorrect
* UI Tests
* Offline Warning



