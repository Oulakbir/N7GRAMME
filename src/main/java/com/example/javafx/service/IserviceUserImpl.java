package com.example.javafx.service;

import com.example.javafx.dao.DBConnection;
import com.example.javafx.dao.UserDao;
import com.example.javafx.dao.entities.User;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.List;

public class IserviceUserImpl implements IUserService{
    DBConnection database = new DBConnection();
    MongoCollection<Document> collection = DBConnection.getDatabase().getCollection("users");
    UserDao userDao;

    public IserviceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User user) {
        userDao.save(user);
    }

    @Override
    public void deleteUserById(String id) {
        userDao.removeById(id);
    }

    @Override
    public User getUserById(String id) {
        return userDao.getById(id);
    }
    @Override
    public User getUserByEmail(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        if (doc != null) {
            User user = new User();
            user.setUser_id(doc.getString("user_id"));
            user.setEmail(doc.getString("email"));
            user.setNom("nom");
            return user;
        }
        return null;
    }
    @Override
    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    @Override
    public void updateUser(User user) {
     userDao.update(user);
    }

    @Override
    public List<User> searchUserByQuery(String query) {
        return userDao.searchUsersByQuery(query);
    }

    @Override
    public User login(String email, String password) {
        return userDao.login(email, password);
    }

    @Override
    public String emailExists(String email) {
        return userDao.emailExists(email);
    }

    @Override
    public void updateImage(String userId, byte[] newImageData) {
        userDao.updateImage(userId, newImageData);
    }

    @Override
    public void addContact(String userId, String contactId) {
        userDao.addContact( userId,  contactId);
    }


}
