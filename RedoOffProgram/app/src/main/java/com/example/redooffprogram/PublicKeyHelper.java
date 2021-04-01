package com.example.redooffprogram;

public class PublicKeyHelper {

    String email, publicKey;

    public PublicKeyHelper(String email, String publicKey) {
        this.email = email;
        this.publicKey = publicKey;

    }

    public String getEmail() {
        return email;
    }

    public String getPublicKey() {
        return publicKey;
    }


}
