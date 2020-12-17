package ru.makletsov.focusstart.s.server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class UserHandler implements Runnable {
    private final Socket socket;
    private final ChatServer server;
    private PrintWriter writer;
    private String userName;

    public UserHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try (socket) {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            userName = reader.readLine();

            while (server.getUserNames().contains(userName)) {
                writer.println("This name has been already taken! Choose another name:");
                userName = reader.readLine();
            }

            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            System.out.println(serverMessage);
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                System.out.println(clientMessage);
                serverMessage = LocalDateTime.now() + " [" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("quit".trim()));

            server.removeUser(userName, this);

            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);
        } catch (IOException ex) {
            System.out.printf("Error in user handler for user %s: %s", userName, ex.getMessage());
            //ex.printStackTrace();  //TODO logger???
        }
    }

    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}