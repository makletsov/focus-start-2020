package ru.makletsov.minesweeper.view;

import ru.makletsov.minesweeper.model.GameMode;
import ru.makletsov.minesweeper.model.Record;
import ru.makletsov.minesweeper.model.Records;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class RecordsTable {
    private static final String POSTFIX = " sec.";
    private static final int COLUMNS_COUNT = 3;

    private final JPanel panel;

    public RecordsTable(Records records) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, COLUMNS_COUNT));

        for (GameMode gameMode : EnumSet.allOf(GameMode.class)) {
            Record record = records.getRecord(gameMode);
            panel.add(new JLabel(record.getOwner() + "  "));
            panel.add(new JLabel(gameMode.getPrettyString() + "  "));
            panel.add(new JLabel(record.getDurationInSeconds() + POSTFIX));
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
