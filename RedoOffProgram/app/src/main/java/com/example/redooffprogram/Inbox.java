package com.example.redooffprogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Inbox extends AppCompatActivity {

    protected Button back, msg;
    protected TextView text;

    protected FirebaseAuth auth;
    protected FirebaseUser user;

    protected FirebaseDatabase base;
    protected DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);


        back = (Button) findViewById(R.id.inboxgoBack);
        text = (TextView) findViewById(R.id.inbox_text);
        msg = (Button) findViewById(R.id.get_Message);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainSite.class));
                finish();
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();

                String uid = user.getUid();

                base = FirebaseDatabase.getInstance();
                ref = base.getReference("Users");

                Query checkUser =ref.orderByChild("id").equalTo(uid);

                System.out.println("I come to listiner");

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println("Come to befire recue");
                        String password = snapshot.child(uid).child("password").getValue(String.class);
                        String email = snapshot.child(uid).child("email").getValue(String.class);
                        GMailReciever recieve = new GMailReciever(email, password, text);
                        recieve.execute();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Inbox.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}