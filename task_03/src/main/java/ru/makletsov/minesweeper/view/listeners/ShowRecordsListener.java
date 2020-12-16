package ru.makletsov.minesweeper.view.listeners;

import javax.swing.*;
import java.awt.event.ActionListener;

import ru.makletsov.minesweeper.view.Presenter;
import ru.makletsov.minesweeper.view.RecordsPanel;

public class ShowRecordsListener {
    private static final String RECORDS_PANEL_TITLE = "Records";

    public static ActionListener get(Presenter presenter) {
        return e -> JOptionPane.showMessageDialog(
            null,
            new RecordsPanel(presenter.getRecords()).getPanel(),
            RECORDS_PANEL_TITLE,
            JOptionPane.PLAIN_MESSAGE
        );
    }
}
