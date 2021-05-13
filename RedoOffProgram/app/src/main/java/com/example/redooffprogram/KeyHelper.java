package com.example.redooffprogram;

public class KeyHelper {

    String id, email, publicKey, privateKey;

    public KeyHelper(String id, String email, String publicKey, String privateKey) {
        this.id = id;
        this.email = email;
        this.publicKey = publicKey;
        this.privateKey = privateKey;

    }

    public KeyHelper(String id) {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getEmail() {
        return email;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getId() {
        return id;
    }
}
