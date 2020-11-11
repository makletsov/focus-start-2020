package ru.makletsov.focusstart.shape;

public abstract class Shape {
    protected static final String STRING_PLACEHOLDER = "%s";
    protected static final String FRACTIONAL_PLACEHOLDER = "%.2f ";
    protected static final String LINE_SEPARATOR = "%n";
    protected static final String SQUARE_UNIT_SUFFIX = "2";
    protected static final String ANGLE_UNIT = "grad.";

    private final String typeName;
    private final String infoPattern;

    protected Shape(String unit, String type) {
        this.typeName = type;

        infoPattern =
                "Shape's type : " + STRING_PLACEHOLDER + LINE_SEPARATOR +
                "Area         : " + FRACTIONAL_PLACEHOLDER + unit + SQUARE_UNIT_SUFFIX + LINE_SEPARATOR +
                "Perimeter    : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR;
    }

    protected abstract double getArea();

    protected abstract double getPerimeter();

    public String getInfo() {
        return String.format(infoPattern, typeName, getArea(), getPerimeter());
    }
}
