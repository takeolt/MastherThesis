package com.example.redooffprogram;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Activity_signUp extends AppCompatActivity {

    EditText email, password, re_Password;
    Button signUp;
    ProgressBar load;
    TextView loginHere;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        checkIfLoggedIn();

        signUp = (Button) findViewById(R.id.registerButton);
        email = (EditText) findViewById(R.id.EmailAddress);
        password = (EditText) findViewById(R.id.Password);
        re_Password = (EditText) findViewById(R.id.RePassword);
        load = (ProgressBar) findViewById(R.id.accountVerificationBar);
        loginHere = (TextView) findViewById(R.id.goToLoginText);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setVisible(load);

                if (checkInformation()) {
                    auth.createUserWithEmailAndPassword(userText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Activity_signUp.this, "User was created successfully, Please verify it", Toast.LENGTH_SHORT).show();

                                            ref = database.getReference("Users");
                                            FirebaseUser user = auth.getCurrentUser();
                                            String uid = user.getUid();

                                            UserInfo info = makeInfo(userText, passwordText, uid);

                                            if (info != null) {
                                                ref.child(uid).setValue(info);
                                            }

                                            setGone(load);
                                            startActivity(new Intent(getApplicationContext(), activity_Main.class));
                                            finish();

                                        } else {
                                            Toast.makeText(Activity_signUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(Activity_signUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                else{
                    setGone(load);
                    
                }
            }
        });



        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), activity_Login.class));
                finish();
            }
        });
    }

    private void checkIfLoggedIn() {
        if(auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), activity_Main.class));
            finish();
        }
    }

    private void clearText(EditText text) {
        text.getText().clear();
    }

    private void setVisible(ProgressBar bar) {
        bar.setVisibility(View.VISIBLE);
    }

    private void setGone(ProgressBar bar) {
        bar.setVisibility(View.GONE);
    }

    private boolean checkInformation() {

        if(TextUtils.isEmpty(email.getText().toString().trim())) {

            clearText(password);
            clearText(re_Password);

            email.setError("Email is required");

            return false;
        }

        else if (TextUtils.isEmpty(password.getText().toString().trim())) {

            clearText(password);
            clearText(re_Password);

            password.setError("Password is required");

            return false;
        }

        else if(password.getText().toString().trim().length() < 6) {

            clearText(password);
            clearText(re_Password);

            password.setError("Password is too short");

            return false;
        }

        else if (password.getText().toString().trim().equals(re_Password.getText().toString().trim())) {

            clearText(re_Password);

            re_Password.setError("Passwords don't match each other");

            return false;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private UserInfo makeInfo(String email, String password, String id) {
        try {
            DatabaseReference refr = database.getReference("Keys");
            KeysGenerator keys = new KeysGenerator();
            KeyHelper temp = new KeyHelper(id, email,keys.encodedPublicKey(), keys.encodedPrivateKey());
            refr.child(id).setValue(temp);

            return new UserInfo(email, password, keys.getPrivateKey(),keys.getPublicKey(), id);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

}