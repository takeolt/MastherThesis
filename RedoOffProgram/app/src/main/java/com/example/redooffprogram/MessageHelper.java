package com.example.redooffprogram;

public class MessageHelper {

    String message;
    String receiver;
    String sender;
    String subject;
    String encryptedKey;

    public MessageHelper(String email, String message) {
        this.receiver = email;
        this.message = message;
    }

    public MessageHelper(String message, String email, String sender, String subject, String encryptedKey){
        this.message = message;
        this.receiver = email;
        this.sender = sender;
        this.subject = subject;
        this.encryptedKey = encryptedKey;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }
}
