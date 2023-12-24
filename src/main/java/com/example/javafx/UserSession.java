package com.example.javafx;

import com.example.javafx.dao.entities.User;

public final class UserSession {

    private static UserSession instance;
    private static String currentContact;

    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static UserSession getInstace(User user) {
        if(instance == null) {
            instance = new UserSession(user);
        }
        return instance;
        /*instance = new UserSession(user);
        return instance;*/
    }

    public static String getCurrentContact() {
        return currentContact;
    }

    public static void  setCurrentContact(String currentCont) {
        currentContact = currentCont;
    }

    public static void cleanUserSession() {
        user = null;// or null
    }
 public User getCurrentUser(){
        return user;
}
    @Override
    public String toString() {
        return "UserSession{" +
                user.toString() +
                '}';
    }
}