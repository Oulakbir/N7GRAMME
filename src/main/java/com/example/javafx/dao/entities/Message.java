package com.example.javafx.dao.entities;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String message_id;
    private String content;
    private String sender;
    private String receiver;
    private Boolean isRead;
    private Date date;
    private String fileName;
    private File attachedFile;

    private byte[] fileContent;

    public File getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(File attachedFile) {
        this.attachedFile = attachedFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Message() {
    }

    public Message(String content, String sender, String receiver, Boolean isRead, Date date) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.isRead = isRead;
        this.date = date;
    }

    public Message(String sender, String receiver, String fileName, byte[] fileData, boolean isRead, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.fileName = fileName;
        this.fileContent = fileData;
        this.isRead = isRead;
        this.date = date;
    }
    public Message(String sender, String receiver, String fileName, File file, boolean isRead, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.fileName = fileName;
        this.attachedFile = file;
        this.isRead = isRead;
        this.date = date;
    }
    public void setMessage_id(String message_id) {
        this.message_id = message_id;
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

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
