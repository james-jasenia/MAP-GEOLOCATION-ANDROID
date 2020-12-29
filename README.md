# Map Geolocation Prototype - Android
##### Tutorial: The Complete Android Oreo Developer Course - Section 6

## Purpose: 
The purpose of this app is to explore the Google Maps API and practise working with the Location Manager and Location Listener objects.


## Key Classes:
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

## Considerations:
### API Key
You need to an API Key from Google. You are able to set restrictions on this key so that the same key can be used in a variety of app across a variety of platforms or it can only be used for an individual app. The API Key is stored in the google_maps_api.xml file. 
```
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">ENTER_STRING</string>
```

### Permissions
You need to ask the user for permissions to access the devices location services. Like all permissions, you can declare your intent to access the location services on the device. 

```
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission>
```

In order to request permission for location services, you can use the .requestPermission() method. 

```
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
```

You use the onRequestPermissionsResult() to handle the outcome of this request. In the example below, it might have been cleaner to use the requestCode in the if statement. Keep in mind that this function is only called after the user as interacted with the permissions request (maybe once in the lifetime of the app).

```
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            //Alert the user that the app cannot function as expected due to denial of user permissions
            Log.i("User Permissions", "User Permissions denied");
        }
    }
```
