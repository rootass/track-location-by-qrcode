package com.wildlabs.tracklocation;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble("lat");
        longitude = extras.getDouble("long");

        SupportMapFragment fm = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        fm.getMapAsync(MapActivity.this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        callMap(googleMap);
    }

    public void callMap(GoogleMap googleMap){
        // Instantiating MarkerOptions class
        MarkerOptions options = new MarkerOptions();

        LatLng position = new LatLng(latitude,longitude);
        System.out.println("LAt = " + latitude + " long = " + longitude);
        // Setting position for the MarkerOptions
        options.position(position);

        // Setting title for the MarkerOptions
        options.title("Location");

        // Setting snippet for the MarkerOptions
        // Adding Marker on the Google Map
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            options.snippet(cityName + "," + stateName);

        } catch (Exception ex) {
            ex.getMessage();
            options.snippet("Latitude:" + latitude + ",Longitude:" + longitude);

        }
        googleMap.addMarker(options);

        // Creating CameraUpdate object for position
//        CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(position);

        // Creating CameraUpdate object for zoom
//        CameraUpdate updateZoom = CameraUpdateFactory.zoomTo(6);

        // Updating the camera position to the user input latitude and longitude
//        googleMap.moveCamera(updatePosition);

        // Applying zoom to the marker position
//        googleMap.animateCamera(updateZoom);
    }

}
