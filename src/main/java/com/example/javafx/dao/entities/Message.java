package com.example.javafx.dao.entities;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String message_id;
    private String content;
    private String sender;
    private String receiver;
    private Boolean isRead;
    private Date date;

    public Message() {
    }

    public Message(String content, String sender, String receiver, Boolean isRead, Date date) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.isRead = isRead;
        this.date = date;
    }

    public String getMessage_id() {
        return message_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
