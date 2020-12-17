package ru.makletsov.focusstart.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ChatServer {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServer.class.getSimpleName());

    private final int port;
    private final UserRepository userRepository;

    private final Set<UserHandler> userHandlers;
    private final ThreadFactory threadFactory;

    public ChatServer(int port, UserRepository userRepository) {
        this.port = port;
        this.userRepository = userRepository;

        userHandlers = ConcurrentHashMap.newKeySet();
        threadFactory = Executors.defaultThreadFactory();
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            LOG.info("Chat Server is listening on port " + port + "... ");

            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocket.accept();
                LOG.trace("New user has been connected.");

                UserHandler newHandler = new UserHandler(socket, this);
                userHandlers.add(newHandler);

                threadFactory.newThread(newHandler).start();
            }

        } catch (IOException ex) {
            LOG.error("Internal server error: ", ex);
        }
    }

    void broadcast(String message, UserHandler excludeUser) {
        for (UserHandler userHandler : userHandlers) {
            if (userHandler != excludeUser) {
                userHandler.sendMessage(message);
            }
        }
    }

    void utilizeHandler(String userName, UserHandler userHandler) {
        boolean removed = userRepository.removeUser(userName);

        if (removed) {
            userHandlers.remove(userHandler);
        }
    }

    UserRepository getUserRepository() {
        return userRepository;
    }
}