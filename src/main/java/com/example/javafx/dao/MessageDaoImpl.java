package com.example.javafx.dao;

import com.example.javafx.dao.entities.Message;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class MessageDaoImpl implements MessageDao{
    MongoCollection<Document> collection = DBConnection.getDatabase().getCollection("messages");

    @Override
    public void save(Message o) {
        UUID id = UUID.randomUUID();
        String messageId = id.toString();
        Document message = new Document("_id", new ObjectId())
                .append("message_id", messageId)
                .append("content", o.getContent())
                .append("sender", o.getSender())
                .append("receiver", o.getReceiver())
                .append("date", new Date())
                .append("isRead", o.getRead());

         collection.insertOne(message);

    }

    @Override
    public void removeById(String id) {
        Bson query = eq("message_id", id);
        collection.deleteOne(query);

    }

    @Override
    public Message getById(String id) {

        Message message = null;

        Document doc = collection.find(Filters.eq("message_id", id)).first();
        if(doc != null){
            message = new Message();
            message.setContent(doc.getString("content"));
            message.setSender(doc.getString("sender"));
            message.setReceiver(doc.getString("receiver"));
            message.setDate(doc.getDate("date"));
            message.setRead(doc.getBoolean("isread"));
        }

        return message;
    }

    @Override
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        try(MongoCursor<Document> cursor = collection.find().iterator())
        {
            while(cursor.hasNext()) {
                Document contenu = cursor.next();
                Message message = new Message();
                message.setContent(contenu.getString("content"));
                message.setSender(contenu.getString("sender"));
                message.setReceiver(contenu.getString("receiver"));
                message.setDate(contenu.getDate("date"));
                message.setRead(contenu.getBoolean("isread"));

               messages.add(message);
            }
        }
        return messages;
    }

    @Override
    public void update(Message o) {

        Bson query  = Filters.eq("message_id", o.getMessage_id());
        Bson updates  = Updates.combine(
                Updates.set("content", o.getContent()),
                Updates.set("sender", o.getSender()),
                Updates.set("receiver", o.getReceiver()),
                Updates.set("date", new Date()),
                Updates.set("isRead", o.getRead()));
                collection.updateOne(query, updates);
    }

    @Override
    public List<Message> searchMessageByQuery(String query) {
        return null;
    }

    @Override
    public List<Message> getMessageByUserId(String idReceiver, String idSender) {

            List<Message> messages = new ArrayList<>();

            try (MongoCursor<Document> cursor = collection.find(Filters.and(
                    Filters.eq("receiver", idReceiver),
                    Filters.eq("sender", idSender)
            )).iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    Message message = new Message();
                    message.setMessage_id(doc.getString("message_id"));
                    message.setContent(doc.getString("content"));
                    message.setSender(doc.getString("sender"));
                    message.setReceiver(doc.getString("receiver"));
                    message.setRead(doc.getBoolean("isRead"));
                    message.setDate(doc.getDate("date"));

                    messages.add(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return messages;
        }



    }
