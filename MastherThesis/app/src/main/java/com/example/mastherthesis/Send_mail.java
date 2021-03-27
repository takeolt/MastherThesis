package com.example.mastherthesis;

import android.content.Intent;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Send_mail extends AppCompatActivity {

    EditText sender, receiver, subject, password, message;

    Button send;

    protected Session session;
    protected FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    protected FirebaseUser user = firebaseAuth.getCurrentUser();
    protected String email = user.getEmail();
    protected ProgressBar prog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);


        receiver = (EditText) findViewById(R.id.send_reciever);
        subject = (EditText) findViewById(R.id.send_Subject);
        password = (EditText) findViewById(R.id.send_password);
        message = (EditText) findViewById(R.id.send_Message);
        sender = (EditText) findViewById(R.id.send_Sender);

        sender.setText(email);

        send = (Button) findViewById(R.id.send_send_Button);
        prog = (ProgressBar) findViewById(R.id.send_progress);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog.setVisibility(View.VISIBLE);

                Properties props = new Properties();

                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "465");

                session = Session.getDefaultInstance(props, new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password.getText().toString());
                    }
                });

                try {
                    Message mm = new MimeMessage(session);
                    mm.setFrom(new InternetAddress(email));
                    mm.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver.getText().toString()));
                    mm.setSubject(subject.getText().toString());
                    mm.setText(message.getText().toString());

                    Transport.send(mm);

                    prog.setVisibility(View.INVISIBLE);
                    Toast.makeText(Send_mail.this, "Message Sent", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }
                catch (MessagingException e) {
                    e.printStackTrace();
                }

                prog.setVisibility(View.INVISIBLE);
            }
        });
        
    }
}
