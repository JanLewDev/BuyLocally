package com.example.firstprototype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ForgotPassword extends AppCompatActivity {

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

        email = findViewById(R.id.email);
        verificationCode = findViewById(R.id.VerificationCode);
        newPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        sendCode = findViewById(R.id.sendCode);
        changePassword = findViewById(R.id.changePassword);


        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
    }

    private void sendCode(){
        String str_email = email.getText().toString();

        DBHelper databaseHelper = new DBHelper(ForgotPassword.this);

        List<CustomerModel> allMatching = databaseHelper.getAllMatching(str_email);

        if(allMatching.size() == 0){
            Toast.makeText(ForgotPassword.this, "An account with this email address does not exist, try registering!", Toast.LENGTH_SHORT).show();
            return;
        }

        Code = new Random().nextInt(900000) + 100000;
        String str_code = Integer.toString(Code);
        Toast.makeText(ForgotPassword.this, str_code, Toast.LENGTH_SHORT).show();


        CustomerModel found = allMatching.get(0);
        String name = found.getFirstName();

        try {
            EmailsHelper.send(name, str_email, Code);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}