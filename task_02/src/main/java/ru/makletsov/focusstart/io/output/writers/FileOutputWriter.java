package ru.makletsov.focusstart.io.output.writers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileOutputWriter implements OutputWriter {
    private final Path filePath;

    public FileOutputWriter(String pathString) {
        filePath = Path.of(pathString);
    }

    @Override
    public void write(String outputString) {
        try {
            Files.write(filePath, List.of(outputString));
        } catch (IOException e) {
            IllegalArgumentException newException = new IllegalArgumentException("Cannot write data to output file.");
            newException.addSuppressed(e);

            throw newException;
        }
    }
}
