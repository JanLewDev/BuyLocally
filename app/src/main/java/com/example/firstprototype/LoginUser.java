package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LoginUser extends AppCompatActivity {

    // references to all buttons and other controls on the layout
    TextView goToRegister, forgotPassword, goToProducer;
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
        goToProducer = findViewById(R.id.goto_producer);

        email = findViewById(R.id.accountEmail);
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

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(LoginUser.this, ForgotPassword.class);
                startActivity(i);
            }
        });

        goToProducer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent i = new Intent(LoginUser.this, ProducerDashboard.class);
                startActivity(i);
            }
        });

    }


    private void loginUser() {
        // convert all input into strings
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();


        // backdoor
//        if(str_email.equals("admin")){
//            Intent i = new Intent(LoginUser.this, HomeActivity.class);
//            startActivity(i);
//            return;
//        }

        if(str_email.length() == 0 || str_password.length() == 0){
            Toast.makeText(LoginUser.this, "Enter email and password!", Toast.LENGTH_SHORT).show();
        } else {
            // get the database
            DBHelper databaseHelper = new DBHelper(LoginUser.this);

            // find all matching records
            List<CustomerModel> allMatching = databaseHelper.getAllMatching(str_email);

            // check if an account of this email already exists
            if (allMatching.size() == 0) {
                Toast.makeText(LoginUser.this, "There is no account with this email address, try registering!", Toast.LENGTH_SHORT).show();
            } else {
                CustomerModel found = allMatching.get(0);


                if (!found.getPassword().equals(str_password)) {
                    Toast.makeText(LoginUser.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", found.getEmail());
                    editor.apply();

                    Intent i = new Intent(LoginUser.this, HomeActivity.class);
                    startActivity(i);
                }
            }
        }

    }
}