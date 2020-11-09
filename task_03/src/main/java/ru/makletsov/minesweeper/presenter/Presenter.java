package ru.makletsov.minesweeper.presenter;

import ru.makletsov.minesweeper.view.*;
import ru.makletsov.minesweeper.model.*;
import ru.makletsov.minesweeper.model.Records;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Presenter {
    private static final long TIMER_DELAY = 1000;
    private static final long TIMER_PERIOD = 1000;
    private static final String WINDOW_TITLE = "Minesweeper";
    private static final String RECORDS_PANEL_TITLE = "Records";
    private static final String ABOUT_PANEL_TITLE = "About";
    private static final String ABOUT_PANEL_MASSAGE =
            "Made by Makletsov Vasily." + System.lineSeparator() +
                    "Novosibirsk, 2020.";

    private final Records records;
    private final List<RecordsConsumer> recordsConsumers;
    private final GameTimer timer;
    private final View view;
    private final RestartGameListener restartGameListener;
    private final FaceActiveListener faceActiveListener;

    private GameMode gameMode;
    private Game game;

    public Presenter(GameMode gameMode, Records records, Markup markup) {
        this.gameMode = gameMode;
        this.records = records;

        timer = new GameTimer(TIMER_DELAY, TIMER_PERIOD);
        view = new View(WINDOW_TITLE, gameMode, markup);
        game = new Game(gameMode);
        restartGameListener = new RestartGameListener();
        faceActiveListener = new FaceActiveListener();
        recordsConsumers = new ArrayList<>();

        addTemporaryListeners();
        addFinalListeners();
    }

    private void addFinalListeners() {
        for (GameMode mode : EnumSet.allOf(GameMode.class)) {
            view.addChangeModeListener(mode, e -> startNewGame(mode));
        }

        view.addShowRecordsListener(new ShowRecordsListener());
        view.addExitGameListeners(e -> notifyRecordsConsumers(), new ExitPerformer());
        view.addShowAboutListener(new AboutListener());
    }

    private void addTemporaryListeners() {
        view.addRestartGameListener(restartGameListener);
        view.addCellButtonListener(faceActiveListener);
        view.addCellButtonListener(new CellButtonListener(game, view, timer, records));
    }

    public void startNewGame() {
        game = new Game(gameMode);

        view.setNewPlayground(gameMode);
        timer.turnOff();

        addTemporaryListeners();
    }

    public void startNewGame(GameMode gameMode) {
        this.gameMode = gameMode;
        startNewGame();
    }

    public boolean isGameStarted() {
        return timer.isOn() || game.isGameOver();
    }

    public void addRecordsConsumer(RecordsConsumer recordsConsumer) {
        recordsConsumers.add(recordsConsumer);
    }

    public void notifyRecordsConsumers() {
        try {
            recordsConsumers.forEach(c -> c.consumeRecords(records));
        } catch (Exception e) {
            //state saving attempt is failed, exit anyway
        }

        System.exit(0);
    }

    private class ExitPerformer extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent e) {
            notifyRecordsConsumers();
        }
    }

    private class RestartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isGameStarted()) {
                startNewGame();
            }
        }
    }

    private static class AboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            ABOUT_PANEL_MASSAGE, ABOUT_PANEL_TITLE, JOptionPane.INFORMATION_MESSAGE));
        }
    }

    public class ShowRecordsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            new RecordsTable(records).getPanel(), RECORDS_PANEL_TITLE, JOptionPane.PLAIN_MESSAGE));
        }
    }

    public class FaceActiveListener extends MouseAdapter {
        private static final int BUTTON_1_DOWN_MASK = InputEvent.BUTTON1_DOWN_MASK;
        private static final int BUTTON_2_DOWN_MASK = InputEvent.BUTTON2_DOWN_MASK;
        private static final int BOTH_BUTTONS_MASK = InputEvent.BUTTON1_DOWN_MASK
                | InputEvent.BUTTON3_DOWN_MASK;

        @Override
        public void mousePressed(MouseEvent e) {
            if (!game.isGameOver() &&
                    (e.getModifiersEx() & BUTTON_1_DOWN_MASK) == BUTTON_1_DOWN_MASK ||
                    (e.getModifiersEx() & BOTH_BUTTONS_MASK) == BOTH_BUTTONS_MASK ||
                    (e.getModifiersEx() & BUTTON_2_DOWN_MASK) == BUTTON_2_DOWN_MASK) {
                view.showScaredFace();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!game.isGameOver()) {
                view.showNormalFace();
            }
        }
    }
}
