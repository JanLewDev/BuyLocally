package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LoginUser extends AppCompatActivity {

    // references to all buttons and other controls on the layout
    TextView goToRegister, forgotPassword;
    EditText email, password;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        // hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // assign every element on the screen
        goToRegister = findViewById(R.id.goto_register);
        forgotPassword = findViewById(R.id.forgotPassword);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login = findViewById(R.id.login);

        // set on click listeners

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(LoginUser.this, RegisterUser.class);
                startActivity(i);

            }
        });
    }

    private void loginUser() {
        // convert all input into strings
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();

        if(str_email.length() == 0 || str_password.length() == 0){
            Toast.makeText(LoginUser.this, "Enter email and password!", Toast.LENGTH_SHORT).show();
        } else {
            // get the database
            DBHelper databaseHelper = new DBHelper(LoginUser.this);

            // find all matching records
            List<CustomerModel> allMatching = databaseHelper.getAllMatching(str_email);

            if (allMatching.size() == 0) {
                Toast.makeText(LoginUser.this, "There is no account with this email address, try registering!", Toast.LENGTH_SHORT).show();
            } else {
                CustomerModel found = allMatching.get(0);


                if (!found.getPassword().equals(str_password)) {
                    Toast.makeText(LoginUser.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(LoginUser.this, HomeActivity.class);
                    startActivity(i);
                }
            }
        }

    }
}