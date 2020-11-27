package ru.makletsov.focusstart;

import ru.makletsov.focusstart.io.input.InputParser;
import ru.makletsov.focusstart.io.input.readers.FileInputReader;
import ru.makletsov.focusstart.io.output.OutputFactory;
import ru.makletsov.focusstart.shape.Shape;
import ru.makletsov.focusstart.shape.ShapeCreator;

import java.util.List;

public class Main {
    private static final int REQUIRED_ARGUMENTS_COUNT = 2;
    private static final String OUTPUT_SOURCE_PATH = "output.txt";
    private static final String SHAPE_PARAMETERS_DELIMITER = " ";

    public static void main(String[] args) {
        try {
            checkArgsLength(args);

            List<String> lines = new FileInputReader(args[1]).getInput();
            InputParser parser = new InputParser(lines, SHAPE_PARAMETERS_DELIMITER);

            String shapeType = parser.getShapeTypeString();
            List<Double> parameters = parser.getParameters();

            Shape shape = ShapeCreator.getShape(shapeType, parameters);

            OutputFactory
                    .getWriter(args[0], OUTPUT_SOURCE_PATH)
                    .write(shape.getInfo());
        } catch (IllegalArgumentException e) {
            System.out.println("Incorrect input data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Something unexpected has happened. Connect the support service!");
        }
    }

    private static void checkArgsLength(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Incorrect input arguments count: " +
                    REQUIRED_ARGUMENTS_COUNT + " is required, but found " + args.length);
        }
    }
}
