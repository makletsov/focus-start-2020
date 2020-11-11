package ru.makletsov.focusstart.shape;

public class Rectangle extends Shape {
    private static final String TYPE_NAME = "Rectangle";

    private final double area;
    private final double perimeter;

    private final double diagonal;
    private final double height;
    private final double width;

    private final String infoPattern;

    public Rectangle(String unit, double height, double width) {
        super(unit, TYPE_NAME);

        area = height * width;
        perimeter = 2 * (height + width);

        diagonal = Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));
        this.height = height;
        this.width = width;

        this.infoPattern =
                "Diagonal     : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR +
                "Height       : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR +
                "Width        : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR;
    }

    protected double getArea() {
        return area;
    }

    protected double getPerimeter() {
        return perimeter;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + String.format(infoPattern, diagonal, height, width);
    }
}
