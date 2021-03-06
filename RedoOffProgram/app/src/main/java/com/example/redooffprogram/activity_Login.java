package com.example.redooffprogram;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class activity_Login extends AppCompatActivity {
    TextView noAccount;
    ProgressBar prog;
    Button login;
    EditText password, email;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        noAccount = findViewById(R.id.login_registrate);
        prog =  findViewById(R.id.loading);
        login = findViewById(R.id.login);
        password =  findViewById(R.id.password);
        email =  findViewById(R.id.username);
        prog.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), activity_Main.class));
            finish();
        }



       login.setOnClickListener(this::onClick);

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_signUp.class));
                finish();
            }
        });
    }

    private void onClick(View v) {


        String loginEmail = email.getText().toString().trim();
        String loginPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(loginEmail)) {
            email.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(loginPassword)) {
            password.setError("Password is required");
            return;
        }

        prog.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(activity_Login.this, task -> {
            if (task.isSuccessful()) {
                user = auth.getCurrentUser();
                assert user != null;
                if (!user.isEmailVerified()) {
                    Toast.makeText(activity_Login.this, "Account needs to be verified", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    prog.setVisibility(View.GONE);
                    return;
                }

                Toast.makeText(activity_Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                prog.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), activity_Main.class));
                finish();
            } else {
                Toast.makeText(activity_Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                prog.setVisibility(View.GONE);
            }

        });
    }
}
