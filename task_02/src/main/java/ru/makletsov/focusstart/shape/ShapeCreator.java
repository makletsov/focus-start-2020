package ru.makletsov.focusstart.shape;

import java.util.List;

import static ru.makletsov.focusstart.shape.ShapeType.*;

public class ShapeCreator {
    public static Shape getShape(String inputString, List<Double> parameters) {
        ShapeType shapeType = ShapeType.get(inputString);

        checkParametersCount(shapeType, parameters);

        switch (shapeType) {
            case CIRCLE:
                return new Circle(CIRCLE, parameters.get(0));
            case RECTANGLE:
                return new Rectangle(RECTANGLE, parameters.get(0), parameters.get(1));
            case TRIANGLE:
                return new Triangle(TRIANGLE, parameters.get(0), parameters.get(1), parameters.get(2));
            default:
                // will never be thrown, cause the value has been already validated
                throw new RuntimeException("Contact the support service!");
        }
    }

    private static void checkParametersCount(ShapeType shapeType, List<Double> parameters) {
        int requiredLength = shapeType.getParamsCount();
        int actualLength = parameters.size();

        if (requiredLength > actualLength) {
            throw new IllegalArgumentException("To less shape parameters has been given - need " +
                    requiredLength + ", but the given count is " + actualLength);
        }

        if (parameters.stream().anyMatch(n -> n <= 0)) {
            throw new IllegalArgumentException("All shape parameters should be greater than 0.");
        }
    }
}
