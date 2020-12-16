package ru.makletsov.focusstart.shape;

public class Triangle extends Shape {
    private final double edge1;
    private final double edge2;
    private final double edge3;

    private final double angle1;
    private final double angle2;
    private final double angle3;

    public Triangle(ShapeType type, double edge1, double edge2, double edge3) {
        super(type);

        checkEdgesLengthsCompatibility(edge1, edge2, edge3);

        perimeter = calculatePerimeter(edge1, edge2, edge3);
        area = calculateArea(edge1, edge2, edge3);

        this.edge1 = edge1;
        this.edge2 = edge2;
        this.edge3 = edge3;

        angle1 = calculateAngle(edge2, edge3, edge1);
        angle2 = calculateAngle(edge1, edge3, edge2);
        angle3 = calculateAngle(edge1, edge2, edge3);
    }

    private static void checkEdgesLengthsCompatibility(double edge1, double edge2, double edge3) {
        if (edge1 + edge2 < edge3 || edge2 + edge3 < edge1 || edge1 + edge3 < edge2) {
            throw new IllegalArgumentException("Cannot create triangle with given edges values.");
        }
    }

    private double calculateArea(double edge1, double edge2, double edge3) {
        double perimeter = calculatePerimeter(edge1, edge2, edge3);

        return Math.sqrt(calculateHalfPerimeter(perimeter) * calculateHalfPerimeterMinusEdge(edge1, perimeter)
                * calculateHalfPerimeterMinusEdge(edge2, perimeter) * calculateHalfPerimeterMinusEdge(edge3, perimeter));
    }

    private double calculatePerimeter(double edge1, double edge2, double edge3) {
        if (perimeter == 0) {
            perimeter = edge1 + edge2 + edge3;
        }

        return perimeter;
    }

    private double calculateHalfPerimeterMinusEdge(double edge, double perimeter) {
        return perimeter / 2 - edge;
    }

    private double calculateHalfPerimeter(double perimeter) {
        return perimeter / 2;
    }

    private double calculateAngle(double adjacentEdge1, double adjacentEdge2, double oppositeEdge) {
        return Math.acos((Math.pow(adjacentEdge1, 2) + Math.pow(adjacentEdge2, 2) - Math.pow(oppositeEdge, 2))
                / (2 * adjacentEdge1 * adjacentEdge2 * 1.0)) / Math.PI * 180;
    }

    public double getEdge1() {
        return edge1;
    }

    public double getEdge2() {
        return edge2;
    }

    public double getEdge3() {
        return edge3;
    }

    public double getAngle1() {
        return angle1;
    }

    public double getAngle2() {
        return angle2;
    }

    public double getAngle3() {
        return angle3;
    }
}
