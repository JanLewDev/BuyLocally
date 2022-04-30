package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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