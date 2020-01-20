# Mobile Code Challenge Android Solution

This is a partial solution for the mobile code challenge, on the basis of which you should be able to solve the requirements of the code challenge.


We have built the basic architecture, which is close to the one used in our actual app.
Based on this framework we expect you to implement a solution that satisfies 
the requirements listed in the [mobile code challenge](https://github.com/door2door-io/d2d-code-challenges/tree/master/mobile). 
Feel free to add more things, but it is not required in order to have your code challenge accepted.

## Project setup

Import the project using Android studio. Go to `google_maps_api.xml` and follow the instructions to 
create a Google Maps API key and paste it there.

## Some tips on how to work with the existing code

Use the methods from `MainScreenInteractor` to get Observable streams of the events coming 
from the WebSocket endpoint (see [documentation](https://d2d-frontend-code-challenge.herokuapp.com/docs)) 
and display them in a way you see fit. There is an example on how to subscribe to an Observable stream 
in the `RideUpdatesPresenter` implementation. If you have not worked with RxJava before, don't be intimidated.
We don't expect you to understand everything and become an expert, but rather want to see how you can work with 
it based on a given example.

In the `MapLayout` make use of the existing private methods to handle marker animation.
We have provided a vehicle icon which you can use, or feel free to add your own image or use 
the basic marker without an icon.

Implement your own design for the `feature_ride_updates` layout. You can make use of the 
colours and styles we have provided or add your own. Make sure to display all the relevant 
information.

## Resources

* Kotlin documentation and tutorials - https://kotlinlang.org
* Rx documentation - http://reactivex.io/
* RxJava - https://github.com/ReactiveX/RxJava

