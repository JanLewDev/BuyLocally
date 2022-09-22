package com.example.firstprototype;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnInfoWindowClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // check if the app has required permissions
        if(!canAccessLocation()){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
        }

        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // enable location
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(MapsActivity.this);
        googleMap.setOnMyLocationClickListener(MapsActivity.this);

        // just a test marker
        LatLng test = new LatLng(53.847995, -2.438157);
        googleMap.addMarker(new MarkerOptions()
                .position(test)
                .title("Test marker")
                .snippet("This is a test")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(test));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        googleMap.setOnInfoWindowClickListener(MapsActivity.this);

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(MapsActivity.this, "Moving to your current location!", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(MapsActivity.this,"Current location:\n" + location, Toast.LENGTH_SHORT).show();
    }

    // helper functions
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }
    
    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Toast.makeText(MapsActivity.this, "Info window clicked!", Toast.LENGTH_SHORT).show();
    }
}