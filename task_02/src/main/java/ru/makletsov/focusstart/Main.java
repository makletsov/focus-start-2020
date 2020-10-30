package ru.makletsov.focusstart;

import ru.makletsov.focusstart.shape.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
    public static final String UNIT = "m";
    public static final String SHAPE_PARAMETERS_DELIMITER = " ";

    public static void main(String[] args) {
        try (ArgsHandler argsHandler = new ArgsHandler(args)) {

            BufferedReader reader = argsHandler.getReader();
            PrintWriter writer = argsHandler.getWriter();

            String shapeType = reader.readLine();
            double[] shapeParameters = Arrays.stream(reader.readLine().split(SHAPE_PARAMETERS_DELIMITER))
                    .mapToDouble(Double::parseDouble)
                    .toArray();

            Shape shape = ShapeCreator.getShape(shapeType, shapeParameters, UNIT);

            writer.println(shape.info());
            writer.flush();
        } catch (IOException e) {
            System.out.println("IO problem occurred: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Cannot parse number from input file. " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Incorrect input data: " + e.getMessage());
        }
    }
}
