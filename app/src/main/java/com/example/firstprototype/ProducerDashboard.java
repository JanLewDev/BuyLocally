package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProducerDashboard extends AppCompatActivity {

    // references to all buttons and other controls on the layout
    Button btn_submit;
    EditText et_secretCode, et_companyName, et_firstName, et_surname, et_description, et_location;
    RadioButton rb_butcher, rb_bakery, rb_diary, rb_grocery;
    
    // define the secret code
    private final String SecretCode = "654321";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_dashboard);

        // hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // locate all elements on the screen
        btn_submit = findViewById(R.id.addProducer);
        et_secretCode = findViewById(R.id.secretCode);
        et_companyName = findViewById(R.id.companyName);
        et_firstName = findViewById(R.id.firstName);
        et_surname = findViewById(R.id.surname);
        et_description = findViewById(R.id.description);
        et_location = findViewById(R.id.location);
        rb_butcher = findViewById(R.id.butcher);
        rb_bakery = findViewById(R.id.bakery);
        rb_diary = findViewById(R.id.diary);
        rb_grocery = findViewById(R.id.grocery);

        // set a listener for the submit button
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDetails();
            }
        });
    }

    private void addDetails(){
        // get the string values of all elements
        String str_secret = et_secretCode.getText().toString();
        String str_company = et_companyName.getText().toString();
        String str_firstName = et_firstName.getText().toString();
        String str_surname = et_surname.getText().toString();
        String str_description = et_description.getText().toString();
        String str_location = et_location.getText().toString();
        String type = "";
        // a marker to know if an option has been chosen
        boolean selectedType = false;
        
        
        // check if the secret code is correct, which for now is 654321
        if(!str_secret.equals(SecretCode)){
            Toast.makeText(this, "Incorrect secret code!", Toast.LENGTH_SHORT).show();
        } else if(str_company.length() < 3){
            Toast.makeText(this, "Company name must be at least 3 characters long!", Toast.LENGTH_SHORT).show();
        } else if(str_firstName.length() < 2 || str_surname.length() < 3){
            Toast.makeText(this, "Enter Your name and surname!", Toast.LENGTH_SHORT).show();
        } else if(str_description.length() < 10){
            Toast.makeText(this, "Description must be at least 10 characters long!", Toast.LENGTH_SHORT).show();
        } else {
            // check the buttons to check which one has been selected and if any has been selected
            if (rb_butcher.isChecked()) {
                type = rb_butcher.getText().toString();
                selectedType = true;
            } else if (rb_bakery.isChecked()) {
                type = rb_bakery.getText().toString();
                selectedType = true;
            } else if (rb_diary.isChecked()) {
                type = rb_diary.getText().toString();
                selectedType = true;
            } else if (rb_grocery.isChecked()) {
                type = rb_grocery.getText().toString();
                selectedType = true;
            }
            // check if any type was selected
            if (!selectedType) {
                Toast.makeText(this, "Select a type!", Toast.LENGTH_SHORT).show();
            } else if (!checkValidLocation(str_location)) {
                // if wrong format of location then stop
            } else {
                // create a producer object
                ProducerModel producer = new ProducerModel(-1, str_company, str_firstName, str_surname,
                str_description, type, str_location);

                // add the producer to the database
                DBHelper databaseHelper  = new DBHelper(ProducerDashboard.this);
                boolean success = databaseHelper.addProducer(producer);

                // signal that information has been added successfully
                new SweetAlertDialog(ProducerDashboard.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Message")
                        .setContentText("Updated info successfully!")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                final Intent i = new Intent(ProducerDashboard.this, LoginUser.class);
                                startActivity(i);
                            }
                        })
                        .show();
            }
        }
    }

    // helper function to check if the location is in correct format
    boolean checkValidLocation(String location){
        // using try to catch conversion errors
        try {
            // check for comma and if it is not the last character
            if(!location.contains(",") || location.indexOf(",")==location.length()-1){
                Toast.makeText(this, "Enter a valid location!", Toast.LENGTH_SHORT).show();
                return false;
            }
            // using split function to get the two parts of ths string and then converting it to double
            String[] parts = location.split(",");
            double longitude = Double.valueOf(parts[0]);
            double latitude = Double.valueOf(parts[1]);
            System.out.println(longitude);
            System.out.println(latitude);
            return true;
        } catch (NumberFormatException e) {
            // if conversion failed, then location is not valid
            Toast.makeText(this, "Enter a valid location!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}