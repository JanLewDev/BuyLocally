package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterUser extends AppCompatActivity {

    // references to all buttons and other controls on the layout
    Button btn_register, btn_gotoLogin;
    EditText et_firstName, et_surname, et_email, et_password, et_confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        // hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // assign all buttons and other controls
        et_firstName = findViewById(R.id.firstName);
        et_surname = findViewById(R.id.surname);
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        et_confirmPassword = findViewById(R.id.confirmPassword);

        btn_register = findViewById(R.id.register);
        btn_gotoLogin = findViewById(R.id.goto_login);


        // set onClickListeners
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();

            }
        });

        btn_gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(RegisterUser.this, LoginUser.class);
                startActivity(i);

            }
        });
    }

    private void registerUser(){
        // convert all inputs to strings
        String str_firstName = et_firstName.getText().toString();
        String str_surname = et_surname.getText().toString();
        String str_email = et_email.getText().toString();
        String str_password = et_password.getText().toString();
        String str_confirmPassword = et_confirmPassword.getText().toString();

        CustomerModel customer = new CustomerModel(-1, str_firstName, str_surname, str_email, str_password);

        DBHelper databaseHelper = new DBHelper(RegisterUser.this);

        if(str_firstName.length() < 3){
            Toast.makeText(RegisterUser.this, "First name must be at least 3 characters long!", Toast.LENGTH_SHORT).show();

        } else if(str_surname.length() < 3){
            Toast.makeText(RegisterUser.this, "Surname must be at least 3 characters long!", Toast.LENGTH_SHORT).show();

        } else if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            Toast.makeText(RegisterUser.this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();

        } else if(!isPasswordValid(str_password)){
            // checks if the password is secure enough
        } else if(!str_password.equals(str_confirmPassword)){
            Toast.makeText(RegisterUser.this, "The passwords are not the same!", Toast.LENGTH_SHORT).show();

        } else if(databaseHelper.getAllMatching(str_email).size() > 0) {
            Toast.makeText(RegisterUser.this, "Account with this email already exists, try to login!", Toast.LENGTH_SHORT).show();
            
        } else {

            boolean success = databaseHelper.addCustomer(customer);

            new SweetAlertDialog(RegisterUser.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Message")
                    .setContentText("Registered successfully!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            final Intent i = new Intent(RegisterUser.this, LoginUser.class);
                            startActivity(i);

                        }
                    })
                    .show();

        }

    }

    private boolean isPasswordValid(String passwd){
        if(passwd.length() < 8){
            Toast.makeText(RegisterUser.this, "Password must be at least 8 characters long!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(passwd.equals(passwd.toLowerCase())){
            Toast.makeText(RegisterUser.this, "Password must contain at least one capital letter!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!passwd.matches(".*[0-9].*")){
            Toast.makeText(RegisterUser.this, "Password must contain at least one number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}