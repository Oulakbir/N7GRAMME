package com.example.javafx.dao;

import com.example.javafx.dao.entities.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import static com.mongodb.client.model.Filters.eq;

public class UserDaoImpl implements UserDao{

    DBConnection database = new DBConnection();
    MongoCollection<Document> collection = DBConnection.getDatabase().getCollection("users");

    @Override
    public void save(User o) {
        UUID id = UUID.randomUUID();
        String userId = id.toString();
       String hashedPassword = BCrypt.hashpw(o.getPassword(), BCrypt.gensalt());
        byte[] imageBytes;
        try {
            Path path = Paths.get(o.getImage());
            imageBytes = Files.readAllBytes(path);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return;
        }

        Binary imageBinary = new Binary(imageBytes);

        Document user = new Document("_id", new ObjectId())
                .append("user_id", userId)
                .append("nom", o.getNom())
                .append("password", hashedPassword)
                .append("email", o.getEmail())
                .append("image", imageBinary);

        collection.insertOne(user);
    }

    @Override
    public void removeById(String id) {
        Bson query = eq("user_id", id);
        collection.deleteOne(query);
    }

    @Override
    public User getById(String id) {
        User user = null;

        Document doc = collection.find(Filters.eq("user_id", id)).first();
        if(doc != null){
            user = new User();
            user.setUser_id(doc.getString("user_id"));
            user.setNom(doc.getString("nom"));
            user.setPassword(doc.getString("password"));
            user.setEmail(doc.getString("email"));
            byte[] imageData = doc.get("image", Binary.class).getData();
            user.setImageData(imageData);
            List<String> contactsList = doc.getList("contact", String.class);
            if (contactsList != null) {
                user.setContacts(contactsList.toArray(new String[0]));
            }
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try(MongoCursor<Document> cursor = collection.find().iterator())
        {
            while(cursor.hasNext()) {
                Document contenu = cursor.next();
                User user = new User();
                user.setUser_id(contenu.getString("user_id"));
                user.setNom(contenu.getString("nom"));
                user.setPassword(contenu.getString("password"));
                user.setEmail(contenu.getString("email"));
                byte[] imageData = contenu.get("image", Binary.class).getData();
                user.setImageData(imageData);
                List<String> contactsList = contenu.getList("contact", String.class);
                if (contactsList != null) {
                    user.setContacts(contactsList.toArray(new String[0]));
                }


                users.add(user);
            }
        }
        return users;
    }

    @Override
    public void update(User o) {
        Bson query  = Filters.eq("user_id", o.getUser_id());
        Bson updates  = Updates.combine(
                Updates.set("nom", o.getNom()),
                Updates.set("password", o.getPassword()),
                Updates.set("email", o.getEmail()),
                Updates.set("contact", Arrays.asList(o.getContacts())));


        collection.updateOne(query, updates);

    }

    @Override
    public List<User> searchUsersByQuery(String query) {
        List<User> users = new ArrayList<>();

        try {
            Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
            Bson filter = Filters.or(
                    Filters.regex("user_id", pattern),
                    Filters.regex("nom", pattern),
                    Filters.regex("password", pattern),
                    Filters.regex("email", pattern));

            try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
                while (cursor.hasNext()) {
                    Document contenu = cursor.next();
                    User user = new User();
                    user.setUser_id(contenu.getString("user_id"));
                    user.setNom(contenu.getString("nom"));
                    user.setPassword(contenu.getString("password"));
                    user.setEmail(contenu.getString("email"));
                    List<String> contactsList = contenu.getList("contacts", String.class);
                    if (contactsList != null) {
                        user.setContacts(contactsList.toArray(new String[0]));
                    }

                    users.add(user);
                }
            }
        } catch (Exception e) {
            // Handle specific exceptions or log them based on your application needs
            e.printStackTrace();
        }

        return users;
    }

    @Override
     public User login(String email, String password) {
        User user = null;
        Document doc = collection.find(Filters.eq("email", email)).first();

        if (doc != null) {
            // Get the hashed password from the document
            String hashedPassword = doc.getString("password");

            // Verify the entered password against the hashed password
            if (BCrypt.checkpw(password, hashedPassword)) {
                user = new User();
                user.setUser_id(doc.getString("user_id"));
                user.setNom(doc.getString("nom"));
                user.setPassword(doc.getString("password"));
                user.setEmail(doc.getString("email"));

                byte[] imageData = doc.get("image", Binary.class).getData();
                user.setImageData(imageData);

                List<String> contactsList = doc.getList("contact", String.class);
                if (contactsList != null) {
                    user.setContacts(contactsList.toArray(new String[0]));
                }
            }
        }

        return user;
    }



    @Override
    public String emailExists(String email) {

        Document doc = collection.find(Filters.eq("email", email)).first();
        if(doc != null) return doc.getString("user_id");
        else return null;
    }

    @Override
    public void updateImage(String userId, byte[] newImageData) {
        Bson filter = Filters.eq("user_id", userId);
        Bson update = Updates.set("image", new Binary(newImageData));
        collection.updateOne(filter, update);
    }

    @Override
    public void addContact(String userId, String contactId) {
        try {

            Document userDoc = collection.find(Filters.eq("user_id", userId)).first();

            if (userDoc != null) {
                List<String> contacts = userDoc.getList("contact", String.class, new ArrayList<>());
                contacts.add(contactId);
                collection.updateOne(
                        Filters.eq("user_id", userId),
                        Updates.set("contact", contacts),
                        new UpdateOptions().upsert(true)
                );
                System.out.println("Contact added successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
