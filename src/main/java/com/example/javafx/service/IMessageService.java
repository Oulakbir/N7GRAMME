package com.example.javafx.service;

import com.example.javafx.dao.entities.Message;
import com.example.javafx.dao.entities.User;

import java.util.List;

public interface IMessageService {
    public void addMessage(Message message);
    public void deleteMessageById(String id);
    public List<Message> getAllMessages();
    public void updateMessage(Message message);
    public List<Message> searchMessageByQuery(String query);
    List<Message> getMessageByUserId(String idReceiver, String idSender);


    List<Message> getMessagesForCurrentUserAndContact(String currentUser, String currentContact);
}
