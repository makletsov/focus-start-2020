package ru.makletsov.minesweeper.view;

import org.jetbrains.annotations.Nullable;
import ru.makletsov.minesweeper.GameMode;
import ru.makletsov.minesweeper.view.listeners.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.stream.IntStream;

public class View {
    static final int MARKUP_PITCH = 4;
    static final int GAP_FACTOR = 2;
    static final int CONTROL_PANEL_ELEMENTS_FACTOR = 8;
    static final int PLAYGROUND_ELEMENTS_FACTOR = 5;
    static final int BORDER_THICKNESS = 2;
    static final double NUMBER_PANEL_PROPORTION = 1.5;
    static final int PLAYGROUND_ELEMENTS_SIZE = MARKUP_PITCH * PLAYGROUND_ELEMENTS_FACTOR;
    static final int CONTROL_PANEL_HEIGHT = MARKUP_PITCH * CONTROL_PANEL_ELEMENTS_FACTOR;
    static final int NUMBER_PANEL_UNIT_HEIGHT = CONTROL_PANEL_HEIGHT - 6;
    static final int NUMBER_PANEL_UNIT_WIDTH = CONTROL_PANEL_HEIGHT * 19 / 48;

    private static final String GET_RECORD_OWNER_NAME_PROMPT = "You set new record! Enter your name:";
    private static final int MAX_TIME_VALUE = 999;

    static final int INITIAL_TIMER_VALUE = 0;

    private GameMode gameMode;
    private final Presenter presenter;
    private final IconsStorage iconsStorage;

    private final JFrame window;
    private GameControlPanel controlPanel;
    private PlaygroundPanel playgroundPanel;
    private final JPanel contentPanel;

    public View(String name, GameMode gameMode, Presenter presenter, IconsStorage iconsStorage) {
        this.gameMode = gameMode;
        this.presenter = presenter;
        this.iconsStorage = iconsStorage;

        window = new JFrame(name);
        contentPanel = initContentPanel();

        MainMenu mainMenu = new MainMenu(gameMode, presenter);
        window.setJMenuBar(mainMenu.getMenuBar());

        window.setIconImage(iconsStorage.getWindowIconImage());
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setContentPane(contentPanel);
        window.addWindowListener(ExitPerformer.get(presenter));
        window.pack();
        window.setVisible(true);
    }

    private JPanel initContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        GridBagConstraints mainPanelConstraints = new GridBagConstraints();

        addControlPanel(panel, mainPanelConstraints);
        addPlaygroundPanel(panel, mainPanelConstraints);

        return panel;
    }

    private void addControlPanel(JPanel panel, GridBagConstraints mainPanelConstraints) {
        int gap = MARKUP_PITCH * GAP_FACTOR;

        mainPanelConstraints.insets = new Insets(gap, gap, 0, gap);
        mainPanelConstraints.fill = GridBagConstraints.HORIZONTAL;

        controlPanel = new GameControlPanel(gameMode, iconsStorage, presenter);

        panel.add(controlPanel.getPanel(), mainPanelConstraints);
    }

    private void addPlaygroundPanel(JPanel panel, GridBagConstraints mainPanelConstraints) {
        int gap = MARKUP_PITCH * GAP_FACTOR;

        mainPanelConstraints.insets = new Insets(gap, gap, gap, gap);
        mainPanelConstraints.gridy = 2;

        playgroundPanel = new PlaygroundPanel(gameMode, iconsStorage);

        addPlaygroundPanelListeners();

        panel.add(playgroundPanel.getPanel(), mainPanelConstraints);
    }

    public void setNewPlayground(GameMode gameMode) {
        if (gameMode == this.gameMode) {
            playgroundPanel.refresh();
        } else {
            this.gameMode = gameMode;

            contentPanel.remove(playgroundPanel.getPanel());

            GridBagConstraints mainPanelConstraints = new GridBagConstraints();
            addPlaygroundPanel(contentPanel, mainPanelConstraints);
            addPlaygroundPanelListeners();

            window.setContentPane(contentPanel);
            window.pack();
        }

        controlPanel.refresh(gameMode);
    }

    private void addPlaygroundPanelListeners() {
        MouseListener cellButtonListener = CellButtonListener.get(presenter);
        MouseListener faceActiveListener = FaceActiveListener.get(this, presenter);

        IntStream.range(0, gameMode.getHeight()).forEach(row ->
            IntStream.range(0, gameMode.getWidth()).forEach(column -> {
                    playgroundPanel.getButton(row, column).addMouseListener(cellButtonListener);
                    playgroundPanel.getButton(row, column).addMouseListener(faceActiveListener);
                }
            ));
    }

    public void showFailedFace() {
        controlPanel.getRestartButton().setLost();
    }

    public void showWinFace() {
        controlPanel.getRestartButton().setWon();
    }

    public void showScaredFace() {
        controlPanel.getRestartButton().setActive();
    }

    public void showNormalFace() {
        controlPanel.getRestartButton().setDefault();
    }

    public void showFailedCell(int rowIndex, int columnIndex) {
        playgroundPanel.getButton(rowIndex, columnIndex).setFailed();
    }

    public void showWrongMarkedCell(int rowIndex, int columnIndex) {
        playgroundPanel.getButton(rowIndex, columnIndex).setWrongMarked();
    }

    public void showMinedCell(int rowIndex, int columnIndex) {
        playgroundPanel.getButton(rowIndex, columnIndex).setMined();
    }

    public void showOpenCell(int rowIndex, int columnIndex, int minesCount) {
        playgroundPanel.getButton(rowIndex, columnIndex).setOpen(minesCount);
    }

    public void showMarkedCell(int rowIndex, int columnIndex, int minesRemains) {
        controlPanel.getMinesCountPanel().setValue(minesRemains);
        playgroundPanel.getButton(rowIndex, columnIndex).setMarked();
    }

    public void showQuestionMarkedCell(int rowIndex, int columnIndex, int minesRemains) {
        controlPanel.getMinesCountPanel().setValue(minesRemains);
        playgroundPanel.getButton(rowIndex, columnIndex).setQuestionMarked();
    }

    public void showDefaultCell(int rowIndex, int columnIndex) {
        playgroundPanel.getButton(rowIndex, columnIndex).setDefault();
    }

    public void showTime(int time) {
        if (time >= 0 && time <= MAX_TIME_VALUE) {
            controlPanel.getTimerPanel().setValue(time);
        }
    }

    public static int getCellIndex(int coordinate) {
        return (coordinate - BORDER_THICKNESS) / (MARKUP_PITCH * PLAYGROUND_ELEMENTS_FACTOR);
    }

    @Nullable
    public String getRecordOwner() {
        return JOptionPane.showInputDialog(GET_RECORD_OWNER_NAME_PROMPT);
    }
}
