package ru.makletsov.minesweeper.view;

import ru.makletsov.minesweeper.GameMode;
import ru.makletsov.minesweeper.view.listeners.AboutListener;
import ru.makletsov.minesweeper.view.listeners.ShowRecordsListener;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MainMenu {
    private final GameMode gameMode;
    private final Presenter presenter;
    private final JMenuBar menuBar;

    public MainMenu(GameMode gameMode, Presenter presenter) {
        this.gameMode = gameMode;
        this.presenter = presenter;

        menuBar = new JMenuBar();

        menuBar.add(initGameMenu());
        menuBar.add(initHelpMenu());
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private JMenu initGameMenu() {
        JMenu game = new JMenu("Game");

        JMenuItem newGame = new JMenuItem("New Game");

        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        newGame.addActionListener(e -> presenter.restartGame());

        game.add(newGame);
        game.add(new JSeparator());

        ButtonGroup gameModesButtonGroup = new ButtonGroup();

        for (GameMode gameMode : GameMode.values()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(gameMode.getName());

            if (gameMode.equals(this.gameMode)) {
                menuItem.setSelected(true);
            }

            menuItem.addActionListener(e -> presenter.startNewGame(gameMode));

            gameModesButtonGroup.add(menuItem);
            game.add(menuItem);
        }

        JMenuItem records = new JMenuItem("Records");
        records.addActionListener(ShowRecordsListener.get(presenter));

        game.add(new JSeparator());
        game.add(records);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> presenter.saveRecordsAndExit());

        game.add(new JSeparator());
        game.add(exit);

        return game;
    }

    private JMenu initHelpMenu() {
        JMenu help = new JMenu("Help");

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(AboutListener.get());

        help.add(about);

        return help;
    }
}
