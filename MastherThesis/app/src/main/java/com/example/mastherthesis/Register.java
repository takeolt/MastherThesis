package com.example.mastherthesis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText emailAddress, password, name, rePassword;
    TextView appName, goToLoginText, registerText;
    Button registerButton;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailAddress = findViewById(R.id.EmailAddress);
        password = findViewById(R.id.Password);
        name = findViewById(R.id.Name);
        rePassword = findViewById(R.id.RePassword);
        appName = findViewById(R.id.appName);
        goToLoginText = findViewById(R.id.goToLoginText);
        registerText = findViewById(R.id.registerText);
        registerButton = findViewById(R.id.registerButton);

        firebaseAuth = FirebaseAuth.getInstance();


    }
}