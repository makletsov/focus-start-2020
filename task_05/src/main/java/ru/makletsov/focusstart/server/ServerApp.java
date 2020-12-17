package ru.makletsov.focusstart.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerApp {
    private static final String PROPERTIES_FILE_NAME = "conf.properties";
    private static final Logger LOG = LoggerFactory.getLogger(ChatServer.class.getSimpleName());

    public static void main(String[] args) {
        try {
            int port = loadPortName();
            UserRepository userRepository = new UserRepository();

            ChatServer server = new ChatServer(port, userRepository);
            server.execute();
        } catch (IOException | NumberFormatException ex) {
            LOG.error("Configuration loading error: ", ex);
        } catch (Exception ex) {
            LOG.error("Unexpected error: ", ex);
        }
    }

    private static int loadPortName() throws IOException {
        Properties properties = new Properties();

        try (InputStream inputStream =
                 ServerApp.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {

             properties.load(inputStream);

             return Integer.parseInt(properties.getProperty("port"));
        }
    }
}
