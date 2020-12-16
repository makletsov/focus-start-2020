package ru.makletsov.minesweeper.view;

import javax.swing.*;
import java.awt.*;

public class RestartButton {
    private final JButton button;
    private final IconsStorage iconsStorage;

    public RestartButton(int buttonSize, IconsStorage iconsStorage, Presenter presenter) {
        if (buttonSize < 0) {
            throw new IllegalArgumentException("Size must be positive.");
        }

        this.iconsStorage = iconsStorage;

        button = new JButton(iconsStorage.getRestartButtonIcon(State.DEFAULT));

        button.setPressedIcon(iconsStorage.getRestartButtonIcon(State.PRESSED));
        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));

        button.addActionListener(e -> presenter.restartGame());
        button.addActionListener(actionEvent ->
                button.setIcon(iconsStorage.getRestartButtonIcon(State.DEFAULT)));
    }

    public void setDefault() {
        setState(State.DEFAULT);
    }

    public void setActive() {
        setState(State.ACTIVE);
    }

    public void setLost() {
        setState(State.LOST);
    }

    public void setWon() {
        setState(State.WON);
    }

    public JButton getButton() {
        return button;
    }

    private void setState(State state) {
        button.setIcon(iconsStorage.getRestartButtonIcon(state));
    }

    public enum State {
        ACTIVE("active"),
        DEFAULT("default"),
        LOST("lost"),
        PRESSED("pressed"),
        WON("won");

        private final String iconName;

        State(String iconName) {
            this.iconName = iconName;
        }

        public String getIconName() {
            return iconName;
        }
    }
}
