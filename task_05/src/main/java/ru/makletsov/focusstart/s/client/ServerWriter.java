package ru.makletsov.focusstart.s.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerWriter implements Runnable {
    private PrintWriter writer;
    private final Socket socket;
    private final ChatClient client;

    public ServerWriter(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        try (socket) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter your name: ");
            String userName = scanner.nextLine();
            client.setUserName(userName);
            writer.println(userName);

            String text = "";

            do {
                if (scanner.hasNextLine()) {
                    text = scanner.nextLine().trim();
                    writer.println(text);
                }
            } while (!text.equals("quit") && !Thread.currentThread().isInterrupted());
        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}