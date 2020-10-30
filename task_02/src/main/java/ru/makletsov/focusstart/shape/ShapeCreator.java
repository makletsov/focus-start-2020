package ru.makletsov.focusstart.shape;

import java.util.Arrays;

public class ShapeCreator {
    private static final String CIRCLE = "CIRCLE";
    private static final String RECTANGLE = "RECTANGLE";
    private static final String TRIANGLE = "TRIANGLE";

    public static Shape getShape(String shapeType, double[] shapeParameters, String unit) {
        Shape shape;

        if (shapeType.equals(CIRCLE) && isParamsCorrect(shapeParameters, 1)) {
            shape = new Circle(shapeParameters[0], unit);

        } else if (shapeType.equals(RECTANGLE) && isParamsCorrect(shapeParameters, 2)) {
            shape = new Rectangle(shapeParameters[0], shapeParameters[1], unit);

        } else if (shapeType.equals(TRIANGLE) && isParamsCorrect(shapeParameters, 3)) {
            shape = new Triangle(shapeParameters[0], shapeParameters[1], shapeParameters[2], unit);

        } else {
            throw new IllegalArgumentException("Unknown shape type.");
        }

        return shape;
    }

    private static boolean isParamsCorrect(double[] params, int requiredLength) {
        if (Arrays.stream(params).anyMatch(n -> n <= 0)) {
            throw new IllegalArgumentException("All shape parameters should be greater than 0.");
        }

        if (params.length < requiredLength) {
            throw new IllegalArgumentException("To less shape parameters has been given - need " +
                    requiredLength + ", but the given count is " + params.length);
        }

        return true;
    }
}
