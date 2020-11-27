package ru.makletsov.focusstart.shape;

public class Triangle extends Shape {
    private static final String TYPE_NAME = "Triangle";

    private final double edge1;
    private final double edge2;
    private final double edge3;

    private final double angle1;
    private final double angle2;
    private final double angle3;

    private final String infoPattern;

    public Triangle(ShapeType type, double edge1, double edge2, double edge3) {
        super(type, TYPE_NAME);

        if (edge1 + edge2 < edge3 || edge2 + edge3 < edge1 || edge1 + edge3 < edge2) {
            throw new IllegalArgumentException("Cannot create triangle with given edges values.");
        }

        perimeter = calculatePerimeter(edge1, edge2, edge3);
        area = calculateArea(edge1, edge2, edge3);

        this.edge1 = edge1;
        this.edge2 = edge2;
        this.edge3 = edge3;

        angle1 = getAngle(edge2, edge3, edge1);
        angle2 = getAngle(edge1, edge3, edge2);
        angle3 = getAngle(edge1, edge2, edge3);

        this.infoPattern =
                "Edge1        : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
                "Angle1       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR +
                "Edge2        : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
                "Angle2       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR +
                "Edge3        : " + FRACTIONAL_PLACEHOLDER + UNIT + LINE_SEPARATOR +
                "Angle3       : " + FRACTIONAL_PLACEHOLDER + ANGLE_UNIT + LINE_SEPARATOR;
    }

    private double calculateArea(double edge1, double edge2, double edge3) {
        double perimeter = calculatePerimeter(edge1, edge2, edge3);

        return Math.sqrt(getHalfPerimeter(perimeter) * getHalfPerimeterMinusEdge(edge1, perimeter)
                * getHalfPerimeterMinusEdge(edge2, perimeter) * getHalfPerimeterMinusEdge(edge3, perimeter));
    }

    private double calculatePerimeter(double edge1, double edge2, double edge3) {
        if (perimeter == 0) {
            perimeter = edge1 + edge2 + edge3;
        }

        return perimeter;
    }

    private double getHalfPerimeterMinusEdge(double edge, double perimeter) {
        return perimeter / 2 - edge;
    }

    private double getHalfPerimeter(double perimeter) {
        return perimeter / 2;
    }

    private double getAngle(double adjacentEdge1, double adjacentEdge2, double oppositeEdge) {
        return Math.acos((Math.pow(adjacentEdge1, 2) + Math.pow(adjacentEdge2, 2) - Math.pow(oppositeEdge, 2))
                / (2 * adjacentEdge1 * adjacentEdge2 * 1.0)) / Math.PI * 180;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + String.format(infoPattern, edge1, angle1, edge2, angle2, edge3, angle3);
    }
}
