package ru.makletsov.focusstart.shape;

public class Circle extends Shape {
    private static final String TYPE_NAME = "Circle";

    private final double radius;
    private final double diameter;

    private final String infoPattern;

    public Circle(ShapeType type, double radius) {
        super(type, TYPE_NAME);

        this.radius = radius;

        area = calculateArea(radius);
        perimeter = calculatePerimeter(radius);
        diameter = calculateDiameter(radius);

        this.infoPattern =
                        "Radius       : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
                        "Diameter     : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR;
    }

    private static double calculateArea(double radius) {
        return Math.PI * Math.pow(radius, 2);
    }

    private static double calculatePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }

    private static double calculateDiameter(double radius) {
        return 2 * radius;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + String.format(infoPattern, radius, diameter);
    }
}
