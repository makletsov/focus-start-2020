package ru.makletsov.minesweeper.view.listeners;

import ru.makletsov.minesweeper.view.GameManipulator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExitPerformer {
    public static WindowAdapter get(GameManipulator gameActionsPerformer) {
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                gameActionsPerformer.saveRecordsAndExit();
                super.windowClosed(e);
            }
        };
    }
}
