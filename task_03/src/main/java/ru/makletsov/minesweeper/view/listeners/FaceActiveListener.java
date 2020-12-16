package ru.makletsov.minesweeper.view.listeners;

import ru.makletsov.minesweeper.view.View;
import ru.makletsov.minesweeper.view.Presenter;

import java.awt.event.*;

public class FaceActiveListener extends MouseAdapter {
    public static MouseListener get(View view, Presenter presenter) {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (presenter.isGameInProcess()) {
                    view.showScaredFace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (presenter.isGameInProcess()) {
                    view.showNormalFace();
                }
            }
        };
    }
}
