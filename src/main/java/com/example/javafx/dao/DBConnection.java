package com.example.javafx.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class DBConnection {

    private static MongoDatabase database;

    static {

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("java_project");

    }

    public static MongoDatabase getDatabase() {
        return database;
    }
}