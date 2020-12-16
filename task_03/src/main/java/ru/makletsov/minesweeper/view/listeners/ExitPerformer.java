package ru.makletsov.minesweeper.view.listeners;

import ru.makletsov.minesweeper.view.Presenter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExitPerformer {
    public static WindowAdapter get(Presenter presenter) {
        return new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                presenter.saveRecordsAndExit();
                super.windowClosed(e);
            }
        };
    }
}
