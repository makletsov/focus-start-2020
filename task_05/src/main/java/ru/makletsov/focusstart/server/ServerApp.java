package ru.makletsov.focusstart.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerApp {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServer.class.getSimpleName());

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            return;
        }

        int port = Integer.parseInt(args[0]);           //TODO read from config, number format exception
        UserRepository userRepository = new UserRepository();

        ChatServer server = new ChatServer(port, userRepository);
        server.execute();
    }
}
