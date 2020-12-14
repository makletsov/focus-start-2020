package ru.makletsov.minesweeper.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

        IntStream.range(0, height).forEach(i ->
            IntStream.range(0, width).forEach(j ->
                playground[i][j] = new Cell(i, j)
            ));
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Cell changeCellMark(int rowIndex, int columnIndex) {
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

        if (!isMinesSet ||
            notEnoughMarkedNeighbours(rowIndex, columnIndex)) {
            return List.of();
        }

        return getSurroundedCellsAsStream(rowIndex, columnIndex)
            .filter(this::isReadyToBeOpened)
            .flatMap(this::getNextRecursivelyOpenedAsStream)
            .collect(Collectors.toList());
    }

    private boolean notEnoughMarkedNeighbours(int rowIndex, int columnIndex) {
        return countMarkedNeighbours(rowIndex, columnIndex) < getMinedNeighboursCount(rowIndex, columnIndex);
    }

    private long countMarkedNeighbours(int rowIndex, int columnIndex) {
        return getSurroundedCellsAsStream(rowIndex, columnIndex)
            .filter(this::isMarked)
            .count();
    }

    private boolean isMarked(Cell cell) {
        return cell.getState() == CellState.MARKED;
    }

    private boolean isReadyToBeOpened(Cell cell) {
        return cell.getState() != CellState.OPEN &&
            cell.getState() != CellState.MARKED;
    }

    private Stream<Cell> getSurroundedCellsAsStream(int rowIndex, int columnIndex) {
        return IntStream.range(rowIndex - 1, rowIndex + 2)
            .filter(this::isCorrectRowIndex)
            .boxed()
            .flatMap(i ->
                IntStream.range(columnIndex - 1, columnIndex + 2)
                    .filter(this::isCorrectColumnIndex)
                    .mapToObj(j -> playground[i][j]));
    }

    private boolean isCorrectRowIndex(int index) {
        return index >= 0 && index < height;
    }

    private boolean isCorrectColumnIndex(int index) {
        return index >= 0 && index < width;
    }

    private Stream<Cell> getNextRecursivelyOpenedAsStream(Cell cell) {
        return openCell(cell.getRowIndex(), cell.getColumnIndex()).stream();
    }

    public Collection<Cell> openCell(int rowIndex, int columnIndex) {
        checkCellIndex(rowIndex, columnIndex);

        if (!isMinesSet) {
            startTime = LocalDateTime.now();           //TODO start game event
            setMines(rowIndex, columnIndex);
        }

        Queue<Cell> queue = new ArrayDeque<>();
        Cell currentCell = playground[rowIndex][columnIndex];

        if (currentCell.isMined()) {
            isGameOver = true;                         //TODO loose game event
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
            isGameOver = true;                      //TODO win game event
            isVictory = true;
        }

        return involvedCells;
    }

    private void setMines(int firstOpenedCellRowIndex, int firstOpenedCellColumnIndex) {
        checkCellIndex(firstOpenedCellRowIndex, firstOpenedCellColumnIndex);

        if (minedCells.size() > 0) {
            throw new IllegalStateException(REPEATED_MINES_SET_ATTEMPT_EXCEPTION_TEXT);
        }

        Random random = new Random();

        while (minedCells.size() < minesCount) {
            int row = random.nextInt(height);
            int column = random.nextInt(width);

            Cell nextMinedCell = playground[row][column];

            if (canSetMine(firstOpenedCellRowIndex, firstOpenedCellColumnIndex, nextMinedCell)) {
                nextMinedCell.setMine();

                minedCells.add(nextMinedCell);

                getSurroundedCellsAsStream(row, column)
                    .forEach(Cell::addMinedNeighbour);
            }
        }

        isMinesSet = true;
    }

    private boolean canSetMine(int openedCellRowIndex, int openedCellColumnIndex, Cell cellToBeMined) {
        return openedCellRowIndex != cellToBeMined.rowIndex &&
            openedCellColumnIndex != cellToBeMined.columnIndex &&
            !cellToBeMined.isMined();
    }

    private boolean isAllEmptyCellsOpen() {
        return openedCellsCount + minesCount == height * width;
    }

    private void addNotMarkedNeighbors(Queue<Cell> queue, Cell cell) {
        getSurroundedCellsAsStream(cell.rowIndex, cell.columnIndex)
            .filter(this::isClosedAndNotMarked)
            .forEach(queue::offer);
    }

    private boolean isClosedAndNotMarked(Cell cell) {
        return cell.getState() == CellState.DEFAULT || cell.getState() == CellState.QUESTION_MARKED;
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

    private int getMinedNeighboursCount(int rowIndex, int columnIndex) {
        return playground[rowIndex][columnIndex].getMinedNeighboursCount();
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
