package ru.makletsov.focusstart.io.input;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputParser {
    private final String shapeTypeString;
    private final List<Double> shapeParameters;
    private final String delimiter;

    public InputParser(List<String> lines, String delimiter) {
        if (lines.size() < 2) {
            throw new IllegalArgumentException("Wrong format of the input data.");
        }

        this.delimiter = delimiter;

        shapeTypeString = lines.get(0);
        shapeParameters = getParameters(lines.get(1));
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
    public String getShapeTypeString() {
        return shapeTypeString;
    }

    public List<Double> getParameters() {
        return shapeParameters;
    }
}
