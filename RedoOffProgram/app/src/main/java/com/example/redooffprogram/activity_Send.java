package com.example.redooffprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_Send extends AppCompatActivity {

    final private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    private FirebaseDatabase base = FirebaseDatabase.getInstance();
    private DatabaseReference ref = base.getReference("Users");

    protected Button send;
    protected EditText me, reciever, sub, msg;
    protected ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__send);

        send = (Button) findViewById(R.id.send_button);
        me = (EditText) findViewById(R.id.send_me);
        reciever = (EditText) findViewById(R.id.send_reciever);
        sub = (EditText) findViewById(R.id.subject);
        msg = (EditText) findViewById(R.id.send_message);
        bar = (ProgressBar) findViewById(R.id.send_load);


        //String myEmail = user.getEmail();
        //me.setText(myEmail);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = ref.child(user.getUid()).child("password").toString();

                msg.setText(password);
            }
        });

    }
}