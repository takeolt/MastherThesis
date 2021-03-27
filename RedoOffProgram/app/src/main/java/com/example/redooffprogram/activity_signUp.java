package com.example.redooffprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class activity_signUp extends AppCompatActivity {

    EditText userName, password, rePassword;
    Button signInn;
    ProgressBar load;
    TextView loginHere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signInn = (Button) findViewById(R.id.registerButton);
        userName = (EditText) findViewById(R.id.EmailAddress);
        password = (EditText) findViewById(R.id.Password);
        rePassword = (EditText) findViewById(R.id.RePassword);
        load = (ProgressBar) findViewById(R.id.accountVerificationBar);
        loginHere = (TextView) findViewById(R.id.goToLoginText);


        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });



    }
}