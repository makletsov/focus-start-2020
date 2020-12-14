package ru.makletsov.minesweeper.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class RestartButton {
    private final JButton button;
    private final Map<State, Icon> icons;

    public RestartButton(int buttonSize, Map<State, Icon> icons) {
        if (buttonSize < 0) {
            throw new IllegalArgumentException("Size must be positive.");
        }

        this.icons = icons;

        button = new JButton(icons.get(State.DEFAULT));

        button.setPressedIcon(icons.get(State.PRESSED));
        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));

        button.addActionListener(actionEvent ->
                button.setIcon(icons.get(State.DEFAULT)));
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
        button.setIcon(icons.get(state));
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
