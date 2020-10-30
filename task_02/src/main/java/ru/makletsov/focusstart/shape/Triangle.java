package ru.makletsov.focusstart.shape;

public class Triangle implements Shape {
    private static final String TYPE = "Triangle";

    private final double edge1;
    private final double edge2;
    private final double edge3;
    private final String infoPattern;

    public Triangle(double edge1, double edge2, double edge3, String unit) {
        if (edge1 + edge2 < edge3 || edge2 + edge3 < edge1 || edge1 + edge3 < edge2) {
            throw new IllegalArgumentException("Cannot create triangle with given edges values.");
        }

        this.edge1 = edge1;
        this.edge2 = edge2;
        this.edge3 = edge3;
        this.infoPattern = "Shape's type : %s" +
                "%nArea         : %.2f " + unit +
                "2%nPerimeter    : %.2f " + unit +
                "%nEdge1        : %.2f " + unit +
                "%nAngle1       : %.2f grad." +
                "%nEdge2        : %.2f " + unit +
                "%nAngle2       : %.2f grad." +
                "%nEdge3        : %.2f " + unit +
                "%nAngle3       : %.2f grad.";
    }

    private double getArea() {
        return Math.sqrt(helper(0) * helper(edge1) * helper(edge2) * helper(edge3));
    }

    private double helper(double edge) {
        return getPerimeter() / 2 - edge;
    }

    private double getAngle(double adjacentEdge1, double adjacentEdge2, double oppositeEdge) {
        return Math.acos((Math.pow(adjacentEdge1, 2) + Math.pow(adjacentEdge2, 2) - Math.pow(oppositeEdge, 2))
                / (2 * adjacentEdge1 * adjacentEdge2 * 1.0)) / Math.PI * 180;
    }

    private double getPerimeter() {
        return edge1 + edge2 + edge3 + 0.0;
    }

    @Override
    public String info() {
        return String.format(infoPattern, TYPE, getArea(), getPerimeter(),
                edge1, getAngle(edge2, edge3, edge1),
                edge2, getAngle(edge1, edge3, edge2),
                edge3, getAngle(edge1, edge2, edge3));
    }
}
