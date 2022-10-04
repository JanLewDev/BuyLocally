package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ProducerProfile extends AppCompatActivity {

    // define all necessary variables
    TextView companyName, fullName, description, type, favouritesText;
    ImageView star;
    Button btn_goToHome;
    CustomerModel CurrentCustomer;
    ProducerModel CurrentProducer;
    private boolean isItFavourite;
    private AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f);
    private AlphaAnimation fadeOut = new AlphaAnimation(1.0f , 0.0f);
    private int LastKnownColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_profile);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Producer profile");
        }
        // reference all elements on the screen
        companyName = findViewById(R.id.profileCompanyName);
        fullName = findViewById(R.id.fullName);
        description = findViewById(R.id.descriptionContact);
        type = findViewById(R.id.productType);
        favouritesText = findViewById(R.id.favouritesText);
        star = findViewById(R.id.star);
        btn_goToHome = findViewById(R.id.goToHome);

        // use shared preferences to know what producer data to fetch
        SharedPreferences sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE);
        String str_companyName = sharedPreferences.getString("companyName", "");
        String str_email = sharedPreferences.getString("email", "");

        // fetch the producer from the database
        DBHelper databaseHelper = new DBHelper(ProducerProfile.this);
        ProducerModel producer = databaseHelper.getProducerByNameOrID(str_companyName, -1).get(0);
        CustomerModel customer = databaseHelper.getAllMatching(str_email).get(0);

        CurrentCustomer = customer;
        CurrentProducer = producer;

        // set the appropriate text on the screen
        companyName.setText(producer.getCompanyName());
        fullName.setText(producer.getFirstName() + " " + producer.getSurname());
        description.setText(producer.getDescription());
        type.setText("Type of product sold - " + producer.getType());

        LastKnownColour = ContextCompat.getColor(this, R.color.yellow);
        isItFavourite = false;
        // check if the producer is in favourites, if yes then amend the text on the screen
        List<Integer> allFavourites = databaseHelper.getAllFavourites(customer);
        if(allFavourites.contains(producer.getId())){
            favouritesText.setText("Remove this producer from your favourites");
            LastKnownColour = ContextCompat.getColor(this, R.color.red);
            star.setColorFilter(ContextCompat.getColor(this, R.color.red));
            isItFavourite = true;
        }


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isItFavourite) {
                    addToFavourites(CurrentCustomer, CurrentProducer);
                } else {
                    removeFromFavourites(CurrentCustomer, CurrentProducer);
                }
            }
        });

        btn_goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(ProducerProfile.this, HomeActivity.class);
                startActivity(i);
            }
        });

    }

    private void addToFavourites(CustomerModel customer, ProducerModel producer){
        // call the function from database helper
        DBHelper databaseHelper = new DBHelper(ProducerProfile.this);
        boolean success = databaseHelper.addToFavourites(customer, producer);
        isItFavourite = true;
        // animate the star and text
        star.animate().setDuration(2000);
        star.animate().rotationBy(1080 - star.getRotation());
        star.animate().rotationYBy(360 - star.getRotationY());
        favouritesText.startAnimation(fadeOut);
        favouritesText.setText("Remove this producer from your favourites");
        favouritesText.startAnimation(fadeIn);
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);
        // for smooth colour change
        int colourFrom = LastKnownColour;
        int colourTo = ContextCompat.getColor(this, R.color.red);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colourFrom, colourTo);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LastKnownColour = (int) valueAnimator.getAnimatedValue();
                star.setColorFilter(LastKnownColour);
            }
        });
        valueAnimator.start();
    }

    private void removeFromFavourites(CustomerModel customer, ProducerModel producer){
        // call the function from the database helper
        DBHelper databaseHelper = new DBHelper(ProducerProfile.this);
        boolean success = databaseHelper.removeFromFavourites(customer, producer);
        isItFavourite = false;
        // animate the star and the text
        star.animate().setDuration(2000);
        star.animate().rotationBy(-1080 - star.getRotation());
        star.animate().rotationYBy(-360 - star.getRotationY());
        favouritesText.startAnimation(fadeOut);
        favouritesText.setText("Add this producer to your favourites");
        favouritesText.startAnimation(fadeIn);
        fadeOut.setDuration(1000);
        fadeOut.setFillAfter(true);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);
        // for smooth colour change
        int colourFrom = LastKnownColour;
        int colourTo = ContextCompat.getColor(this, R.color.yellow);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colourFrom, colourTo);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LastKnownColour = (int) valueAnimator.getAnimatedValue();
                star.setColorFilter(LastKnownColour);
            }
        });
        valueAnimator.start();
    }

}