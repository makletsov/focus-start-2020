package ru.makletsov.minesweeper.view;

import org.jetbrains.annotations.Nullable;
import ru.makletsov.minesweeper.model.Game;
import ru.makletsov.minesweeper.model.GameMode;

import javax.swing.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class View {
    private static final String GET_RECORD_OWNER_NAME_PROMPT = "You set new record! Enter your name:";

    private final Markup markup;
    private final MainMenu mainMenu;
    private final JFrame window;

    private GameMode gameMode;

    public View(String name, GameMode gameMode, Markup markup) {
        this.gameMode = gameMode;
        this.markup = markup;

        mainMenu = new MainMenu(gameMode);
        window = new JFrame(name);

        window.setIconImage(markup.getWindowIconImage());
        window.setJMenuBar(mainMenu.getMenuBar());
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setContentPane(markup.getNewView(gameMode));
        window.pack();
        window.setVisible(true);
    }

    public void showDefeat(Game game, Collection<Game.Cell> lastTurnCells) {
        showWrongMarkedCells(game.getMarkedCells().stream()
                .filter(cell -> !cell.isMined())
                .collect(Collectors.toList()));
        showMinedCells(game.getMinedCells().stream()
                .filter(cell -> cell.getState() != Game.CellState.MARKED)
                .collect(Collectors.toList()));
        showFailedCells(lastTurnCells.stream()
                .filter(Game.Cell::isMined)
                .collect(Collectors.toList()));

        SwingUtilities.invokeLater(() ->
                markup.getRestartButton().setLost());
    }

    private void showFailedCells(Collection<Game.Cell> cells) {
        SwingUtilities.invokeLater(() ->
                getCellButtons(cells).forEach(CellButton::setFailed));
    }

    private void showWrongMarkedCells(Collection<Game.Cell> cells) {
        SwingUtilities.invokeLater(() ->
                getCellButtons(cells).forEach(CellButton::setWrongMarked));
    }

    private void showMinedCells(Collection<Game.Cell> cells) {
        SwingUtilities.invokeLater(() ->
                getCellButtons(cells).forEach(CellButton::setMined));
    }

    private Collection<CellButton> getCellButtons(Collection<Game.Cell> cells) {
        return cells.stream()
                .map(markup::getCellButton)
                .collect(Collectors.toList());
    }

    public void showVictory(Collection<Game.Cell> lastTurnCells) {
        showOpenCells(lastTurnCells);

        SwingUtilities.invokeLater(() ->
                markup.getRestartButton().setWon());
    }

    public void showOpenCells(Collection<Game.Cell> cells) {
        SwingUtilities.invokeLater(() -> cells.forEach(this::openCell));
    }

    public void showScaredFace() {
        markup.getRestartButton().setActive();
    }

    public void showNormalFace() {
        markup.getRestartButton().setDefault();
    }

    private void openCell(Game.Cell cell) {
        int minesCount = cell.getMinedNeighboursCount();
        markup.getCellButton(cell).setOpen(minesCount);
    }

    public void changeCellMark(Game.Cell cell) {
        CellButton button = markup.getCellButton(cell);

        SwingUtilities.invokeLater(() -> {
            switch (cell.getState()) {
                case MARKED:
                    markup.getMinesCounter().decreaseValue();
                    button.setMarked();
                    break;
                case QUESTION_MARKED:
                    markup.getMinesCounter().increaseValue();
                    button.setQuestionMarked();
                    break;
                case DEFAULT:
                    button.setDefault();
                    break;
            }
        });
    }

    public boolean canMarkCell() {
        return markup.getMinesCounter().canDecrease();
    }

    public void showTime(long time) {
        if (time >= 0 && time <= 999) {
            SwingUtilities.invokeLater(() -> markup.getTimer().setValue(time));
        }
    }

    public void setNewPlayground(GameMode gameMode) {
        this.gameMode = gameMode;

        window.setContentPane(markup.getNewView(gameMode));
        window.pack();
    }

    public void addRestartGameListener(ActionListener actionListener) {
        mainMenu.addNewGameListener(actionListener);
        markup.getRestartButton().getButton().addActionListener(actionListener);
    }

    public void addExitGameListeners(ActionListener actionListener, WindowListener windowListener) {
        mainMenu.addExitListener(actionListener);
        window.addWindowListener(windowListener);
    }

    public void addShowRecordsListener(ActionListener actionListener) {
        mainMenu.addRecordsTableListener(actionListener);
    }

    public void addShowAboutListener(ActionListener actionListener) {
        mainMenu.addAboutListener(actionListener);
    }

    public void addChangeModeListener(GameMode gameMode, ActionListener actionListener) {
        mainMenu.addGameModeListener(gameMode, actionListener);
    }

    public void addCellButtonListener(MouseAdapter mouseListener) {
        IntStream.range(0, gameMode.getHeight())
                .forEach(row ->
                        IntStream.range(0, gameMode.getWidth())
                                .forEach(column ->
                                        markup.getCellButton(row, column).addMouseListener(mouseListener)
                                ));
    }

    public int getCellIndex(int coordinate) {
        return markup.getCellIndex(coordinate);
    }

    @Nullable
    public String getRecordOwner() {
        return JOptionPane.showInputDialog(GET_RECORD_OWNER_NAME_PROMPT);
    }
}
