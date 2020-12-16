package ru.makletsov.focusstart.shape;

public class Rectangle extends Shape {
    private final double diagonal;
    private final double height;
    private final double width;

    public Rectangle(ShapeType type, double height, double width) {
        super(type);

        this.height = height;
        this.width = width;

        area = calculateArea(height, width);
        perimeter = calculatePerimeter(height, width);
        diagonal = calculateDiagonal(height, width);
    }

    private static double calculateArea(double height, double width) {
        return height * width;
    }

    private static double calculatePerimeter(double height, double width) {
        return 2 * (height + width);
    }

    private static double calculateDiagonal(double height, double width) {
        return Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));
    }

    public double getDiagonal() {
        return diagonal;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}
