package ru.makletsov.focusstart.io.input;

import ru.makletsov.focusstart.io.input.readers.InputReader;
import ru.makletsov.focusstart.io.input.readers.FileInputReader;

public class DefaultInputFactory implements InputFactory {
    public static final String SHAPE_PARAMETERS_DELIMITER = " ";

    @Override
    public InputReader getReader(String readerKey, String sourcePath) {
        return new FileInputReader(sourcePath, SHAPE_PARAMETERS_DELIMITER);
    }
}
