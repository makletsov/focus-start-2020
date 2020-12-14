package ru.makletsov.minesweeper.view;

import java.awt.*;
import javax.swing.*;
import java.util.Collection;

import ru.makletsov.minesweeper.model.Record;

public class RecordsPanel {
    private static final String POSTFIX = " sec.";
    private static final int COLUMNS_COUNT = 3;

    private final JPanel panel;

    public RecordsPanel(Collection<Record> records) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, COLUMNS_COUNT));

        for(Record record: records) {
            panel.add(new JLabel(record.getOwner() + "  "));
            panel.add(new JLabel(record.getGameMode().getName() + "  "));
            panel.add(new JLabel(record.getDuration() + POSTFIX));
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
