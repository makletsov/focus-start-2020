package ru.makletsov.minesweeper.view.listeners;

import ru.makletsov.minesweeper.view.Presenter;
import ru.makletsov.minesweeper.view.View;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CellButtonListener {
    private static final int BUTTON_2_DOWN_MASK = InputEvent.BUTTON2_DOWN_MASK;
    private static final int BUTTON_3_DOWN_MASK = InputEvent.BUTTON3_DOWN_MASK;
    private static final int BOTH_BUTTONS_MASK = InputEvent.BUTTON1_DOWN_MASK
        | InputEvent.BUTTON3_DOWN_MASK;
    private static final int THREE_BUTTONS_MASK = InputEvent.BUTTON1_DOWN_MASK
        | InputEvent.BUTTON2_DOWN_MASK | InputEvent.BUTTON3_DOWN_MASK;

    public static MouseListener get(Presenter presenter) {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                int rowIndex = View.getCellIndex(component.getY());
                int columnIndex = View.getCellIndex(component.getX());

                boolean isOnlyButton3Pressed = (e.getModifiersEx() & (THREE_BUTTONS_MASK)) == BUTTON_3_DOWN_MASK;

                boolean isBothButtonsOrWheelPressed = (e.getModifiersEx() & (BOTH_BUTTONS_MASK)) == BOTH_BUTTONS_MASK ||
                    (e.getModifiersEx() & (THREE_BUTTONS_MASK)) == BUTTON_2_DOWN_MASK;

                if (isBothButtonsOrWheelPressed) {
                    presenter.tryOpenNeighbors(rowIndex, columnIndex);
                } else if (isOnlyButton3Pressed) {
                    presenter.changeCellMark(rowIndex, columnIndex);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();
                int rowIndex = View.getCellIndex(component.getY());
                int columnIndex = View.getCellIndex(component.getX());

                boolean isSimpleButton1Click = e.getButton() == MouseEvent.BUTTON1 &&
                    (e.getModifiersEx() & THREE_BUTTONS_MASK) == 0;

                if (isSimpleButton1Click) {
                    presenter.openCell(rowIndex, columnIndex);
                }
            }
        };
    }
}
