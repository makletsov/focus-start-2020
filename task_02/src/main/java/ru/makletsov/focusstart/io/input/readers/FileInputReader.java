package ru.makletsov.focusstart.io.input.readers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileInputReader implements InputReader {
    private final String shapeTypeString;
    private final List<Double> shapeParameters;
    private final String delimiter;

    public FileInputReader(String pathString, String parametersDelimiter) {
        this.delimiter = parametersDelimiter;

        List<String> lines = getFileLines(pathString);

        if (lines.size() < 2) {
            throw new IllegalArgumentException("Wrong format of the input data.");
        }

        shapeTypeString = lines.get(0);
        shapeParameters = getParameters(lines.get(1));
    }

    private List<String> getFileLines(String pathString) {
        try {
            return Files.lines(Path.of(pathString)).collect(Collectors.toList());
        } catch (IOException e) {
            IllegalArgumentException newException = new IllegalArgumentException("Cannot read the input file.");
            newException.addSuppressed(e);

            throw newException;
        }
    }

    private List<Double> getParameters(String parametersString) {
        try {
            return Stream.of(parametersString.split(delimiter))
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Incorrect input shape parameters.");
        }
    }

    @Override
    public String getShapeTypeString() {
        return shapeTypeString;
    }

    @Override
    public List<Double> getParameters() {
        return shapeParameters;
    }
}
