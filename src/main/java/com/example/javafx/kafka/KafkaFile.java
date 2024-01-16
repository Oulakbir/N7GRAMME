package com.example.javafx.kafka;

import javafx.application.Platform;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class KafkaFile {

    private Properties producerProperties;
    private Properties consumerProperties;
    private Producer<String, byte[]> producer;
    private Consumer<String, String> consumer;
    private java.util.function.Consumer<String> messageCallback;

    public KafkaFile() {
        // Initialize and configure Kafka producer properties
        producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "broker-2-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-3-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-5-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-4-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-0-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-1-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093");
        producerProperties.put("security.protocol", "SASL_SSL");
        producerProperties.put("sasl.mechanism", "PLAIN");
        producerProperties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\"0WXauOLhNE_0YJyDC8YvOAiM-kEsnfdula_9Y1Tagvh8\";");
        producerProperties.put("ssl.protocol", "TLSv1.2");
        producerProperties.put("ssl.enabled.protocols", "TLSv1.2");
        producerProperties.put("ssl.endpoint.identification.algorithm", "HTTPS");
        producerProperties.put("key.serializer", StringSerializer.class.getName());
        producerProperties.put("value.serializer", ByteArraySerializer.class.getName());

        // Initialize and configure Kafka consumer properties
        consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", "broker-2-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-3-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-5-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-4-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-0-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093,broker-1-95jpsxj0s9lj40r2.kafka.svc08.us-south.eventstreams.cloud.ibm.com:9093");
        consumerProperties.put("security.protocol", "SASL_SSL");
        consumerProperties.put("sasl.mechanism", "PLAIN");
        consumerProperties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\"0WXauOLhNE_0YJyDC8YvOAiM-kEsnfdula_9Y1Tagvh8\";");
        consumerProperties.put("ssl.protocol", "TLSv1.2");
        consumerProperties.put("ssl.enabled.protocols", "TLSv1.2");
        consumerProperties.put("ssl.endpoint.identification.algorithm", "HTTPS");
        consumerProperties.put("group.id", "group-id-1");
        consumerProperties.put("key.deserializer", StringDeserializer.class.getName());
        consumerProperties.put("value.deserializer", StringDeserializer.class.getName());

        // Initialize KafkaProducer and KafkaConsumer
        producer = new KafkaProducer<>(producerProperties);
        consumer = new KafkaConsumer<>(consumerProperties);
    }

    public boolean sendFile(String topic, String sender, String receiver, byte[] fileBytes) {



            try {
                // Create a ProducerRecord with byte[] as the value type
                ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, sender + receiver, fileBytes);

                // Send the record and wait for acknowledgment
                RecordMetadata metadata = producer.send(record).get();

                // If the metadata is not null, the audio file was sent successfully
                if (metadata != null) {
                    System.out.println("file sent successfully:");
                    System.out.println("Topic: " + metadata.topic());
                    System.out.println("Partition: " + metadata.partition());
                    System.out.println("Offset: " + metadata.offset());
                    return true;
                } else {
                    System.err.println("Failed to send file.");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }



    }

    public boolean sendImage(String topic, String sender, String receiver, byte[] fileBytes) {

        try {
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, sender + receiver, fileBytes);
            RecordMetadata metadata = producer.send(record).get();
            if (metadata != null) {
                System.out.println("Image sent successfully:");
                System.out.println("Topic: " + metadata.topic());
                System.out.println("Partition: " + metadata.partition());
                System.out.println("Offset: " + metadata.offset());
                return true;
            } else {
                System.err.println("Failed to send Image.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }



    }


    public void receiveFiles(String topic) {
        // Create the "files" folder in the working directory
        File folder = new File("files");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Assign partitions directly
        List<TopicPartition> partitions = Arrays.asList(new TopicPartition(topic, 0), new TopicPartition(topic, 1));
        consumer.assign(partitions);

        new Thread(() -> {
            try {
                consumer.seekToBeginning(partitions);

                while (true) {
                    // Poll for records
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                    if (!records.isEmpty()) {
                        System.out.println("New records received");
                    }

                    records.forEach(record -> {
                        String senderReceiver = record.key();
                        String message = record.value();

                        if (message.startsWith("File:")) {

                            // Extract the base64 content excluding the "File:" prefix
                            String base64File = message.substring("File:".length());

                            try {
                                byte[] fileContent = Base64.getDecoder().decode(base64File);

                                // Save the file locally in the "files" folder
                                String fileName = "files/received_file_" + System.currentTimeMillis() + "_" + UUID.randomUUID();
                                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                                    fos.write(fileContent);
                                    System.out.println("File saved locally: " + fileName);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // Use the callback to update the user interface
                                Platform.runLater(() -> {
                                    if (messageCallback != null) {
                                        messageCallback.accept("Received file from " + senderReceiver + ": " + fileName);
                                    }
                                });
                            } catch (IllegalArgumentException e) {
                                // Handle invalid base64 content
                                System.err.println("Invalid base64 content received: " + base64File);
                                e.printStackTrace();
                            }
                        } else {
                            // Handle non-file messages differently or log them
                            System.out.println("Unexpected message format: " + message);
                        }
                    });

                }
            } catch (Exception e) {
                // Handle exceptions, if necessary
                e.printStackTrace();
            }
        }).start();

        // Add another log message to clarify the sequence of events
        System.out.println("File receiving loop started");
    }

    public void setMessageCallback(java.util.function.Consumer<String> callback) {
        this.messageCallback = callback;
    }

    public void closeProducerAndConsumer() {
        // Close the KafkaProducer and KafkaConsumer when done
        producer.close();
        consumer.close();
    }
}
