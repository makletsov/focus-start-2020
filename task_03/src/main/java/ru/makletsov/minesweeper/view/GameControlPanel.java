package ru.makletsov.minesweeper.view;

import ru.makletsov.minesweeper.GameMode;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.List;

public class GameControlPanel {

    private final JPanel panel;
    private final NumberBoard minesCounter;
    private final NumberBoard timer;
    private final RestartButton restartButton;

    public GameControlPanel(GameMode gameMode, IconsStorage iconsStorage) {
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        int gap = View.MARKUP_PITCH * View.GAP_FACTOR;

        GridBagConstraints controlPanelConstraints = new GridBagConstraints();
        controlPanelConstraints.insets = new Insets(gap, gap, gap, gap);

        int numberPanelWidth = (int)
            (View.CONTROL_PANEL_HEIGHT * View.NUMBER_PANEL_PROPORTION);

        minesCounter = new NumberBoard(
            gameMode.getMinesCount(),
            View.CONTROL_PANEL_HEIGHT,
            numberPanelWidth,
            iconsStorage
        );

        timer = new NumberBoard(
            View.INITIAL_TIMER_VALUE,
            View.CONTROL_PANEL_HEIGHT,
            numberPanelWidth,
            iconsStorage
        );

        restartButton = new RestartButton(
            View.CONTROL_PANEL_HEIGHT,
            iconsStorage
        );

        int counter = 0;

        for (JComponent component : List.of(minesCounter, restartButton.getButton(), timer)) {
            controlPanelConstraints.gridx = counter;
            counter++;

            panel.add(component, controlPanelConstraints);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public NumberBoard getMinesCounter() {
        return minesCounter;
    }

    public NumberBoard getTimer() {
        return timer;
    }

    public RestartButton getRestartButton() {
        return restartButton;
    }

    public void refresh(GameMode gameMode) {
        timer.setValue(0);
        minesCounter.setValue(gameMode.getMinesCount());
        restartButton.setDefault();
    }
}
