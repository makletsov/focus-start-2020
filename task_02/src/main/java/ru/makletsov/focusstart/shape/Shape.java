package ru.makletsov.focusstart.shape;

public abstract class Shape {
    protected double area;
    protected double perimeter;

    private final ShapeType type;

    protected Shape(ShapeType type) {
        this.type = type;
    }

    public ShapeType getType() {
        return type;
    }

    public double getArea() {
        return area;
    }

    public double getPerimeter() {
        return area;
    }
}
