package ru.makletsov.minesweeper.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class CellButton {
    private static final int MIN_MINES_COUNT = 0;
    private static final int MAX_MINES_COUNT = 8;

    private final JButton button;
    private final IconsStorage iconsStorage;

    public CellButton(int buttonSize, IconsStorage iconsStorage) {
        if (buttonSize < 0) {
            throw new IllegalArgumentException("Size must be positive.");
        }

        this.iconsStorage = iconsStorage;

        button = new JButton(getIcon(State.DEFAULT));

        button.setPressedIcon(getIcon(State.PRESSED));
        button.setDisabledIcon(getIcon(State.DEFAULT));
        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
    }

    private Icon getIcon(CellButton.State state) {
        return iconsStorage.getCellButtonIcon(state);
    }

    private Icon getIcon(int minesCount) {
        return iconsStorage.getCellButtonIcon(minesCount);
    }

    public JButton getButton() {
        return button;
    }

    public void setDefault() {
        button.setIcon(getIcon(State.DEFAULT));
        button.setDisabledIcon(getIcon(State.DEFAULT));
        button.setPressedIcon(getIcon(State.PRESSED));
    }

    public void setMarked() {
        button.setIcon(getIcon(State.MARKED));
        button.setDisabledIcon(getIcon(State.MARKED));
        button.setPressedIcon(getIcon(State.MARKED));
    }

    public void setQuestionMarked() {
        button.setIcon(getIcon(State.QUESTION_MARKED));
        button.setDisabledIcon(getIcon(State.DEFAULT));
        button.setPressedIcon(getIcon(State.PRESSED));
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
        button.setDisabledIcon(getIcon(buttonState));
        button.setEnabled(false);
    }

    public void setOpen(int minesCount) {
        if (minesCount < 0) {
            throw new IllegalArgumentException("Given mines count is less than " + MIN_MINES_COUNT);
        }

        if (minesCount > 8) {
            throw new IllegalArgumentException("Given mines count is greater than " + MAX_MINES_COUNT);
        }

        button.setDisabledIcon(getIcon(minesCount));
        button.setEnabled(false);
    }

    public void addMouseListener(MouseListener mouseListener) {
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
