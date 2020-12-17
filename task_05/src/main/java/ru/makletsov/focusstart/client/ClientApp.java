package ru.makletsov.focusstart.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.makletsov.focusstart.server.ChatServer;
import ru.makletsov.focusstart.server.ServerApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientApp {
    private static final String PROPERTIES_FILE_NAME = "conf.properties";
    private static final Logger LOG = LoggerFactory.getLogger(ChatServer.class.getSimpleName());

    public static void main(String[] args) {
        try {
            Properties properties = loadProperties();

            String hostname = properties.getProperty("host");
            int port = Integer.parseInt(properties.getProperty("port"));

            ChatClient client = new ChatClient(hostname, port, System.in, System.out);
            client.execute();
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Cannot load properties from " + PROPERTIES_FILE_NAME);
            LOG.error("Configuration loading error: ", ex);
        } catch (Exception ex) {
            System.out.println("Execution process ends exceptionally.");
            LOG.error("Execution aborted: ", ex);
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        try (InputStream inputStream =
                 ServerApp.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {

            properties.load(inputStream);

            return properties;
        }
    }
}
