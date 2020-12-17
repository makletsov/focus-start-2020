package ru.makletsov.focusstart.s.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ChatServer {
    private final int port;

    private final Set<String> userNames;
    private final Set<UserHandler> userHandlers;
    private final ThreadFactory threadFactory;

    public ChatServer(int port) {
        this.port = port;

        userNames = ConcurrentHashMap.newKeySet();
        userHandlers = ConcurrentHashMap.newKeySet();
        threadFactory = Executors.defaultThreadFactory();
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port + "... ");

            while (true) {
                Socket socket = serverSocket.accept();

                UserHandler newHandler = new UserHandler(socket, this);
                userHandlers.add(newHandler);

                threadFactory.newThread(newHandler).start();
            }

        } catch (IOException ex) {
            System.out.printf("Internal server error: %s", ex.getMessage());
            //ex.printStackTrace();  //TODO logger???
        }
    }

    void broadcast(String message, UserHandler excludeUser) {
        for (UserHandler userHandler : userHandlers) {
            if (userHandler != excludeUser) {
                userHandler.sendMessage(message);
            }
        }
    }

    void addUserName(String userName) {
        userNames.add(userName);
    }

    void removeUser(String userName, UserHandler aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userHandlers.remove(aUser);
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}