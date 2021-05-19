package com.example.redooffprogram;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailSender extends AsyncTask<Void, Void, Void> {

    private Session session;

    //Information to send email
    private String mailhost = "smtp.gmail.com";
    protected final String user;
    protected final String subject;
    protected final String messageText;
    protected final String password;
    protected final String reciever;
    @SuppressLint("StaticFieldLeak")
    protected Context context;
    private static int amount = 0;


    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    public GMailSender(Context context, String user, String password, String subject, String messageText, String reciever) {
        this.user = user;
        this.password = password;
        this.messageText = messageText;
        this.subject = subject;
        this.reciever = reciever;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Properties props = new Properties();

        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try{
            MimeMessage message = new MimeMessage(session);

            DataHandler handler = new DataHandler(new ByteArrayDataSource(messageText.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(user));
            message.setSubject(subject);
            message.setDataHandler(handler);
            if (reciever.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reciever));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(reciever));
            Transport.send(message);
        }catch(Exception e){
            e.printStackTrace();
        }


        FirebaseDatabase base = FirebaseDatabase.getInstance();
        DatabaseReference ref = base.getReference("Keys");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()
                     ) {

                    String email = datasnapshot.child("email").getValue(String.class);

                    if(email.equals(reciever)) {
                        try {

                            KeyFactory kf = KeyFactory.getInstance("RSA");
                            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(datasnapshot.child("publicKey").getValue(String.class)));
                            RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

                            Encryption en = new Encryption(messageText, pubKey);
                            byte[] text = en.encrypt();

                            System.out.println("THIS IS BEFORE THE TEST" + text);

                            System.out.println("The key is : " + en.getEncryptedKey());


                            String textMsg = Base64.getEncoder().encodeToString(text);

                            System.out.println("THIS IS A TEST: " + Base64.getDecoder().decode(textMsg));
                            System.out.println("THIS IS A TEST: " + Base64.getDecoder().decode(textMsg));

                            DatabaseReference ref = base.getReference("Message");
                            String id = "msg" + amount;


                            MessageHelper msg = new MessageHelper(textMsg, reciever, user, subject, Base64.getEncoder().encodeToString(en.getEncryptedKey()));

                            amount++;

                            ref.child(id).setValue(msg);

                        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                            e.printStackTrace();
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

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }

}
