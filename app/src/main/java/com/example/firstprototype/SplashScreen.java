package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class SplashScreen extends AppCompatActivity{

    GifImageView shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // create an Intent to send the user to after 3 seconds
        final Intent i = new Intent(SplashScreen.this, LoginUser.class);

        // timer for waiting 3 second while the gif file plays
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    // we do not need the splash screen activity anymore
                    finish();
                }
            }
        };

        // start the timer
        timer.start();

    }
}