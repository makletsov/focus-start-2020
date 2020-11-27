package ru.makletsov.focusstart.ex2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
    private static final String STRING_PLACEHOLDER = "%s";
    private static final String CANNOT_READ_MESSAGE_TEMPLATE = "Cannot read "+ STRING_PLACEHOLDER + " from input file";
    private static final String LOAD_ERROR_MESSAGE = "Cannot load properties from input file.";

    private final Properties properties;

    public PropertiesManager(String path) {
        this.properties = new Properties();

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw getWrapperException(e, LOAD_ERROR_MESSAGE);
        }
    }

    public int getConsumersCount() {
        try {
            return Integer.parseInt(properties.getProperty("consumers"));
        } catch (NumberFormatException e) {
            throw getCannotReadWrapperException(e, "consumers count");
        }
    }

    public int getConsumersTimeout() {
        try {
            return Integer.parseInt(properties.getProperty("consumersTimeout"));
        } catch (NumberFormatException e) {
            throw getCannotReadWrapperException(e, "consumers timeout");
        }
    }

    public int getProducersCount() {
        try {
            return Integer.parseInt(properties.getProperty("producers"));
        } catch (NumberFormatException e) {
            throw getCannotReadWrapperException(e, "producers count");
        }
    }

    public int getProducersTimeout() {
        try {
            return Integer.parseInt(properties.getProperty("producersTimeout"));
        } catch (NumberFormatException e) {
            throw getCannotReadWrapperException(e, "producers timeout");
        }
    }

    public int getStorageCapacity() {
        try {
            return Integer.parseInt(properties.getProperty("storageCapacity"));
        } catch (NumberFormatException e) {
            throw getCannotReadWrapperException(e, "storage capacity");
        }
    }

    public RuntimeException getCannotReadWrapperException(Exception e, String propertyName) {
        String message = String.format(CANNOT_READ_MESSAGE_TEMPLATE, propertyName);
        return getWrapperException(e, message);
    }

    public RuntimeException getWrapperException(Exception wrapped, String message) {
        RuntimeException wrapperException = new RuntimeException(message);
        wrapperException.addSuppressed(wrapped);

        return wrapperException;
    }
}
