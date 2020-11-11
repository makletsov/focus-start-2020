package ru.makletsov.focusstart.shape;

public class Circle extends Shape {
    private static final String TYPE_NAME = "Circle";

    private final double area;
    private final double perimeter;

    private final double radius;
    private final double diameter;

    private final String infoPattern;

    public Circle(String unit, double radius) {
        super(unit, TYPE_NAME);

        area = Math.PI * Math.pow(radius, 2);
        perimeter = 2 * Math.PI * radius;

        this.radius = radius;
        diameter = 2 * radius;

        this.infoPattern =
                        "Radius       : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR +
                        "Diameter     : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR;
    }

    protected double getArea() {
        return area;
    }

    protected double getPerimeter() {
        return perimeter;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + String.format(infoPattern, radius, diameter);
    }
}
