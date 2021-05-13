package com.example.redooffprogram;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Encryption {
    final private String message;
    final private PublicKey publicKey;
    private SecretKey randomKey;
    private byte[] encryptedKey;
    private byte[] encryptedText;

    public Encryption(String text, PublicKey publicKey ) {
        this.message = text;
        this.publicKey = publicKey;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] encrypt() throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        randomKey = keyGen.generateKey();

        try {
            Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aesCipher.init(Cipher.ENCRYPT_MODE, randomKey);
            System.out.println("This is the key: " + randomKey);
            byte[] temp = message.getBytes();

            byte[] temp2 = randomKey.getEncoded();


            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            encryptedKey = rsaCipher.doFinal(temp2);

            encryptedText = aesCipher.doFinal(temp);

            return encryptedText;
        }
        catch (NoSuchPaddingException e ) {
            System.out.println("No such padding exist");
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            System.out.println("No such Key");
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


        return null;

    }

    protected SecretKey getRandomKey() {
        return randomKey;
    }

    protected byte[] getEncryptedKey() {
        return encryptedKey;

    }

    public byte[] getEncryptedText() {
        return encryptedText;
    }
}
