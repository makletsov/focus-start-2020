package ru.makletsov.focusstart.io.input;

import ru.makletsov.focusstart.io.input.readers.InputReader;

public interface InputFactory {
    InputReader getReader(String readerKey, String sourcePath);
}
