package ru.makletsov.minesweeper.model;

import ru.makletsov.minesweeper.GameMode;

import java.time.Duration;
import java.time.Instant;
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
    private final List<GameListener> gameListeners;

    private int openedCellsCount;
    private boolean isMinesSet;
    private Instant startTime;
    private boolean isGameOver;

    public Game(GameMode gameMode) {
        this.gameMode = gameMode;
        height = gameMode.getHeight();
        width = gameMode.getWidth();
        minesCount = gameMode.getMinesCount();

        minedCells = new ArrayList<>();
        markedCells = new ArrayList<>();
        playground = new Cell[gameMode.getHeight()][gameMode.getWidth()];

        gameListeners = new ArrayList<>();

        IntStream.range(0, height).forEach(row ->
            IntStream.range(0, width).forEach(column ->
                playground[row][column] = new Cell(row, column)
            ));
    }

    public int getRemainingMinesCount() {
        return minesCount - markedCells.size();
    }

    public boolean isGameOver() {
        return isGameOver;
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
                markCellIfPossible(cell);
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

    private void markCellIfPossible(Cell cell) {
        if (getRemainingMinesCount() > 0) {
            cell.setState(CellState.MARKED);
            markedCells.add(cell);
        }
    }

    public Collection<Cell> tryOpenNeighbors(int rowIndex, int columnIndex) {
        checkCellIndex(rowIndex, columnIndex);

        Cell currentCell = playground[rowIndex][columnIndex];

        if (!(currentCell.getState() == CellState.OPEN) ||
            !isMinesSet ||
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
        Cell currentCell = playground[rowIndex][columnIndex];

        if (currentCell.getState() == CellState.MARKED) {
            return List.of();
        }

        if (!isMinesSet) {
            startTime = Instant.now();
            gameListeners.forEach(l -> l.gameStarted(startTime));

            setMines(rowIndex, columnIndex);
        }

        Queue<Cell> queue = new ArrayDeque<>();

        if (currentCell.isMined()) {
            isGameOver = true;
            currentCell.setState(CellState.OPEN);

            EndGameEvent event = getDefeatEvent(currentCell);
            gameListeners.forEach(l -> l.gameLost(event));

            return List.of();
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

            Duration gameDuration = Duration.between(startTime, Instant.now());
            gameListeners.forEach(l -> l.gameWon(gameDuration));
        }

        return involvedCells;
    }

    private EndGameEvent getDefeatEvent(Cell cell) {
        List<Cell> wrongMarkedCells = markedCells
            .stream()
            .filter(c -> !c.isMined())
            .collect(Collectors.toList());

        List<Cell> minedNotOpenedCells = minedCells
            .stream()
            .filter(this::isClosedAndNotMarked)
            .collect(Collectors.toList());

        return new EndGameEvent(cell, minedNotOpenedCells, wrongMarkedCells);
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
        return openedCellRowIndex != cellToBeMined.getRowIndex() &&
            openedCellColumnIndex != cellToBeMined.getColumnIndex() &&
            !cellToBeMined.isMined();
    }

    private boolean isAllEmptyCellsOpen() {
        return openedCellsCount + minesCount == height * width;
    }

    private void addNotMarkedNeighbors(Queue<Cell> queue, Cell cell) {
        getSurroundedCellsAsStream(cell.getRowIndex(), cell.getColumnIndex())
            .filter(this::isClosedAndNotMarked)
            .forEach(queue::offer);
    }

    private boolean isClosedAndNotMarked(Cell cell) {
        return cell.getState() == CellState.DEFAULT || cell.getState() == CellState.QUESTION_MARKED;
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

    public void addGameListener(GameListener gameListener) {
        gameListeners.add(gameListener);
    }
}
