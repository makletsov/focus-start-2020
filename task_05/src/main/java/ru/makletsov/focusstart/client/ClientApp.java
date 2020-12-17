package ru.makletsov.focusstart.client;

public class ClientApp {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("No args");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}
