package com.nipa.chatgptbyapi.model;

public class ChatModel {
    boolean chatByMe=true;

    public boolean isChatByMe() {
        return chatByMe;
    }

    public void setChatByMe(boolean chatByMe) {
        this.chatByMe = chatByMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;
}
