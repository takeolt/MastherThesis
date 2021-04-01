package com.example.redooffprogram;

import java.security.PrivateKey;
import java.security.PublicKey;

public class UserInfo {
    protected String email, password, id;
    protected PrivateKey privateKey;
    protected PublicKey publicKey;

    public UserInfo(String email, String password, PrivateKey privateKey, PublicKey publicKey, String id) {
        this.email = email;
        this.password = password;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPrivateKey() {
        return privateKey.toString();
    }

    public String getPublicKey() {
        return publicKey.toString();
    }

    public String getId() {
        return id;
    }
}
