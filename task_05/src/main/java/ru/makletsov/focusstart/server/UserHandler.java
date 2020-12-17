package ru.makletsov.focusstart.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class UserHandler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(UserHandler.class.getSimpleName());
    private static final String MESSAGE_TEMPLATE = "%s [%s]: %s";

    private String userName;

    private final Socket socket;
    private final ChatServer server;
    private final DateTimeFormatter dateFormat;
    private final UserRepository userRepository;
    private final StringBuilder stringBuilder;

    private PrintWriter writer;
    private BufferedReader reader;

    public UserHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        this.userRepository = server.getUserRepository();

        dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        stringBuilder = new StringBuilder();
    }

    public void run() {
        try (socket) {
            initializeIOStreams();

            printExistingUsers();

            userName = reader.readLine();

            if (isSuchUserExists()) {
                writer.println("User with this name is already exist! Try with another name.");
                LOG.info("Failed to connect with user " + userName + ". Repeatable user name.");
                return;
            }

            userRepository.addUserName(userName);

            sendNewUserCreationNotification();

            handleUserInteraction(reader);

            utilizeUserConnection();
        } catch (Exception ex) {
            tryUtilizeUserHandlerExceptionally();
            LOG.error("Error in user handler for user " + userName, ex);
        }
    }

    private void initializeIOStreams() throws IOException {
        InputStream input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));

        OutputStream output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
    }

    private void tryUtilizeUserHandlerExceptionally() {
        try {
            utilizeUserConnection();
        } catch (Exception ex) {
            server.utilizeHandler(userName, this);
            LOG.error("User handler utilizing failed: ", ex);
        }
    }

    private void utilizeUserConnection() {
        server.utilizeHandler(userName, this);

        String serverMessage = userName + " has quited.";

        LOG.trace(serverMessage);
        server.broadcast(serverMessage, this);
    }

    private void handleUserInteraction(BufferedReader reader) throws IOException {
        String serverMessage;
        String clientMessage;

        do {
            clientMessage = reader.readLine();
            serverMessage = formatMessage(clientMessage);

            LOG.trace(serverMessage);
            server.broadcast(serverMessage, this);

        } while (!clientMessage.equals("quit".trim()));
    }

    private void sendNewUserCreationNotification() {
        String serverMessage = "New user connected: " + userName;

        LOG.info(serverMessage);
        server.broadcast(serverMessage, this);
    }

    private boolean isSuchUserExists() {
        return userRepository.getUserNames().contains(userName);
    }

    private String formatMessage(String clientMessage) {
        stringBuilder.setLength(0);

        stringBuilder.append(String.format(MESSAGE_TEMPLATE, getDate(), userName, clientMessage));

        return stringBuilder.toString();
    }


    private String getDate() {
        return dateFormat.format(LocalDateTime.now());
    }

    private void printExistingUsers() {
        if (userRepository.hasUsers()) {
            writer.println("Connected users: " + userRepository.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}