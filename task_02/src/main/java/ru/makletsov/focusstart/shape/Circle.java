package ru.makletsov.focusstart.shape;

public class Circle extends Shape {
    private final double radius;
    private final double diameter;

    public Circle(ShapeType type, double radius) {
        super(type);

        this.radius = radius;

        area = calculateArea(radius);
        perimeter = calculatePerimeter(radius);
        diameter = calculateDiameter(radius);
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

    public double getRadius() {
        return radius;
    }

    public double getDiameter() {
        return diameter;
    }
}
