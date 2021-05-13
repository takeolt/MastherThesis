package com.example.redooffprogram;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class Decryption {

    private PrivateKey privateKey;
    private final byte[] encryptedKey;
    private final byte[] encryptedText;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public Decryption(String encryptedKey, String encryptedText, Context context, String id, TextView view) {
        this.encryptedText = Base64.getDecoder().decode(encryptedText);
        this.encryptedKey = Base64.getDecoder().decode(encryptedKey);


        FirebaseDatabase base = FirebaseDatabase.getInstance();
        DatabaseReference ref = base.getReference("Keys");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shot: snapshot.getChildren()
                ) {

                    String tempId = shot.child("id").getValue(String.class);

                    if(tempId.equals(id)) {
                        String privateKeyTemp = shot.child("privateKey").getValue(String.class);
                        byte[] temp = Base64.getDecoder().decode(privateKeyTemp);

                        try {
                            KeyFactory fac = KeyFactory.getInstance("RSA");
                            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(temp);
                            setPrivateKey(fac.generatePrivate(keySpecPKCS8));

                            byte[] text = decrypt();

                            String temp3 = new String(text, StandardCharsets.UTF_8);

                            view.setText(temp3);

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


    }


    public byte[] decrypt() {
        try {

            Cipher rsaCipher  = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] temp = rsaCipher.doFinal(encryptedKey);

            SecretKey secretKey = new SecretKeySpec(temp, 0, temp.length, "AES");

            Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aesCipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] test = aesCipher.doFinal(encryptedText);

            System.out.println(test);
            return test;
        }
        catch (NoSuchPaddingException e ) {
            System.out.println("No such padding exist");
            e.printStackTrace();
        }

        catch (IllegalBlockSizeException e) {
            System.out.println("Too big of a message");
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            System.out.println("Bad padding");
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("No such algorithm");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
