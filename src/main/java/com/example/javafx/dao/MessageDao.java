package com.example.javafx.dao;

import com.example.javafx.dao.entities.Message;

import java.util.List;

public interface MessageDao extends Dao<Message, String>{

    List<Message> searchMessageByQuery(String query);
    List<Message> getMessageByUserId(String idReceiver, String idSender);


}
