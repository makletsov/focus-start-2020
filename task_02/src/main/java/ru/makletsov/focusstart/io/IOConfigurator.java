package ru.makletsov.focusstart.io;

import ru.makletsov.focusstart.io.input.InputFactory;
import ru.makletsov.focusstart.io.input.readers.InputReader;
import ru.makletsov.focusstart.io.output.OutputFactory;
import ru.makletsov.focusstart.io.output.writers.OutputWriter;

public class IOConfigurator {
    private static final int REQUIRED_ARGUMENTS_COUNT = 2;
    private static final String OUTPUT_SOURCE_PATH = "output.txt";
    private static final String READER_OPTION_KEY = "";

    private final InputReader inputReader;
    private final OutputWriter outputWriter;

    public IOConfigurator(String[] args, InputFactory inputFactory, OutputFactory outputFactory) {
        if (args.length < 2) {
            System.out.println("Incorrect input arguments count: " +
                    REQUIRED_ARGUMENTS_COUNT + " is required, but found " + args.length);
        }

        outputWriter = outputFactory.getWriter(args[0], OUTPUT_SOURCE_PATH);
        inputReader = inputFactory.getReader(READER_OPTION_KEY, args[1]);
    }

    public InputReader getInputReader() {
        return inputReader;
    }

    public OutputWriter getOutputWriter() {
        return outputWriter;
    }
}
