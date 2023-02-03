package com.example.firstapp;

public class Message {

    private String message;
    private String senderMobile;
    private String reMobile;

    private String msgId;
    public Message(){

    }

    public Message(String message, String senderMobile, String reMobile, String msgId) {
        this.message = message;
        this.senderMobile = senderMobile;
        this.reMobile = reMobile;
        this.msgId = msgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public void setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
    }

    public String getReMobile() {
        return reMobile;
    }

    public void setReMobile(String reMobile) {
        this.reMobile = reMobile;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

