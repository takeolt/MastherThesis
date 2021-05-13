package com.example.redooffprogram;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sun.mail.pop3.POP3Store;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class GMailReciever extends AsyncTask<Void, Void, Void> {

    protected String email;
    protected String pwd;
    protected FirebaseDatabase base;
    protected DatabaseReference ref;
    protected TextView viewText;
    protected String[] text = new String[2];
    protected Context context;

    public GMailReciever(Context context, String email, String pwd, TextView viewText) {
        this.email = email;
        this.pwd = pwd;
        this.viewText = viewText;
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        String hostval = "pop.gmail.com";
        String mailStrProt = "pop3s";




            Properties propvals = new Properties();
            propvals.put("mail.pop3.host", hostval);
            propvals.put("mail.pop3.port", "995");
            propvals.put("mail.pop3.starttls.enable", "true");
            propvals.put("mail.pop3.SSL.emable", "true");

            Session emailSessionObj = Session.getDefaultInstance(propvals);
        POP3Store emailStore = null;
        try {
            emailStore = (POP3Store) emailSessionObj.getStore(mailStrProt);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            emailStore.connect(email, pwd);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Folder emailFolderObj = null;
        try {
            emailFolderObj = emailStore.getFolder("INBOX");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            emailFolderObj.open(Folder.READ_ONLY);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Message[] messageobjs = new Message[0];
        try {
            messageobjs = emailFolderObj.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        base = FirebaseDatabase.getInstance();
            ref = base.getReference("Public Keys");

            System.out.println("I come to for");
            System.out.println(messageobjs.length);


            for (Message indvidualmsg : messageobjs) {
                String temp = null;
                try {
                    temp = indvidualmsg.getFrom()[0].toString();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                String temp2 = "";

                for(int i = 0; i < temp.length(); i++) {


                    if(temp.charAt(i) == '<') {
                       for(int a = i + 1; a < temp.length(); a++) {

                           if(temp.charAt(a) == '>') {
                               i = temp.length() - 1;
                               break;
                           }

                           temp2 = temp2 + temp.charAt(a);
                       }
                    }
                }




            FirebaseDatabase base = FirebaseDatabase.getInstance();
            DatabaseReference ref = base.getReference("Message");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String myEmail = user.getEmail();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()
                         ) {

                        String email = dataSnapshot.child("receiver").getValue(String.class);

                        if(email.equals(myEmail)) {
                            String message = dataSnapshot.child("message").getValue(String.class);
                            String encryptedKey = dataSnapshot.child("encryptedKey").getValue(String.class);

                            System.out.println("Encrypted" + encryptedKey);


                            Decryption dec = new Decryption(encryptedKey, message, context, user.getUid(), viewText);
                            if(dec.getPrivateKey() == null) {
                                System.out.println("This is wrong");
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });





        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(text[0] != null) {
            String msg = "Subject: " + text[0] + "\n " + "Text: " + text[1];
            System.out.println(msg);
            viewText.setText(msg);
        }
    }
}
