package com.example.redooffprogram;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class GMailReciever extends AsyncTask<Void, Void, Void> {

    protected String email;
    protected String pwd;
    protected FirebaseDatabase base;
    protected DatabaseReference ref;
    protected TextView viewText;
    protected String[] text = new String[2];

    public GMailReciever(String email, String pwd, TextView viewText) {
        this.email = email;
        this.pwd = pwd;
        this.viewText = viewText;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        String hostval = "Pop.gmail.com";
        String mailStrProt = "pop3";


        try {
            Properties propvals = new Properties();
            propvals.put("mail.pop3.host", hostval);
            propvals.put("mail.pop3.port", "995");
            propvals.put("mail.pop3.starttls.enable", "true");

            Session emailSessionObj = Session.getDefaultInstance(propvals);
            Store storeObj = emailSessionObj.getStore("pop3s");

            storeObj.connect(hostval, email, pwd);

            Folder emailFolderObj = storeObj.getFolder("INBOX");

            emailFolderObj.open(Folder.READ_ONLY);

            Message[] messageobjs = emailFolderObj.getMessages();

            base = FirebaseDatabase.getInstance();
            ref = base.getReference("Public Keys");

            System.out.println("I come to for");
            System.out.println(messageobjs.length);


            for (Message indvidualmsg : messageobjs) {
                String temp = indvidualmsg.getFrom()[0].toString();
                String temp2 = "";

                for(int i = 0; i < temp.length(); i++) {


                    if(temp.charAt(i) == '<') {
                       for(int a = i + 1; a < temp.length(); a++) {
                           temp2 = temp2 + temp.charAt(a);

                           if(temp.charAt(a) == '>') {
                               i = temp.length() - 1;
                               break;
                           }
                       }
                    }
                }
                if (temp2.equals("paulius476@gmail.com")) {
                    text[0] = temp2;
                    text[1] = indvidualmsg.getContent().toString();

                    break;
                }

                temp = "";
            }


            System.out.println(text[0]);

            System.out.println("i come all the way here");

            emailFolderObj.close(false);
            storeObj.close();

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }

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
