package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPassword extends AppCompatActivity {

    // declare all elements of the screen
    EditText email, verificationCode, newPassword, confirmPassword;
    Button sendCode, changePassword;

    private int Code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // find all elements of the screen, inputs and buttons
        email = findViewById(R.id.accountEmail);
        verificationCode = findViewById(R.id.VerificationCode);
        newPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        sendCode = findViewById(R.id.sendCode);
        changePassword = findViewById(R.id.changePassword);

        // set up click listeners to the buttons
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDetails();
            }
        });
    }

    private void sendCode(){
        String str_email = email.getText().toString();

        // initialise the database helper and find matching accounts
        DBHelper databaseHelper = new DBHelper(ForgotPassword.this);
        List<CustomerModel> allMatching = databaseHelper.getAllMatching(str_email);

        // no matching accounts
        if(allMatching.size() == 0){
            Toast.makeText(ForgotPassword.this, "Account with this email address does not exist, try registering!", Toast.LENGTH_SHORT).show();
            return;
        }
        // generate the verification code and convert it to string
        Code = new Random().nextInt(900000) + 100000;
        String str_code = Integer.toString(Code);

        // get the name and then use EmailsHelper class to send the message
        CustomerModel found = allMatching.get(0);
        String name = found.getFirstName();

        try {
            EmailsHelper.send(name, str_email, Code);
            Toast.makeText(ForgotPassword.this, "Verification message sent!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void checkDetails(){
        // get the string values of all parameters
        String str_email = email.getText().toString();
        String str_verificationCode = verificationCode.getText().toString();
        String str_newPassword = newPassword.getText().toString();
        String str_confirmPassword = confirmPassword.getText().toString();
        String str_code = Integer.toString(Code);

        // verify the inputs
        if(!str_verificationCode.equals(str_code)){
            Toast.makeText(ForgotPassword.this, "The verification code is not correct, try again!", Toast.LENGTH_SHORT).show();
        } else if(!isPasswordValid(str_newPassword)){
            // check if password is secure enough
        } else if(!str_newPassword.equals(str_confirmPassword)){
            Toast.makeText(ForgotPassword.this, "Passwords are not the same!", Toast.LENGTH_SHORT).show();
        } else {
            // initialize the database helper
            DBHelper databaseHelper = new DBHelper(ForgotPassword.this);

            // get all matching accounts and then the first of them
            List<CustomerModel> allMatching = databaseHelper.getAllMatching(str_email);
            CustomerModel found = allMatching.get(0);

            if(found.getPassword().equals(str_newPassword)){
                Toast.makeText(this, "Your new password cannot be the same as your old one!", Toast.LENGTH_SHORT).show();
                return;
            }

            // call the change password function with the new password and the customer ID
            boolean success = databaseHelper.changePassword(str_newPassword, found.getId());

            // using an alert to inform the user the password has been changed and move to login page
            new SweetAlertDialog(ForgotPassword.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Message")
                    .setContentText("Password changed successfully!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            final Intent i = new Intent(ForgotPassword.this, LoginUser.class);
                            startActivity(i);
                        }
                    })
                    .show();
        }
    }

    // helper function to check if the new password is valid
    private boolean isPasswordValid(String passwd){
        if(passwd.length() < 8){
            Toast.makeText(ForgotPassword.this, "Password must be at least 8 characters long!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(passwd.equals(passwd.toLowerCase())){
            Toast.makeText(ForgotPassword.this, "Password must contain at least one capital letter!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!passwd.matches(".*[0-9].*")){
            Toast.makeText(ForgotPassword.this, "Password must contain at least one number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}