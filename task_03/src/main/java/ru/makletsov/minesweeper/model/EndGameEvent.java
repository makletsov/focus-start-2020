package ru.makletsov.minesweeper.model;

import java.util.Collection;

public class EndGameEvent {
    private final Cell failedCell;
    private final Collection<Cell> minedCells;
    private final Collection<Cell> wrongMarkedCells;

    public EndGameEvent(Cell failedCell,
                        Collection<Cell> minedCells,
                        Collection<Cell> wrongMarkedCells) {
        this.failedCell = failedCell;
        this.minedCells = minedCells;
        this.wrongMarkedCells = wrongMarkedCells;
    }

    public Cell getFailedCell() {
        return failedCell;
    }

    public Collection<Cell> getMinedCells() {
        return minedCells;
    }

    public Collection<Cell> getWrongMarkedCells() {
        return wrongMarkedCells;
    }
}
