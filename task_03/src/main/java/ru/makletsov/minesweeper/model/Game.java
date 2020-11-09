package ru.makletsov.minesweeper.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
    private static final String REPEATED_MINES_SET_ATTEMPT_EXCEPTION_TEXT = "Mines have been already set.";

    private final GameMode gameMode;
    private final int height;
    private final int width;
    private final int minesCount;
    private final Cell[][] playground;
    private final Collection<Cell> minedCells;
    private final Collection<Cell> markedCells;

    private int openedCellsCount;
    private boolean isMinesSet;
    private LocalDateTime startTime;
    private boolean isGameOver;
    private boolean isVictory;

    public Game(GameMode gameMode) {
        this.gameMode = gameMode;
        height = gameMode.getHeight();
        width = gameMode.getWidth();
        minesCount = gameMode.getMinesCount();

        minedCells = new ArrayList<>();
        markedCells = new ArrayList<>();
        playground = new Cell[gameMode.getHeight()][gameMode.getWidth()];

        IntStream.range(0, height)
                .forEach(i ->
                        IntStream.range(0, width)
                                .forEach(j -> playground[i][j] = new Cell(i, j)));
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Cell changeMark(int rowIndex, int columnIndex) {
        checkCellIndex(rowIndex, columnIndex);

        Cell cell = playground[rowIndex][columnIndex];
        CellState currentState = cell.getState();

        switch (currentState) {
            case DEFAULT:
                cell.setState(CellState.MARKED);
                markedCells.add(cell);
                break;
            case MARKED:
                cell.setState(CellState.QUESTION_MARKED);
                markedCells.remove(cell);
                break;
            case QUESTION_MARKED:
                cell.setState(CellState.DEFAULT);
                break;
        }

        return cell;
    }

    public CellState getCellState(int rowIndex, int columnIndex) {
        checkCellIndex(rowIndex, columnIndex);

        return playground[rowIndex][columnIndex].getState();
    }

    public Collection<Cell> tryOpenNeighbors(int rowIndex, int columnIndex) {
        checkCellIndex(rowIndex, columnIndex);

        if (!isMinesSet) {
            return List.of();
        }

        Collection<Cell> markedNeighbours = new ArrayList<>();
        Collection<Cell> involvedCells = new ArrayList<>();

        IntStream.range(rowIndex - 1, rowIndex + 2)
                .filter(i -> (i >= 0 && i < height))
                .forEach(i ->
                        IntStream.range(columnIndex - 1, columnIndex + 2)
                                .filter(j -> (j >= 0 && j < width))
                                .forEach(j -> {
                                    Cell cell = playground[i][j];

                                    if (cell.getState() == CellState.MARKED) {
                                        markedNeighbours.add(cell);
                                    } else if (cell.getState() != CellState.OPEN) {
                                        involvedCells.add(cell);
                                    }
                                }));

        int minedNeighboursCount = playground[rowIndex][columnIndex].getMinedNeighboursCount();

        if (markedNeighbours.size() < minedNeighboursCount) {
            return List.of();
        }

        return involvedCells.stream()
                .flatMap(cell -> openCell(cell).stream())
                .collect(Collectors.toList());
    }

    private Collection<Cell> openCell(Cell cell) {
        return openCell(cell.getRowIndex(), cell.getColumnIndex());
    }

    public Collection<Cell> openCell(int rowIndex, int columnIndex) {
        checkCellIndex(rowIndex, columnIndex);

        if (!isMinesSet) {
            startTime = LocalDateTime.now();
            setMines(rowIndex, columnIndex);
        }

        Queue<Cell> queue = new ArrayDeque<>();
        Cell currentCell = playground[rowIndex][columnIndex];

        if (currentCell.isMined()) {
            isGameOver = true;
            return Set.of(currentCell);
        }

        queue.offer(currentCell);
        Collection<Cell> involvedCells = new ArrayList<>();

        while (!queue.isEmpty()) {
            currentCell = queue.poll();

            if (currentCell.getState() != CellState.OPEN) {
                openedCellsCount++;
                currentCell.setState(CellState.OPEN);
                involvedCells.add(currentCell);
            }

            if (currentCell.getMinedNeighboursCount() == 0) {
                addNotMarkedNeighbors(queue, currentCell);
            }
        }

        if (isAllEmptyCellsOpen()) {
            isGameOver = true;
            isVictory = true;
        }

        return involvedCells;
    }

    private void setMines(int rowIndex, int columnIndex) {
        checkCellIndex(rowIndex, columnIndex);

        if (minedCells.size() > 0) {
            throw new IllegalStateException(REPEATED_MINES_SET_ATTEMPT_EXCEPTION_TEXT);
        }

        Random random = new Random();

        while (minedCells.size() < minesCount) {
            int row = random.nextInt(height);
            int column = random.nextInt(width);

            Cell currentCell = playground[row][column];
            boolean canSetMine =
                    row != rowIndex &&
                            column != columnIndex &&
                            !currentCell.isMined();

            if (canSetMine) {
                currentCell.setMine();
                minedCells.add(currentCell);

                IntStream.range(row - 1, row + 2)
                        .filter(i -> i >= 0 && i < height)
                        .forEach(i -> IntStream.range(column - 1, column + 2)
                                .filter(j -> j >= 0 && j < width)
                                .forEach(j -> playground[i][j].addMinedNeighbour()));

            }
        }

        isMinesSet = true;
    }

    private boolean isAllEmptyCellsOpen() {
        return openedCellsCount + minesCount == height * width;
    }

    private void addNotMarkedNeighbors(Queue<Cell> queue, Cell cell) {
        int topBound = cell.getRowIndex() - 1;
        int bottomBound = cell.getRowIndex() + 2;
        int leftBound = cell.getColumnIndex() - 1;
        int rightBound = cell.getColumnIndex() + 2;

        IntStream.range(topBound, bottomBound)
                .filter(i -> i >= 0 && i < height)
                .forEach(i ->
                        IntStream.range(leftBound, rightBound)
                                .filter(j -> j >= 0 && j < width)
                                .filter(j -> playground[i][j].getState() == CellState.DEFAULT
                                        || playground[i][j].getState() == CellState.QUESTION_MARKED)
                                .forEach(j -> queue.offer(playground[i][j])));
    }

    public Collection<Cell> getMarkedCells() {
        return markedCells;
    }

    public Collection<Cell> getMinedCells() {
        return minedCells;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isVictory() {
        return isVictory;
    }

    private void checkCellIndex(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= height) {
            throw new IndexOutOfBoundsException("Row index must be between 0 and " + height);
        }

        if (columnIndex < 0 || columnIndex >= width) {
            throw new IndexOutOfBoundsException("Column index must be between 0 and " + width);
        }
    }

    public static class Cell {
        private CellState state;
        private final int rowIndex;
        private final int columnIndex;
        private boolean isMined;
        private int minesAround;

        private Cell(int rowIndex, int columnIndex) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;

            state = CellState.DEFAULT;
        }

        private void setState(CellState state) {
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

        private void setMine() {
            isMined = true;
        }

        public int getMinedNeighboursCount() {
            return minesAround;
        }

        private void addMinedNeighbour() {
            minesAround++;
        }
    }

    public enum CellState {
        OPEN, DEFAULT, MARKED, QUESTION_MARKED
    }
}
