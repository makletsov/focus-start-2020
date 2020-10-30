package ru.makletsov.focusstart.shape;

public class Rectangle implements Shape {
    private static final String TYPE = "Rectangle";

    private final double height;
    private final double width;
    private final String infoPattern;

    public Rectangle(double height, double width, String unit) {
        this.height = height;
        this.width = width;
        this.infoPattern = "Shape's type : %s" +
                "%nArea         : %.2f " + unit +
                "2%nPerimeter    : %.2f " + unit +
                "%nDiagonal     : %.2f " + unit +
                "%nHeight       : %.2f " + unit +
                "%nWidth        : %.2f " + unit;
    }

    private double getArea() {
        return height * width;
    }

    private double getPerimeter() {
        return 2 * (height + width);
    }

    private double getDiagonal() {
        return Math.sqrt(Math.pow(height, 2) + Math.pow(width, 2));
    }

    @Override
    public String info() {
        return String.format(infoPattern, TYPE, getArea(), getPerimeter(), getDiagonal(), height, width);
    }
}
