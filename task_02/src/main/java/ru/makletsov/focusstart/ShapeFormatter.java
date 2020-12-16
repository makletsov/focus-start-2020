package ru.makletsov.focusstart;

import ru.makletsov.focusstart.shape.Circle;
import ru.makletsov.focusstart.shape.Rectangle;
import ru.makletsov.focusstart.shape.Shape;
import ru.makletsov.focusstart.shape.Triangle;

public class ShapeFormatter {
    protected static final String STRING_PLACEHOLDER = "%s";
    protected static final String FRACTIONAL_PLACEHOLDER = "%.2f ";
    protected static final String LINE_SEPARATOR = "%n";
    protected static final String SQUARE_UNIT_SUFFIX = "2";
    protected static final String ANGLE_UNIT = "grad.";

    protected static final String UNIT = "m";

    private static final String CIRCLE_NAME = "Circle";
    private static final String RECTANGLE_NAME = "Rectangle";
    private static final String TRIANGLE_NAME = "Triangle";

    private static final String BASE_TEMPLATE =
            "Shape's type : " + STRING_PLACEHOLDER + LINE_SEPARATOR +
            "Area         : " + FRACTIONAL_PLACEHOLDER + UNIT + SQUARE_UNIT_SUFFIX + LINE_SEPARATOR +
            "Perimeter    : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR;

    private static final String CIRCLE_TEMPLATE =
            "Radius       : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
            "Diameter     : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR;

    private static final String RECTANGLE_TEMPLATE =
            "Diagonal     : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
            "Height       : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
            "Width        : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR;

    private static final String TRIANGLE_TEMPLATE =
            "Edge1        : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
            "Angle1       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR +
            "Edge2        : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
            "Angle2       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR +
            "Edge3        : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
            "Angle3       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR;

    public static String format(Shape shape) {
        switch (shape.getType()) {
            case CIRCLE:
                return format((Circle) shape);
            case RECTANGLE:
                return format((Rectangle) shape);
            case TRIANGLE:
                return format((Triangle) shape);
            default:
                throw new IllegalArgumentException("Unknown shape type!");
        }
    }

    private static String format(Circle circle) {
        return getBaseString(circle, CIRCLE_NAME) +
                String.format(CIRCLE_TEMPLATE,
                circle.getRadius(),
                circle.getDiameter());
    }

    private static String format(Rectangle rectangle) {
        return getBaseString(rectangle, RECTANGLE_NAME) +
                String.format(RECTANGLE_TEMPLATE,
                rectangle.getDiagonal(),
                rectangle.getHeight(),
                rectangle.getWidth());
    }

    private static String format(Triangle triangle) {
        return getBaseString(triangle, TRIANGLE_NAME) +
                String.format(TRIANGLE_TEMPLATE,
                triangle.getEdge1(),
                triangle.getAngle1(),
                triangle.getEdge2(),
                triangle.getAngle2(),
                triangle.getEdge3(),
                triangle.getAngle3());
    }

    private static String getBaseString(Shape shape, String shapeName) {
        return String.format(BASE_TEMPLATE,
                shapeName,
                shape.getArea(),
                shape.getPerimeter());
    }
}
