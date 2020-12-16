package ru.makletsov.minesweeper.presenter;

import ru.makletsov.minesweeper.GameMode;
import ru.makletsov.minesweeper.view.*;
import ru.makletsov.minesweeper.model.*;
import ru.makletsov.minesweeper.model.RecordsTable;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class PresenterImpl implements GameListener, Presenter {
    private static final long TIMER_DELAY = 1000;
    private static final long TIMER_PERIOD = 1000;
    private static final String WINDOW_TITLE = "Minesweeper";

    private final RecordsTable recordsTable;
    private final List<RecordsConsumer> recordsConsumers;
    private final GameTimer timer;
    private final View view;

    private GameMode gameMode;
    private Game game;
    private boolean isFirstCellOpened;

    public PresenterImpl(GameMode gameMode, RecordsTable recordsTable, IconsStorage iconsStorage) {
        this.gameMode = gameMode;
        this.recordsTable = recordsTable;

        timer = new GameTimer(TIMER_DELAY, TIMER_PERIOD);
        view = new View(WINDOW_TITLE, gameMode, this, iconsStorage);
        game = new Game(gameMode);

        game.addGameListener(this);
        recordsConsumers = new ArrayList<>();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void restartGame() {
        if (isFirstCellOpened) {
            startNewGame();
        }
    }

    @Override
    public void startNewGame(GameMode gameMode) {
        this.gameMode = gameMode;
        startNewGame();
    }

    private void startNewGame() {
        game = new Game(gameMode);
        game.addGameListener(this);

        isFirstCellOpened = false;
        timer.turnOff();

        view.setNewPlayground(gameMode);
    }

    public void addRecordsConsumer(RecordsConsumer recordsConsumer) {
        recordsConsumers.add(recordsConsumer);
    }

    @Override
    public void saveRecordsAndExit() {
        try {
            recordsConsumers.forEach(c -> c.saveRecords(recordsTable.getRecords()));
        } catch (Exception ignored) {
            //records saving attempt is failed, exit anyway
        }

        System.exit(0);
    }

    @Override
    public void openCell(int rowIndex, int columnIndex) {
        for (Cell cell : game.openCell(rowIndex, columnIndex)) {
            int row = cell.getRowIndex();
            int column = cell.getColumnIndex();
            int minesAround = cell.getMinedNeighboursCount();

            view.showOpenCell(row, column, minesAround);
        }
    }

    @Override
    public void tryOpenNeighbors(int rowIndex, int columnIndex) {
        for (Cell cell : game.tryOpenNeighbors(rowIndex, columnIndex)) {
            int row = cell.getRowIndex();
            int column = cell.getColumnIndex();
            int minesAround = cell.getMinedNeighboursCount();

            view.showOpenCell(row, column, minesAround);
        }
    }

    @Override
    public void changeCellMark(int rowIndex, int columnIndex) {
        Cell cell = game.changeCellMark(rowIndex, columnIndex);
        int minesRemains = game.getRemainingMinesCount();

        switch (cell.getState()) {
            case MARKED:
                view.showMarkedCell(rowIndex, columnIndex, minesRemains);
                break;
            case QUESTION_MARKED:
                view.showQuestionMarkedCell(rowIndex, columnIndex, minesRemains);
                break;
            case DEFAULT:
                view.showDefaultCell(rowIndex, columnIndex);
                break;
        }
    }

    @Override
    public void gameStarted(Instant startTime) {
        timer.turnOn(getViewUpdateTimerTask(startTime));
        isFirstCellOpened = true;
    }

    private TimerTask getViewUpdateTimerTask(Instant startTime) {
        return new TimerTask() {
            @Override
            public void run() {
                Duration duration = Duration.between(startTime, Instant.now());
                view.showTime((int) duration.toSeconds());
            }
        };
    }

    @Override
    public void gameLost(EndGameEvent endGameEvent) {
        view.showFailedFace();

        int failedCellRowIndex = endGameEvent.getFailedCell().getRowIndex();
        int failedCellColumnIndex = endGameEvent.getFailedCell().getColumnIndex();

        view.showFailedCell(failedCellRowIndex, failedCellColumnIndex);

        for (Cell minedCell : endGameEvent.getMinedCells()) {
            int row = minedCell.getRowIndex();
            int column = minedCell.getColumnIndex();

            view.showMinedCell(row, column);
        }

        for (Cell wrongMarkedCell : endGameEvent.getWrongMarkedCells()) {
            int row = wrongMarkedCell.getRowIndex();
            int column = wrongMarkedCell.getColumnIndex();

            view.showWrongMarkedCell(row, column);
        }

        timer.turnOff();
    }

    @Override
    public void gameWon(Duration gameDuration) {
        view.showWinFace();

        GameMode mode = game.getGameMode();

        if (recordsTable.isRecord(mode, gameDuration)) {
            recordsTable.setRecord(mode, view.getRecordOwner(), gameDuration);
        }

        timer.turnOff();
    }

    @Override
    public boolean isGameInProcess() {
        return !game.isGameOver();
    }

    @Override
    public Collection<Record> getRecords() {
        return recordsTable.getRecords();
    }
}
