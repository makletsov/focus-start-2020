package ru.makletsov.minesweeper.view;

import org.jetbrains.annotations.Nullable;
import ru.makletsov.minesweeper.model.Game;
import ru.makletsov.minesweeper.model.GameMode;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class View extends JComponent {
    private static final int MARKUP_PITCH = 4;
    private static final int GAP_FACTOR = 2;
    private static final int CONTROL_PANEL_ELEMENTS_FACTOR = 8;
    private static final int PLAYGROUND_ELEMENTS_FACTOR = 5;
    private static final int BORDER_THICKNESS = 2;
    private static final double NUMBER_PANEL_PROPORTION = 1.5;
    private static final int HORIZONTAL_GAPS_COUNT = 2;
    private static final int VERTICAL_GAPS_COUNT = 5;
    private static final int PLAYGROUND_ELEMENTS_SIZE = MARKUP_PITCH * PLAYGROUND_ELEMENTS_FACTOR;
    private static final int CONTROL_PANEL_HEIGHT = MARKUP_PITCH * CONTROL_PANEL_ELEMENTS_FACTOR;

    private static final int INITIAL_TIMER_VALUE = 0;

    private final Map<RestartButton.State, Icon> restartButtonIcons;
    private final Map<CellButton.State, Icon> cellStateIcons;
    private final Map<Integer, Icon> minesCountIcons;
    private final List<Image> numbersImages;

    private GameMode gameMode;
    private JFrame frame;
    private MainMenu mainMenu;

    private NumberBoard minesCounter;
    private RestartButton restartButton;
    private NumberBoard timer;

    private CellButton[][] cellButtons;

    public View(String name,
                GameMode gameMode,
                Map<RestartButton.State, Image> restartButtonImages,
                Map<CellButton.State, Image> cellStateImages,
                Map<Integer, Image> minesCountImages,
                List<Image> numbersImages) {

        this.gameMode = gameMode;
        this.restartButtonIcons = getRestartButtonIcons(restartButtonImages);
        this.cellStateIcons = getCellIcons(cellStateImages);
        this.minesCountIcons = getCellIcons(minesCountImages);
        this.numbersImages = numbersImages;

        SwingUtilities.invokeLater(() -> {
            frame = getMainFrame(name);
            frame.setVisible(true);
        });
    }

    private Map<RestartButton.State, Icon> getRestartButtonIcons(Map<RestartButton.State, Image> imagesMap) {
        return imagesMap.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, this::getRestartButtonIcon)
        );
    }

    private Icon getRestartButtonIcon(Map.Entry<?, Image> entry) {
        int size = MARKUP_PITCH * CONTROL_PANEL_ELEMENTS_FACTOR;

        return new ImageIcon(entry.getValue().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    private <T> Map<T, Icon> getCellIcons(Map<T, Image> imagesMap) {
        return imagesMap.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, this::getCellButtonIcon)
        );
    }

    private Icon getCellButtonIcon(Map.Entry<?, Image> entry) {
        int size = MARKUP_PITCH * PLAYGROUND_ELEMENTS_FACTOR;

        return new ImageIcon(entry.getValue().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    private JFrame getMainFrame(String viewName) {
        JFrame frame = new JFrame(viewName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(0, 0, getWindowWidth(gameMode), getWindowHeight(gameMode));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        mainMenu = new MainMenu(gameMode);
        frame.setJMenuBar(mainMenu.getMenuBar());

        JPanel playgroundPanel = initMainPanel(gameMode);
        frame.setContentPane(playgroundPanel);

        frame.pack();
        return frame;
    }

    private int getWindowHeight(GameMode gameMode) {
        int playgroundHeight = gameMode.getHeight() * PLAYGROUND_ELEMENTS_FACTOR;
        int totalGapsHeight = VERTICAL_GAPS_COUNT * GAP_FACTOR;

        return MARKUP_PITCH * (playgroundHeight + totalGapsHeight + CONTROL_PANEL_ELEMENTS_FACTOR);
    }

    private int getWindowWidth(GameMode gameMode) {
        return MARKUP_PITCH *
                (gameMode.getWidth() * PLAYGROUND_ELEMENTS_FACTOR +
                        HORIZONTAL_GAPS_COUNT * GAP_FACTOR);
    }

    private JPanel initMainPanel(GameMode gameMode) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        int gap = MARKUP_PITCH * GAP_FACTOR;

        GridBagConstraints mainPanelConstraints = new GridBagConstraints();

        mainPanelConstraints.insets = new Insets(gap, gap, 0, gap);
        mainPanelConstraints.fill = GridBagConstraints.HORIZONTAL;

        panel.add(initControlPanel(gameMode), mainPanelConstraints);

        mainPanelConstraints.insets = new Insets(gap, gap, gap, gap);
        mainPanelConstraints.gridy = 2;

        panel.add(initPlaygroundPanel(gameMode), mainPanelConstraints);

        return panel;
    }

    private JPanel initControlPanel(GameMode gameMode) {
        JPanel counterPanel = new JPanel(new GridBagLayout());
        counterPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        int gap = MARKUP_PITCH * GAP_FACTOR;

        GridBagConstraints controlPanelConstraints = new GridBagConstraints();
        controlPanelConstraints.insets = new Insets(gap, gap, gap, gap);

        int numberPanelWidth = (int) (CONTROL_PANEL_HEIGHT * NUMBER_PANEL_PROPORTION);

        minesCounter = new NumberBoard(gameMode.getMinesCount(), CONTROL_PANEL_HEIGHT, numberPanelWidth, numbersImages);
        timer = new NumberBoard(INITIAL_TIMER_VALUE, CONTROL_PANEL_HEIGHT, numberPanelWidth, numbersImages);

        restartButton = new RestartButton(CONTROL_PANEL_HEIGHT, restartButtonIcons);

        List<JComponent> components = List.of(minesCounter, restartButton.getButton(), timer);

        IntStream.range(0, components.size()).forEach(index -> {
            controlPanelConstraints.gridx = index;
            counterPanel.add(components.get(index), controlPanelConstraints);
        });

        return counterPanel;
    }

    private JPanel initPlaygroundPanel(GameMode gameMode) {
        int width = gameMode.getWidth();
        int height = gameMode.getHeight();

        cellButtons = new CellButton[height][width];

        JPanel playgroundPanel = new JPanel();

        playgroundPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        playgroundPanel.setLayout(new GridLayout(height, width));

        IntStream.range(0, height)
                .forEach(i -> IntStream.range(0, width)
                        .forEach(j -> {
                            cellButtons[i][j] = new CellButton(PLAYGROUND_ELEMENTS_SIZE, cellStateIcons, minesCountIcons);
                            playgroundPanel.add(cellButtons[i][j].getButton());
                        }));

        return playgroundPanel;
    }

    public void showDefeat(Game game, Collection<Game.Cell> lastTurnCells) {
        SwingUtilities.invokeLater(() -> {
            showWrongMarkedCells(game.getMarkedCells().stream()
                    .filter(cell -> !cell.isMined())
                    .collect(Collectors.toList()));
            showMinedCells(game.getMinedCells().stream()
                    .filter(cell -> cell.getState() != Game.CellState.MARKED)
                    .collect(Collectors.toList()));
            showFailedCells(lastTurnCells.stream()
                    .filter(Game.Cell::isMined)
                    .collect(Collectors.toList()));
            restartButton.setLost();
        });
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
                .map(cell -> {
                    int rowIndex = cell.getRowIndex();
                    int columnIndex = cell.getColumnIndex();

                    return cellButtons[rowIndex][columnIndex];
                })
                .collect(Collectors.toList());
    }

    public void showVictory(Collection<Game.Cell> lastTurnCells) {
        SwingUtilities.invokeLater(() -> {
            showOpenCells(lastTurnCells);
            restartButton.setWon();
        });
    }

    public void showOpenCells(Collection<Game.Cell> cells) {
        SwingUtilities.invokeLater(() -> {
            for (Game.Cell cell : cells) {
                int rowIndex = cell.getRowIndex();
                int columnIndex = cell.getColumnIndex();
                int minesCount = cell.getMinedNeighboursCount();

                cellButtons[rowIndex][columnIndex].setOpen(minesCount);
            }
        });
    }

    public void changeCellMark(Game.Cell cell) {
        SwingUtilities.invokeLater(() -> {
            CellButton button = cellButtons[cell.getRowIndex()][cell.getColumnIndex()];

            switch (cell.getState()) {
                case MARKED:
                    minesCounter.decreaseValue();
                    button.setMarked();
                    break;
                case QUESTION_MARKED:
                    minesCounter.increaseValue();
                    button.setQuestionMarked();
                    break;
                case DEFAULT:
                    button.setDefault();
                    break;
            }
        });
    }

    public boolean canMarkCell() {
        return minesCounter.canDecrease();
    }

    public void showTime(long time) {
        if (time >= 0 && time <= 999) {
            SwingUtilities.invokeLater(() -> timer.setValue(time));
        }
    }

    public void setNewPlayground(GameMode gameMode) {
        this.gameMode = gameMode;

        SwingUtilities.invokeLater(() -> {
            JPanel mainPanel = initMainPanel(gameMode);

            frame.setContentPane(mainPanel);
            frame.pack();
        });
    }

    public void addRestartGameListener(ActionListener actionListener) {
        SwingUtilities.invokeLater(() -> {
            mainMenu.addNewGameListener(actionListener);
            restartButton.getButton().addActionListener(actionListener);
        });
    }

    public void addExitGameListeners(ActionListener actionListener, WindowListener windowListener) {
        SwingUtilities.invokeLater(() ->
        {
            mainMenu.addExitListener(actionListener);
            frame.addWindowListener(windowListener);
        });
    }

    public void addShowRecordsListener(ActionListener actionListener) {
        SwingUtilities.invokeLater(() ->
                mainMenu.addRecordsTableListener(actionListener));
    }

    public void addShowAboutListener(ActionListener actionListener) {
        SwingUtilities.invokeLater(() ->
                mainMenu.addAboutListener(actionListener));
    }

    public void addChangeModeListener(GameMode gameMode, ActionListener actionListener) {
        SwingUtilities.invokeLater(() ->
                mainMenu.addGameModeListener(gameMode, actionListener));
    }

    public void addCellButtonListener(MouseAdapter mouseListener) {
        SwingUtilities.invokeLater(() -> {
            for (CellButton[] buttonRow : cellButtons) {
                for (CellButton button : buttonRow) {
                    button.addMouseListener(mouseListener);
                }
            }
        });
    }

    //TODO: delegate from markup
    public int getCellIndex(int coordinate) {
        return (coordinate - BORDER_THICKNESS) / (MARKUP_PITCH * PLAYGROUND_ELEMENTS_FACTOR);
    }

    @Nullable
    public String getRecordOwner() {
        return JOptionPane.showInputDialog("You set new record! Enter your name:");
    }
}
