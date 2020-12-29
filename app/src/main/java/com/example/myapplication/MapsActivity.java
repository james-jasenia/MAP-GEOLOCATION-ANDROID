package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    //This method will only be called after the user has responded to the permissions request.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Check the results array isn't empty. Access the first element and check to see if PERMISSION_GRANTED. You could also check the request code (which might be safer if you stored your codes in an enum):
        // if (requestCode == *Int you assigned to permission request*)
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            //Alert the user that the app cannot function as expected due to denial of user permissions
            Log.i("User Permissions", "User Permissions denied");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setupLocationManager();
    }

    //Location Services
    public void setupLocationManager() {

        //Init Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //Init Location Listener
        locationListener = new LocationListener() {

            //This method will be called when the devices GPS location sends an update.
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //Init a new LatLng object using the current locations latitude and longitude.
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                //Add and format the marker using the currentLatLng object
                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You are here")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));

                //Google uses the GeoCoder object to provide an address for a given LatLng
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addressList != null && addressList.size() > 0) {
                        String address = "";

                        if (addressList.get(0).getAdminArea() != null) {
                            address += addressList.get(0).getAdminArea() + ", ";
                        }

                        if (addressList.get(0).getLocality() != null) {
                            address += addressList.get(0).getLocality() + ", ";
                        }

                        if (addressList.get(0).getThoroughfare() != null) {
                            address += addressList.get(0).getThoroughfare();
                        }

                        Log.i("Address", address);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //User Permissions
        //Check to see if the user has previously granted LOCATION Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //User has granted permission.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}