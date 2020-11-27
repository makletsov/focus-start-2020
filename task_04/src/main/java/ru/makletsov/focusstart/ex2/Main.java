package ru.makletsov.focusstart.ex2;

public class Main {
    private static final String PROPERTIES_FILE_NAME = "conf.properties";
    private static final int PROCESS_TIMEOUT = 10000;

    public static void main(String[] args) {
        PropertiesManager manager = new PropertiesManager(PROPERTIES_FILE_NAME);
        Process process = new Process(manager);

        process.start();

        try {
            Thread.sleep(PROCESS_TIMEOUT);
        } catch (InterruptedException ignored) {
        }

        process.stop();
    }
}
