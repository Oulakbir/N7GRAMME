package com.example.javafx;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class serveur extends Thread {
    private boolean isActive = true;
    private int nombreClient = 0;
    private List<Conversation> clients = new ArrayList<>();

    public static void main(String[] args) {

        new serveur().start();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            while (isActive) {
                Socket socket = serverSocket.accept();
                ++nombreClient;
                Conversation conversation = new Conversation(socket, nombreClient);
                clients.add(conversation);
                conversation.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    class Conversation extends Thread {
        private Socket socketClient;
        private int numero;

        public Conversation(Socket socketClient, int numero) {
            this.socketClient = socketClient;
            this.numero = numero;
        }

        public void broadcastMessage(String message, Socket socket) {

                try {
                    for (Conversation client : clients) {

                            PrintWriter printWriter = new PrintWriter(client.socketClient.getOutputStream(), true);
                            printWriter.println(message);

                    }
                 } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }


        public void run() {
            try {
                InputStream inputStream = socketClient.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);

                PrintWriter pw = new PrintWriter(socketClient.getOutputStream(), true);
                String ipClient = socketClient.getRemoteSocketAddress().toString();
                pw.println("bienvenue");
                System.out.println("Conneion du client num" + numero + " avec IP" + ipClient);

                while (true) {
                    String req = br.readLine();
                    if(req == null) break;
                    broadcastMessage(req, socketClient);

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
