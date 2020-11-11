package ru.makletsov.focusstart.shape;

public class Triangle extends Shape {
    private static final String TYPE_NAME = "Triangle";

    private final double area;
    private final double perimeter;

    private final double edge1;
    private final double edge2;
    private final double edge3;

    private final double angle1;
    private final double angle2;
    private final double angle3;

    private final String infoPattern;

    public Triangle(String unit, double edge1, double edge2, double edge3) {
        super(unit, TYPE_NAME);

        if (edge1 + edge2 < edge3 || edge2 + edge3 < edge1 || edge1 + edge3 < edge2) {
            throw new IllegalArgumentException("Cannot create triangle with given edges values.");
        }

        perimeter = edge1 + edge2 + edge3 + 0.0;
        area = Math.sqrt(getHalfPerimeter() * getHalfPerimeterMinusEdge(edge1)
                * getHalfPerimeterMinusEdge(edge2) * getHalfPerimeterMinusEdge(edge3));

        this.edge1 = edge1;
        this.edge2 = edge2;
        this.edge3 = edge3;

        angle1 = getAngle(edge2, edge3, edge1);
        angle2 = getAngle(edge1, edge3, edge2);
        angle3 = getAngle(edge1, edge2, edge3);

        this.infoPattern =
                "Edge1        : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR +
                "Angle1       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR +
                "Edge2        : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR +
                "Angle2       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR +
                "Edge3        : " + FRACTIONAL_PLACEHOLDER + unit + LINE_SEPARATOR +
                "Angle3       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR;
    }

    private double getHalfPerimeterMinusEdge(double edge) {
        return perimeter / 2 - edge;
    }

    private double getHalfPerimeter() {
        return perimeter / 2;
    }

    private double getAngle(double adjacentEdge1, double adjacentEdge2, double oppositeEdge) {
        return Math.acos((Math.pow(adjacentEdge1, 2) + Math.pow(adjacentEdge2, 2) - Math.pow(oppositeEdge, 2))
                / (2 * adjacentEdge1 * adjacentEdge2 * 1.0)) / Math.PI * 180;
    }

    protected double getArea() {
        return area;
    }

    protected double getPerimeter() {
        return perimeter;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + String.format(infoPattern, edge1, angle1, edge2, angle2, edge3, angle3);
    }
}
