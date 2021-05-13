package com.example.redooffprogram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activity_Send extends AppCompatActivity implements View.OnClickListener{

    final private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    private FirebaseDatabase base = FirebaseDatabase.getInstance();
    private DatabaseReference ref = base.getReference("Users");

    protected Button send, back;
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


        String myEmail = user.getEmail();
        me.setText(myEmail);

        send.setOnClickListener(this);

    }

    private void sendEmail() {

        Query checkUser = ref.orderByChild("id").equalTo(user.getUid());

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String password = snapshot.child(user.getUid()).child("password").getValue(String.class);
                String email = snapshot.child(user.getUid()).child("email").getValue(String.class);

                try {

                    String subject = sub.getText().toString().trim();
                    String message = msg.getText().toString().trim();
                    String rec = reciever.getText().toString().trim();

                    send(email, password, subject, message, rec);


                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_Send.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }


        });

    }

    private void send(String email, String password, String subject, String message, String rec) {
        GMailSender temp = new GMailSender(this, email, password, subject, message, rec);
        temp.execute();
    }


    private void setBarVis (ProgressBar bar) {
        bar.setVisibility(View.VISIBLE);
    }
    private void setBarGone (ProgressBar bar) {
        bar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        sendEmail();
    }
}