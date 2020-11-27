package ru.makletsov.focusstart.shape;

public abstract class Shape {
    protected static final String STRING_PLACEHOLDER = "%s";
    protected static final String FRACTIONAL_PLACEHOLDER = "%.2f ";
    protected static final String LINE_SEPARATOR = "%n";
    protected static final String SQUARE_UNIT_SUFFIX = "2";
    protected static final String ANGLE_UNIT = "grad.";

    protected static final String UNIT = "m";

    protected double area;
    protected double perimeter;

    private final ShapeType type;
    private final String typeName;
    private final String infoPattern;

    protected Shape(ShapeType type, String typeName) {
        this.type = type;
        this.typeName = typeName;

        infoPattern =
                "Shape's type : " + STRING_PLACEHOLDER + LINE_SEPARATOR +
                "Area         : " + FRACTIONAL_PLACEHOLDER + UNIT + SQUARE_UNIT_SUFFIX + LINE_SEPARATOR +
                "Perimeter    : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR;
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

    public String getInfo() {
        return String.format(infoPattern, typeName, getArea(), getPerimeter());
    }
}
