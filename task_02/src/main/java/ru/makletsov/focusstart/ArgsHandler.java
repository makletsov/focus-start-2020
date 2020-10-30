package ru.makletsov.focusstart;

import java.io.*;

public class ArgsHandler implements AutoCloseable {
    private static final String OUTPUT_FILE_NAME = "output.txt";
    private static final String CONSOLE_OUTPUT_KEY = "c";
    private static final String FILE_OUTPUT_KEY = "f";

    private final PrintWriter writer;
    private final BufferedReader reader;
    private final boolean needWriterClosure;

    public ArgsHandler(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("To less input arguments have been given.");
        }

        switch (args[0]) {
            case CONSOLE_OUTPUT_KEY:
                needWriterClosure = false;
                writer = new PrintWriter(System.out);
                break;
            case FILE_OUTPUT_KEY:
                try {
                    needWriterClosure = true;
                    writer = new PrintWriter(OUTPUT_FILE_NAME);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("File cannot be created \"" + OUTPUT_FILE_NAME + "\".");
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect output source option - \"" + args[0] + "\".");
        }

        String fileName = args[1];
        File file = new File(fileName);

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found \"" + fileName + "\".");
        }
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    @Override
    public void close() {
        try {
            if (needWriterClosure) {
                writer.close();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot close some IO resource.");
        }
    }
}
