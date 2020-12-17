package ru.makletsov.focusstart.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerReader implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(ServerReader.class.getSimpleName());

    private final BufferedReader reader;
    private final PrintStream userOutput;

    public ServerReader(Socket socket, PrintStream userOutput) {
        this.userOutput = userOutput;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            userOutput.println("Reading from server is unavailable.");
            LOG.error("Error getting input stream: " + ex.getMessage());

            throw new RuntimeException(ex);
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();

                if (response == null) {
                    System.out.println("Connection has been aborted. Try again.");
                    return;
                }

                userOutput.println(response);
            } catch (SocketException ex) {
                userOutput.println("Socket has been closed exceptionally.");
                LOG.error("Socket has been closed: ", ex);
                return;
            } catch (IOException ex) {
                userOutput.println("Cannot reading information from server.");
                LOG.error("Error reading from server: ", ex);
                return;
            }
        }
    }
}