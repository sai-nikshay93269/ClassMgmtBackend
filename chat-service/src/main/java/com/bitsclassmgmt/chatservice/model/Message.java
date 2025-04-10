package com.bitsclassmgmt.chatservice.model;

public class Message {
    private String message;

    public Message() {
        // Required for Jackson
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}