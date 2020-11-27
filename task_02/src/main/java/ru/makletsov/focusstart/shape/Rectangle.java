package ru.makletsov.focusstart.shape;

public class Rectangle extends Shape {
    private static final String TYPE_NAME = "Rectangle";

    private final double diagonal;
    private final double height;
    private final double width;

    private final String infoPattern;

    public Rectangle(ShapeType type, double height, double width) {
        super(type, TYPE_NAME);

        this.height = height;
        this.width = width;

        area = calculateArea(height, width);
        perimeter = calculatePerimeter(height, width);
        diagonal = calculateDiagonal(height, width);

        this.infoPattern =
                "Diagonal     : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
                "Height       : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
                "Width        : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR;
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

    @Override
    public String getInfo() {
        return super.getInfo() + String.format(infoPattern, diagonal, height, width);
    }
}
