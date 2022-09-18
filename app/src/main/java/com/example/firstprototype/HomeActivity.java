package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {

    Button goToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        goToMap = findViewById(R.id.goToMap);

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
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