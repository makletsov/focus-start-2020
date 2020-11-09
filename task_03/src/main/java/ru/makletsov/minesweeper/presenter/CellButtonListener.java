package ru.makletsov.minesweeper.presenter;

import ru.makletsov.minesweeper.view.View;
import ru.makletsov.minesweeper.model.Game;
import ru.makletsov.minesweeper.model.GameTimer;
import ru.makletsov.minesweeper.model.Records;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TimerTask;

public class CellButtonListener extends MouseAdapter {
    private static final int BUTTON_2_DOWN_MASK = InputEvent.BUTTON2_DOWN_MASK;
    private static final int BUTTON_3_DOWN_MASK = InputEvent.BUTTON3_DOWN_MASK;
    private static final int BOTH_BUTTONS_MASK = InputEvent.BUTTON1_DOWN_MASK
            | InputEvent.BUTTON3_DOWN_MASK;
    private static final int THREE_BUTTONS_MASK = InputEvent.BUTTON1_DOWN_MASK
            | InputEvent.BUTTON2_DOWN_MASK | InputEvent.BUTTON3_DOWN_MASK;

    private final Game game;
    private final View view;
    private final GameTimer timer;
    private final Records records;

    public CellButtonListener(Game game, View view, GameTimer timer, Records records) {
        this.game = game;
        this.view = view;
        this.timer = timer;
        this.records = records;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (game.isGameOver()) {
            return;
        }

        JComponent component = (JComponent) e.getSource();
        int rowIndex = view.getCellIndex(component.getY());
        int columnIndex = view.getCellIndex(component.getX());

        boolean isOnlyButton3Pressed = (e.getModifiersEx() & (THREE_BUTTONS_MASK)) == BUTTON_3_DOWN_MASK;

        boolean isBothButtonsOrWheelPressed = (e.getModifiersEx() & (BOTH_BUTTONS_MASK)) == BOTH_BUTTONS_MASK ||
                (e.getModifiersEx() & (THREE_BUTTONS_MASK)) == BUTTON_2_DOWN_MASK;

        boolean cellIsAlreadyOpen = game.getCellState(rowIndex, columnIndex) == Game.CellState.OPEN;

        if (isBothButtonsOrWheelPressed && cellIsAlreadyOpen) {
            Collection<Game.Cell> lastTurnCells = game.tryOpenNeighbors(rowIndex, columnIndex);

            repaintPlayground(lastTurnCells);
        } else if (isOnlyButton3Pressed && view.canMarkCell()) {
            Game.Cell cell = game.changeMark(rowIndex, columnIndex);

            view.changeCellMark(cell);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (game.isGameOver()) {
            return;
        }

        JComponent component = (JComponent) e.getSource();
        int rowIndex = view.getCellIndex(component.getY());
        int columnIndex = view.getCellIndex(component.getX());

        boolean isCellMarked = game.getCellState(rowIndex, columnIndex) == Game.CellState.MARKED;

        boolean isSimpleButton1Click = e.getButton() == MouseEvent.BUTTON1 &&
                (e.getModifiersEx() & THREE_BUTTONS_MASK) == 0;

        if (isSimpleButton1Click && !isCellMarked) {
            Collection<Game.Cell> lastTurnCells = game.openCell(rowIndex, columnIndex);

            turnOnTimerIfNecessary();
            repaintPlayground(lastTurnCells);
        }
    }

    private void turnOnTimerIfNecessary() {
        if (!timer.isOn()) {
            timer.turnOn(new TimerTask() {
                @Override
                public void run() {
                    Duration duration = Duration.between(game.getStartTime(), LocalDateTime.now());
                    view.showTime(duration.toSeconds());
                }
            });
        }
    }

    private void repaintPlayground(Collection<Game.Cell> lastTurnCells) {
        if (!game.isGameOver()) {
            view.showOpenCells(lastTurnCells);
        } else {
            timer.turnOff();
            showGameEnd(lastTurnCells);
        }
    }

    private void showGameEnd(Collection<Game.Cell> lastTurnCells) {
        if (!game.isVictory()) {
            view.showDefeat(game, lastTurnCells);
        } else {
            view.showVictory(lastTurnCells);
            Duration gameDuration = Duration.between(game.getStartTime(), LocalDateTime.now());

            if (records.checkForRecord(game.getGameMode(), gameDuration)) {
                records.setRecord(game.getGameMode(), view.getRecordOwner(), gameDuration);
            }
        }
    }
}
