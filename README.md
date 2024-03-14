# The HVEPYC OnHover Implementation for GoogleMap Android SDK Drawable Elements

This is the official repository for the OnHover Implementation created for GoogleMap Android SDK Drawable Elements.

This repository contains a sample implementation of one how could emulate a OnHover setup, where, for eg: a cursor at the center of the map could be used to hover over the elements.

The OnHover implementation works as follows:
1. A sample GoogleMap fragment is used, and a location pin stock image is placed at the center of the map to act as a cursor, where the hovering is done.
2. An object called the LatLngDistanceKeeper is created, which stores information regarding each marked point, such as Latitude, Longitude, associated text with element, and details of ImageView and TextView elements used with each region marked on the map.
3. An ArrayList of LatLngDistanceKeeper is initialized, used later in the application.
4. In order to show the effects of the Hover, this implementation shows a banner consisting of an ImageView and a TextView. The Banner shows information about that specific region when hovering over the region with the cursor.
5. The IDs of the ImageView and TextView of the banner are identified and stored for later use.
6. The GoogleMap fragment is initialized, along with a button using which the user can add things onto the map (This example will add Circle Drawable elements onto the Map).
7. The button is set in such a manner that when pressed, it notes the LatLng at which the center of the map is (where the cursor is), creates a new copy of an ImageView and TextView for the banner, and adds it to the ArrayList of LatLngDistanceKeeper elements. A circle is also drawn at the point where the cursor is hovering.
8. The Radius of the circle, and the hovering radius is initialized at the beginning as a Circle Radius final variable, which will be used for later work.
9. The GoogleMap SDK has a setOnCameraMoveListener, which is used here to detect when the map is moved (and subsequently, the location over which the cursor is pointing).
10. The listener is set in such a manner, where when movement occurs, it iterates through the LatLngDistanceKeeper ArrayList, and computes the distance between the location the cursor is pointing at, and the location of the previously marked locations on the map.
11. Each distance computed is stored in each LatLngDistanceKeeper object, and the value is used to determine whether the distance is less than the Circle Radius variable.
12. If the distance is detected to be less than that of the Circle Radius value, a piece of code which obtains the Banner ImageView and TextView IDs associated with the LatLngDistanceKeeper object is loaded, and accordingly shown as required.
13. The code also makes sure to make banners disappear when the cursor is outside the circle radius.

Hence, in such a manner, an OnHover Implementation has been created.

This Implementation can be extended to Drawable elements outside circles, where the only modifications needed would be the distance calculations between the border and the cursor.

This repository's sample implementation can be used as a reference for other work. 

Suggestions, Contributions and Improvements are welcome for this project :)

Thank You
Created by HVEPYC.