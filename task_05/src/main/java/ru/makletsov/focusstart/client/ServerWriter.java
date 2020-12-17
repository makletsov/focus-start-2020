package ru.makletsov.focusstart.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerWriter implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ServerWriter.class.getSimpleName());

    private final PrintWriter writer;
    private final Socket socket;
    private final InputStream userInput;
    private final PrintStream userOutput;

    public ServerWriter(Socket socket, InputStream userInput, PrintStream userOutput) {
        this.socket = socket;
        this.userInput = userInput;
        this.userOutput = userOutput;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            LOG.error("Error getting output stream: ", ex);
            throw new RuntimeException(ex);
        }
    }

    public void run() {
        try (socket) {
            Scanner scanner = new Scanner(userInput);

            userOutput.println("Enter your name: ");
            String userName = scanner.nextLine();

            writer.println(userName);
            userOutput.println("Start writing!)");

            String text = "";

            do {
                text = scanner.nextLine().trim();
                writer.println(text);
            } while (!text.equals("quit"));
        } catch (Exception ex) {
            userOutput.println("Error writing to server: " + ex.getMessage());
            LOG.error("Error writing to server: ", ex);
        }
    }
}