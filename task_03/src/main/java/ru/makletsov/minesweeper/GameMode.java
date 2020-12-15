package ru.makletsov.minesweeper;

public enum GameMode {
    BEGINNER(9, 9, 10, "Beginner"),
    AMATEUR(16, 16, 40, "Amateur"),
    PROFESSIONAL(16, 30, 99, "Professional");

    private final int height;
    private final int width;
    private final int minesCount;
    private final String name;

    GameMode(int height, int width, int minesCount, String name) {
        this.height = height;
        this.width = width;
        this.minesCount = minesCount;
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public String getName() {
        return name;
    }
}
