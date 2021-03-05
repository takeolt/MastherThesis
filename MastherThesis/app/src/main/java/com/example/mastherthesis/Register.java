package com.example.mastherthesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText emailAddress, password, name, rePassword;
    TextView appName, goToLoginText, registerText;
    Button registerButton;
    FirebaseAuth firebaseAuth;
    ProgressBar progress;


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
        progress = findViewById(R.id.accountVerificationBar);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailAddress.getText().toString().trim();
                String password1 = password.getText().toString().trim();
                String password2 = rePassword.getText().toString().trim();
                String personName = name.getText().toString().trim();


                if(TextUtils.isEmpty(email)) {
                    emailAddress.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(personName)) {
                    name.setError("Name is Required");
                    return;
                }

                if(TextUtils.isEmpty(password1)) {
                    password.setError("Password is required");
                    return;
                }

                if(password1.length() < 6) {
                    password.getText().clear();
                    rePassword.getText().clear();
                    password.setError("Password is too short, password should be at least 6 of length");
                    return;
                }

                if(!password1.equals(password2)) {
                    password.getText().clear();
                    rePassword.getText().clear();
                    rePassword.setError("Reentered password is not equal the password");
                    return;
                }

                progress.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }

                                else {
                                    Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                );
            }
        });


    }
}