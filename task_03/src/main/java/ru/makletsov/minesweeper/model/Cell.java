package ru.makletsov.minesweeper.model;

public class Cell {
    private CellState state;
    private final int rowIndex;
    private final int columnIndex;
    private boolean isMined;
    private int minesAround;

    Cell(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;

        state = CellState.DEFAULT;
    }

    void setState(CellState state) {
        this.state = state;
    }

    public CellState getState() {
        return state;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public boolean isMined() {
        return isMined;
    }

    void setMine() {
        isMined = true;
    }

    public int getMinedNeighboursCount() {
        return minesAround;
    }

    void addMinedNeighbour() {
        minesAround++;
    }
}
