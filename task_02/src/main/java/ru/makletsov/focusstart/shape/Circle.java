package ru.makletsov.focusstart.shape;

public class Circle implements Shape {
    private static final String TYPE = "Circle";

    private final double radius;
    private final String infoPattern;

    public Circle(double radius, String unit) {
        this.radius = radius;
        this.infoPattern = "Shape's type : %s" +
                        "%nArea         : %.2f " + unit +
                        "2%nPerimeter    : %.2f " + unit +
                        "%nRadius       : %.2f " + unit +
                        "%nDiameter     : %.2f " + unit;
    }

    private double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    private double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    private double getDiameter() {
        return 2 * radius;
    }

    @Override
    public String info() {
        return String.format(infoPattern, TYPE, getArea(), getPerimeter(), radius, getDiameter());
    }
}
