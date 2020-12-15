package ru.makletsov.minesweeper.view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.stream.IntStream;

import ru.makletsov.minesweeper.GameMode;

public class PlaygroundPanel {
    private final CellButton[][] cellButtons;
    private final JPanel panel;

    public PlaygroundPanel(GameMode gameMode, IconsStorage iconsStorage) {
        int width = gameMode.getWidth();
        int height = gameMode.getHeight();

        cellButtons = new CellButton[height][width];

        panel = new JPanel();

        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panel.setLayout(new GridLayout(height, width));

        IntStream.range(0, height).forEach(row ->
            IntStream.range(0, width).forEach(column ->
                addNewButton(row, column, iconsStorage)
            ));
    }

    private void addNewButton(int rowIndex, int columnIndex, IconsStorage iconsStorage) {
        cellButtons[rowIndex][columnIndex] = new CellButton(View.PLAYGROUND_ELEMENTS_SIZE, iconsStorage);

        panel.add(cellButtons[rowIndex][columnIndex].getButton());
    }

    public JPanel getPanel() {
        return panel;
    }

    public CellButton getButton(int rowIndex, int columnIndex) {
        return cellButtons[rowIndex][columnIndex];
    }

    public void refresh() {
        for (CellButton[] cellButtonsRow : cellButtons) {
            for (CellButton cellButton : cellButtonsRow) {
                cellButton.setDefault();
            }
        }
    }
}
