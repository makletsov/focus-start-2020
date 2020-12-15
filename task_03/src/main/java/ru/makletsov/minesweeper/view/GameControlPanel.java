package ru.makletsov.minesweeper.view;

import ru.makletsov.minesweeper.GameMode;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.List;

public class GameControlPanel {
    private final JPanel panel;
    private final DigitPanel minesCountPanel;
    private final DigitPanel timerPanel;
    private final RestartButton restartButton;

    public GameControlPanel(GameMode gameMode, IconsStorage iconsStorage) {
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        int gap = View.MARKUP_PITCH * View.GAP_FACTOR;

        GridBagConstraints controlPanelConstraints = new GridBagConstraints();
        controlPanelConstraints.insets = new Insets(gap, gap, gap, gap);

        int numberPanelWidth = (int)
            (View.CONTROL_PANEL_HEIGHT * View.NUMBER_PANEL_PROPORTION);

        minesCountPanel = new DigitPanel(
            gameMode.getMinesCount(),
            View.CONTROL_PANEL_HEIGHT,
            numberPanelWidth,
            iconsStorage
        );

        timerPanel = new DigitPanel(
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

        List<JComponent> componentsList =
            List.of(minesCountPanel.getPanel(), restartButton.getButton(), timerPanel.getPanel());

        for (JComponent component : componentsList) {
            controlPanelConstraints.gridx = counter;
            counter++;

            panel.add(component, controlPanelConstraints);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    public DigitPanel getMinesCountPanel() {
        return minesCountPanel;
    }

    public DigitPanel getTimerPanel() {
        return timerPanel;
    }

    public RestartButton getRestartButton() {
        return restartButton;
    }

    public void refresh(GameMode gameMode) {
        timerPanel.setValue(0);
        minesCountPanel.setValue(gameMode.getMinesCount());
        restartButton.setDefault();
    }
}
