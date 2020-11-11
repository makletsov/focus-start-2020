package ru.makletsov.focusstart.shape;

import java.util.List;

public class ShapeCreator {
    public static Shape getShape(String inputString, List<Double> parameters, String unit) {
        ShapeType shapeType = getShapeType(inputString);

        validateParameters(shapeType, parameters);

        Shape shape;

        switch (shapeType) {
            case CIRCLE:
                shape = new Circle(unit, parameters.get(0));
                break;
            case RECTANGLE:
                shape = new Rectangle(unit, parameters.get(0), parameters.get(1));
                break;
            case TRIANGLE:
                shape = new Triangle(unit, parameters.get(0), parameters.get(1), parameters.get(2));
                break;
            default:
                // will never be thrown, cause the value has been already validated
                throw new RuntimeException("Contact the support service!");
        }

        return shape;
    }

    private static ShapeType getShapeType(String inputString) {
        try {
            return ShapeType.valueOf(inputString.toUpperCase());
        } catch (IllegalArgumentException e) {
            IllegalArgumentException newException = new IllegalArgumentException("Unknown input shape type!");
            newException.addSuppressed(e);

            throw newException;
        }
    }

    private static void validateParameters(ShapeType shapeType, List<Double> parameters) {
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

    private enum ShapeType {
        CIRCLE(1) , RECTANGLE(2) , TRIANGLE(3);

        private final int paramsCount;

        ShapeType(int paramsCount) {
            this.paramsCount = paramsCount;
        }

        public int getParamsCount() {
            return paramsCount;
        }
    }
}
