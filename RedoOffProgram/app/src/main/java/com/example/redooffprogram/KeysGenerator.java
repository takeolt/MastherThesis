package com.example.redooffprogram;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.lang.*;

public class KeysGenerator {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    protected KeysGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);

        KeyPair pair  = keyGen.generateKeyPair();

        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    protected PrivateKey getPrivateKey() {
        return privateKey;
    }

    protected PublicKey getPublicKey() {
        return publicKey;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String encodedPublicKey() {
        byte[] temp = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(temp);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String encodedPrivateKey() {
        byte[] temp = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(temp);
    }

}

