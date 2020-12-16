package ru.makletsov.focusstart.io.input.readers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileInputReader implements InputReader {
    private final String pathString;

    public FileInputReader(String pathString) {
        this.pathString = pathString;
    }

    public List<String> getInput() {
        Path inputDataPath = Path.of(pathString);

        try (Stream<String> lines = Files.lines(inputDataPath)) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            IllegalArgumentException newException = new IllegalArgumentException("Cannot read the input file.");
            newException.addSuppressed(e);

            throw newException;
        }
    }
}
