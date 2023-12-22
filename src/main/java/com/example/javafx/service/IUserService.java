package com.example.javafx.service;

import com.example.javafx.dao.entities.User;

import java.util.List;

public interface IUserService {

    public void addUser(User user);
    public void deleteUserById(String id);
    public User getUserById(String id );
    User getUserByEmail(String email);
    public List<User> getAllUsers();
    public void updateUser(User user);
    public List<User> searchUserByQuery(String query);
    public User login(String email, String password);
    public String emailExists(String email);
    void updateImage(String userId, byte[] newImageData);


    void addContact(String userId, String contactId);
}
