package com.example.firstprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {

    Button goToMap;
    TextView accountEmail, fullName;
    RecyclerView favouritesList;
    RecyclerView.Adapter listAdapter;
    RecyclerView.LayoutManager layoutManager;
    Menu menu;
    List<ProducerModel> allFavourites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Home");
        }

        goToMap = findViewById(R.id.goToMap);
        accountEmail = findViewById(R.id.homeAccountEmail);
        fullName = findViewById(R.id.homeFullName);
        favouritesList = findViewById(R.id.listOfProducers);
        // get the current user and find them and display info on the screen
        SharedPreferences sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        DBHelper databaseHelper = new DBHelper(HomeActivity.this);
        CustomerModel found = databaseHelper.getAllMatching(email).get(0);
        if(email != null){
            accountEmail.setText("Email: " + found.getEmail());
            fullName.setText("Welcome back " + found.getFirstName() + " " + found.getSurname() + "!");
        }
        // get all favourite producers' IDs
        List<Integer> allFavouritesIDs = databaseHelper.getAllFavourites(found);
        allFavourites = new ArrayList<>();
        for(int id : allFavouritesIDs){
            allFavourites.add(databaseHelper.getProducerByNameOrID(null, id).get(0));
        }
        // if no favourite producers, add an empty item to the list
        if(allFavourites.size() == 0){
            allFavourites.add(new ProducerModel(0, "Looks like the list is empty!",
                    "", "",
                    "Add some producers to your favourites to see them here!", "none", ""));
        }
        // list management
        favouritesList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        favouritesList.setLayoutManager(layoutManager);

        listAdapter = new RecycleViewAdapter(allFavourites, HomeActivity.this);
        favouritesList.setAdapter(listAdapter);

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        DBHelper databaseHelper = new DBHelper(HomeActivity.this);
        CustomerModel found = databaseHelper.getAllMatching(email).get(0);

        List<Integer> allFavouritesIDs = databaseHelper.getAllFavourites(found);
        allFavourites.clear();
        for(int id : allFavouritesIDs){
            allFavourites.add(databaseHelper.getProducerByNameOrID(null, id).get(0));
        }

        if(allFavourites.size() == 0){
            allFavourites.add(new ProducerModel(0, "Looks like the list is empty!",
                    "", "",
                    "Add some producers to your favourites to see them here!", "none", ""));
        }

        listAdapter.notifyDataSetChanged();
    }

    // use the menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_AtoZ:
                Collections.sort(allFavourites, ProducerModel.CompanyNameAZComparator);
                listAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_ZtoA:
                Collections.sort(allFavourites, ProducerModel.CompanyNameZAComparator);
                listAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_by_type:
                Collections.sort(allFavourites, ProducerModel.ProducerTypeComparator);
                listAdapter.notifyDataSetChanged();
                return true;
        }

        return true;
    }

    // confirmation of exiting the app
    @Override
    public void onBackPressed() {

        new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Exit")
                .setContentText("Do you really want to close the app?")
                .setCancelText("No")
                .setConfirmText("Exit!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finishAffinity();
                    }
                })
                .show();
    }
}