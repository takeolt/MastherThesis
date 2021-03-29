package com.example.redooffprogram;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

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

}

