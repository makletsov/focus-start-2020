package ru.makletsov.focusstart.s.server;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            return;
        }

        int port = Integer.parseInt(args[0]);           //TODO read from config, number format exception

        ChatServer server = new ChatServer(port);
        server.execute();
    }
}
