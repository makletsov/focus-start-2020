package ru.makletsov.focusstart.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ServerReader implements Runnable {
    private BufferedReader reader;
    private final Socket socket;
    private final ChatClient client;

    public ServerReader(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();

                if (response == null) {
                    System.out.println("Disconnected...");
                    return;
                }

                System.out.println(response);
            } catch (SocketException ex) {
                System.out.println("Socket has been closed: " + ex.getMessage());
                return;
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}