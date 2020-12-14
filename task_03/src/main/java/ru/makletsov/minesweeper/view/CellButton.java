package ru.makletsov.minesweeper.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.Map;

public class CellButton {
    private static final int MIN_MINES_COUNT = 0;
    private static final int MAX_MINES_COUNT = 8;

    private final JButton button;
    private final Map<State, Icon> stateIcons;
    private final Map<Integer, Icon> minesCountIcons;

    public CellButton(int buttonSize, Map<State, Icon> stateIcons, Map<Integer, Icon> minesCountIcons) {
        if (buttonSize < 0) {
            throw new IllegalArgumentException("Size must be positive.");
        }

        this.stateIcons = stateIcons;
        this.minesCountIcons = minesCountIcons;

        button = new JButton(stateIcons.get(State.DEFAULT));

        button.setPressedIcon(stateIcons.get(State.PRESSED));
        button.setDisabledIcon(stateIcons.get(State.DEFAULT));
        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
    }

    public JButton getButton() {
        return button;
    }

    public void setDefault() {
        button.setIcon(stateIcons.get(State.DEFAULT));
        button.setDisabledIcon(stateIcons.get(State.DEFAULT));
        button.setPressedIcon(stateIcons.get(State.PRESSED));
    }

    public void setMarked() {
        button.setIcon(stateIcons.get(State.MARKED));
        button.setDisabledIcon(stateIcons.get(State.MARKED));
        button.setPressedIcon(stateIcons.get(State.MARKED));
    }

    public void setQuestionMarked() {
        button.setIcon(stateIcons.get(State.QUESTION_MARKED));
        button.setDisabledIcon(stateIcons.get(State.DEFAULT));
        button.setPressedIcon(stateIcons.get(State.PRESSED));
    }

    public void setWrongMarked() {
        setStateAndDisable(State.WRONG_MARKED);
    }

    public void setFailed() {
        setStateAndDisable(State.FAILED);
    }

    public void setMined() {
        setStateAndDisable(State.MINED);
    }

    private void setStateAndDisable(State buttonState) {
        button.setDisabledIcon(stateIcons.get(buttonState));
        button.setEnabled(false);
    }

    public void setOpen(int minesCount) {
        if (minesCount < 0) {
            throw new IllegalArgumentException("Given mines count is less than " + MIN_MINES_COUNT);
        }

        if (minesCount > 8) {
            throw new IllegalArgumentException("Given mines count is greater than " + MAX_MINES_COUNT);
        }

        button.setDisabledIcon(minesCountIcons.get(minesCount));
        button.setEnabled(false);
    }

    public void disable() {
        button.setEnabled(false);
    }

    public void addMouseListener(MouseAdapter mouseListener) {
        button.addMouseListener(mouseListener);
    }

    public enum State {
        DEFAULT("default"),
        FAILED("failed"),
        MARKED("marked"),
        MINED("mined"),
        PRESSED("pressed"),
        QUESTION_MARKED("question_marked"),
        WRONG_MARKED("wrong_marked");

        private final String iconName;

        State(String iconName) {
            this.iconName = iconName;
        }

        public String getIconName() {
            return iconName;
        }
    }
}
