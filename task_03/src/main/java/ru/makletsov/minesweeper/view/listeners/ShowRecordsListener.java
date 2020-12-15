package ru.makletsov.minesweeper.view.listeners;

import javax.swing.*;
import java.awt.event.ActionListener;

import ru.makletsov.minesweeper.view.GameManipulator;
import ru.makletsov.minesweeper.view.RecordsPanel;

public class ShowRecordsListener {
    private static final String RECORDS_PANEL_TITLE = "Records";

    public static ActionListener get(GameManipulator gameActionsPerformer) {
        return e -> JOptionPane.showMessageDialog(
            null,
            new RecordsPanel(gameActionsPerformer.getRecords()).getPanel(),
            RECORDS_PANEL_TITLE,
            JOptionPane.PLAIN_MESSAGE
        );
    }
}
