package com.example.javafx.service;

import com.example.javafx.dao.MessageDaoImpl;
import com.example.javafx.dao.UserDaoImpl;
import com.example.javafx.dao.entities.Message;
import com.example.javafx.dao.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {
        IMessageService service = new IServiceMessageImpl(new MessageDaoImpl());

        IUserService userService = new IserviceUserImpl(new UserDaoImpl());
      /*  Message message=new Message();
        message.setContent("Hello rachida");
        message.setSender("rechida");
        message.setReceiver("Aafaf");

        service.addMessage(message);*/

        /*List<Message> messages = service.getAllMessages();
        for(Message msg:messages){
            System.out.println(msg.getContent());
        }
        User user=new User();
        user.setNom("hi ");
        user.setPassword("0000");
        user.setEmail("tanassat@gmail.com");
        user.setContacts(new String[]{"f68c750c-687b-4c3a-98d9-5fd8a681eaa5", "f68c750c-687b-4c3a-98d9-5fd8a681eaa5"});

        userService.addUser(user);

        User user1 = userService.login("rachida@gmail.com", "123");
        System.out.println(user1.getNom());
        User user2 = userService.getUserById("f68c750c-687b-4c3a-98d9-5fd8a681eaa5");
        String contact_ids[] = user2.getContacts();
        if (contact_ids != null) {
            for (String id : contact_ids) {
                User contact = userService.getUserById(id);
                if (contact != null) {
                    System.out.println(contact.getNom());
                } else {
                    System.out.println("User with ID " + id + " not found.");
                }
            }

        }*/

       /* List<User> users = userService.searchUserByQuery("rachida");

        for (User user:
             users) {
            System.out.println(user.getNom());

        }*/

        List<Message> messages = service.getMessageByUserId("af4e854e-739b-4e5f-babe-eabab8e9318d","83c2f609-58c9-4716-a234-32a16da242e3");
        System.out.println(messages);
    }

}
