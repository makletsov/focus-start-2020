package ru.makletsov.minesweeper.view;

import ru.makletsov.minesweeper.model.Game;
import ru.makletsov.minesweeper.model.GameMode;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Markup {
    private static final int MARKUP_PITCH = 4;
    private static final int GAP_FACTOR = 2;
    private static final int CONTROL_PANEL_ELEMENTS_FACTOR = 8;
    private static final int PLAYGROUND_ELEMENTS_FACTOR = 5;
    private static final int BORDER_THICKNESS = 2;
    private static final double NUMBER_PANEL_PROPORTION = 1.5;
    private static final int PLAYGROUND_ELEMENTS_SIZE = MARKUP_PITCH * PLAYGROUND_ELEMENTS_FACTOR;
    private static final int CONTROL_PANEL_HEIGHT = MARKUP_PITCH * CONTROL_PANEL_ELEMENTS_FACTOR;

    private static final int INITIAL_TIMER_VALUE = 0;

    private final Image windowIconImage;
    private final Map<RestartButton.State, Icon> restartButtonIcons;
    private final Map<CellButton.State, Icon> cellStateIcons;
    private final Map<Integer, Icon> minesCountIcons;
    private final List<Image> numbersImages;

    private NumberBoard minesCounter;
    private RestartButton restartButton;
    private NumberBoard timer;
    private CellButton[][] cellButtons;

    public Markup(Image windowIconImage,
                  Map<RestartButton.State, Image> restartButtonImages,
                  Map<CellButton.State, Image> cellStateImages,
                  Map<Integer, Image> minesCountImages,
                  List<Image> numbersImages) {
        this.windowIconImage = windowIconImage;
        this.restartButtonIcons = getRestartButtonIcons(restartButtonImages);
        this.cellStateIcons = getCellIcons(cellStateImages);
        this.minesCountIcons = getCellIcons(minesCountImages);
        this.numbersImages = numbersImages;
    }

    public Image getWindowIconImage() {
        return windowIconImage;
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

        java.util.List<JComponent> components = List.of(minesCounter, restartButton.getButton(), timer);

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

    public int getCellIndex(int coordinate) {
        return (coordinate - BORDER_THICKNESS) / (MARKUP_PITCH * PLAYGROUND_ELEMENTS_FACTOR);
    }

    public NumberBoard getMinesCounter() {
        return minesCounter;
    }

    public RestartButton getRestartButton() {
        return restartButton;
    }

    public NumberBoard getTimer() {
        return timer;
    }

    public JPanel getNewView(GameMode gameMode) {
        return initMainPanel(gameMode);
    }

    public CellButton getCellButton(int rowIndex, int columnIndex) {
        return cellButtons[rowIndex][columnIndex];
    }

    public CellButton getCellButton(Game.Cell cell) {
        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();

        return getCellButton(rowIndex, columnIndex);
    }
}
