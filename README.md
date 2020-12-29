# Map Geolocation Prototype - Android
##### Tutorial: The Complete Android Oreo Developer Course - Section 6

### Purpose: 
The purpose of this app is to explore the Google Maps API and practise working with the Location Manager and Location Listener objects.

### LocationManager
The LocationManager allows you access the systems location services such as the GPS. This object will receieve preiodic updates on the devices geographical location. You can control the frequency in which you receieve location updates as well as the provider (GPS, Network, etc) using the .requestLocationUpdates() method.

```
locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
```

Note: Location Permission must granted in order for the LocationManager to receieve updates.

### LocationListener
The LocationListener is responsible for receiving notifcations from the LocationManager. The important method is onLocationChanged(Location location) which is called when the LocationManager notifies the LocationListener that the location has changed. As onLocationChanged(Location location) is an event, you should put any relevant UI update logic here.

```
     locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));
            }
     }
```

### GoogleMap and SupportMapFragment
The SupportMapFragment is the ViewController. The GoogleMap object is the view layer that can be customised to suit the purpose of the app at the given time. It is highly customisable.

### Geocoder
Google uses the Geocoder object to provide an address for any given latitude and longitude. You use the method .getFromLocation() to request an address (type). You need to wrap it in a try/catch block. The method will return a list of addresses.
```

            try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    //Check addressList is not null and use the data as needed

                } catch (Exception e) {
                    e.printStackTrace();
                }
```
