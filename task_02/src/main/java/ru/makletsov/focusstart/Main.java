package ru.makletsov.focusstart;

import ru.makletsov.focusstart.io.IOConfigurator;
import ru.makletsov.focusstart.io.input.DefaultInputFactory;
import ru.makletsov.focusstart.io.input.readers.InputReader;
import ru.makletsov.focusstart.io.output.DefaultOutputFactory;
import ru.makletsov.focusstart.io.output.writers.OutputWriter;
import ru.makletsov.focusstart.shape.Shape;
import ru.makletsov.focusstart.shape.ShapeCreator;

public class Main {
    private static final String UNIT = "m";

    public static void main(String[] args) {
        try {
            IOConfigurator argsHandler = new IOConfigurator(args, new DefaultInputFactory(), new DefaultOutputFactory());

            InputReader inputReader = argsHandler.getInputReader();
            OutputWriter outputWriter = argsHandler.getOutputWriter();

            Shape shape = ShapeCreator.getShape(inputReader.getShapeTypeString(), inputReader.getParameters(), UNIT);

            outputWriter.writeToOutput(shape.getInfo());
        } catch (IllegalArgumentException e) {
            System.out.println("Incorrect input data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Something unexpected has happened. Connect the support service!");
        }
    }
}
