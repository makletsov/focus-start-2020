package ru.makletsov.minesweeper.view;

import ru.makletsov.minesweeper.GameMode;
import ru.makletsov.minesweeper.model.Record;

import java.util.Collection;

public interface GameManipulator {
    void startNewGame(GameMode gameMode);

    void restartGame();

    void openCell(int rowIndex, int columnIndex);

    void tryOpenNeighbors(int rowIndex, int columnIndex);

    void changeCellMark(int rowIndex, int columnIndex);

    boolean isGameInProcess();

    void saveRecordsAndExit();

    Collection<Record> getRecords();
}
