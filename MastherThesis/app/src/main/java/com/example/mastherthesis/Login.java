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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextView noAccount, tittle, appName;
    ProgressBar prog;
    Button login;
    EditText password, email;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        noAccount = (TextView) findViewById(R.id.LoginNoAcountText);
        tittle = (TextView) findViewById(R.id.TittleName);
        appName = (TextView) findViewById(R.id.Mmail_login_Text);
        prog = (ProgressBar) findViewById(R.id.progressBarLogin);
        login = (Button) findViewById(R.id.LoginButton);
        password = (EditText) findViewById(R.id.LoginPasswordText);
        email = (EditText) findViewById(R.id.LoginEmailText);
        prog.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String loginEmail = email.getText().toString().trim();
                String loginPassword = password.getText().toString().trim();

                if(TextUtils.isEmpty(loginEmail)) {
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(loginPassword)) {
                    password.setError("Password is required");
                    return;
                }

                prog.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(!user.isEmailVerified()) {
                                Toast.makeText(Login.this, "Account needs to be verified", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                prog.setVisibility(View.INVISIBLE);
                                return;
                            }
                            prog.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            prog.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });
    }
}