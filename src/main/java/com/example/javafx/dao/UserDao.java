package com.example.javafx.dao;

import com.example.javafx.dao.entities.User;

import java.util.List;

public interface UserDao extends Dao<User, String>{
    List<User> searchUsersByQuery(String query);
    User login(String email, String password);

    String emailExists(String email);

    void updateImage(String userId, byte[] newImageData);




    void addContact(String userId, String contactId);
}
