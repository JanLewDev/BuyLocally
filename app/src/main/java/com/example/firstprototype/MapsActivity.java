package com.example.firstprototype;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnInfoWindowClickListener{

    private List<String> chosenTypes = new ArrayList<>();
    private List<Marker> markersList = new ArrayList<>();
    double currentLatitude = 0, currentLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Discover new producers");
        }

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

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                LatLng startingPosition = new LatLng(currentLatitude, currentLongitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPosition, 12));
            }
        });

        // 53.84446058176871, -2.4342807456783273

        chosenTypes.add("Bakery");
        chosenTypes.add("Butcher");
        chosenTypes.add("Diary");
        chosenTypes.add("Grocery");

        DBHelper databaseHelper = new DBHelper(MapsActivity.this);
        List<ProducerModel> allProducers = databaseHelper.getAllProducers();
        for(ProducerModel producer : allProducers){
            String location = producer.getLocation();
            String[] parts = location.split(",");
            double latitude = Double.valueOf(parts[0]);
            double longitude = Double.valueOf(parts[1]);
            LatLng where = new LatLng(latitude, longitude);

            float colour = 0;
            if (producer.getType().equals("Butcher")) {
                colour = BitmapDescriptorFactory.HUE_RED;
            } else if (producer.getType().equals("Grocery")) {
                colour = BitmapDescriptorFactory.HUE_GREEN;
            } else if (producer.getType().equals("Bakery")) {
                colour = BitmapDescriptorFactory.HUE_YELLOW;
            } else {
                // it has to diary
                colour = BitmapDescriptorFactory.HUE_CYAN;
            }

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(where)
                    .title(producer.getCompanyName())
                    .snippet(producer.getFirstName() + " " + producer.getSurname())
                    .icon(BitmapDescriptorFactory.defaultMarker(colour)));

            markersList.add(marker);
        }

        googleMap.setOnInfoWindowClickListener(MapsActivity.this);
    }

    // to use the correct layout file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        boolean wasChecked = false;
        // checking and unchecking the options as needed
        if(item.isChecked()){
            wasChecked = true;
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
        String toChange = "";

        if(id == R.id.menuBakery) {
            toChange = "Bakery";
        } else if(id == R.id.menuButcher) {
            toChange = "Butcher";
        } else if(id == R.id.menuDiary) {
            toChange = "Diary";
        } else if(id == R.id.menuGrocery){
            toChange = "Grocery";
        }

        if(wasChecked){
            chosenTypes.remove(toChange);
        } else {
            chosenTypes.add(toChange);
        }
        // amending the list of producers shown
        DBHelper databaseHelper = new DBHelper(MapsActivity.this);
        for(Marker marker : markersList){
            marker.setVisible(false);
            String name = marker.getTitle();
            ProducerModel producer = databaseHelper.getProducerByNameOrID(name, -1).get(0);
            if(chosenTypes.contains(producer.getType())){
                marker.setVisible(true);
            }
        }

        return true;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(MapsActivity.this, "Moving to your current location!", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(MapsActivity.this,"Your current location:\n" + location, Toast.LENGTH_SHORT).show();
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
        // Toast.makeText(MapsActivity.this, "Info window clicked!", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("companyName", marker.getTitle());
        editor.apply();

        Intent i = new Intent(MapsActivity.this, ProducerProfile.class);
        startActivity(i);
    }
}