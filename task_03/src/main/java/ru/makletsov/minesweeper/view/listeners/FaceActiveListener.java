package ru.makletsov.minesweeper.view.listeners;

import ru.makletsov.minesweeper.view.View;
import ru.makletsov.minesweeper.view.GameManipulator;

import java.awt.event.*;

public class FaceActiveListener extends MouseAdapter {
    public static MouseListener get(View view, GameManipulator gameManipulator) {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameManipulator.isGameInProcess()) {
                    view.showScaredFace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (gameManipulator.isGameInProcess()) {
                    view.showNormalFace();
                }
            }
        };
    }
}
