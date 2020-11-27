package ru.makletsov.focusstart.shape;

public enum ShapeType {
    CIRCLE(1) , RECTANGLE(2) , TRIANGLE(3);

    private final int paramsCount;

    ShapeType(int paramsCount) {
        this.paramsCount = paramsCount;
    }

    public int getParamsCount() {
        return paramsCount;
    }

    public static ShapeType get(String inputString) {
        try {
            return ShapeType.valueOf(inputString.toUpperCase());
        } catch (IllegalArgumentException e) {
            IllegalArgumentException newException = new IllegalArgumentException("Unknown input shape type!");
            newException.addSuppressed(e);

            throw newException;
        }
    }
}
