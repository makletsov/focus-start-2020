package ru.makletsov.minesweeper.view;

import ru.makletsov.minesweeper.model.GameMode;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class MainMenu {
    private final GameMode currentMode;
    private final JMenuBar menuBar;

    private JMenuItem newGame;

    private Map<GameMode, JRadioButtonMenuItem> gameModes;

    private JMenuItem records;
    private JMenuItem exit;

    private JMenuItem about;

    public MainMenu(GameMode gameMode) {
        currentMode = gameMode;

        menuBar = new JMenuBar();

        menuBar.add(initGameMenu());
        menuBar.add(initHelpMenu());
    }

    public void addNewGameListener(ActionListener newGameListener) {
        addListener(newGame, newGameListener);
    }

    public void addGameModeListener(GameMode gameMode, ActionListener gameModeListener) {
        addListener(gameModes.get(gameMode), gameModeListener);
    }

    public void addRecordsTableListener(ActionListener actionListener) {
        addListener(records, actionListener);
    }

    public void addExitListener(ActionListener actionListener) {
        addListener(exit, actionListener);
    }

    public void addAboutListener(ActionListener actionListener) {
        addListener(about, actionListener);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private JMenu initGameMenu() {
        JMenu game = new JMenu("Game");

        newGame = new JMenuItem("New Game");

        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));

        game.add(newGame);
        game.add(new JSeparator());

        ButtonGroup gameModesButtonGroup = new ButtonGroup();
        gameModes = new HashMap<>();

        for (GameMode gameMode : GameMode.values()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(gameMode.getPrettyString());

            if (gameMode.equals(currentMode)) {
                menuItem.setSelected(true);
            }

            gameModes.put(gameMode, menuItem);
            gameModesButtonGroup.add(menuItem);
            game.add(menuItem);
        }

        records = new JMenuItem("Records");

        game.add(new JSeparator());
        game.add(records);

        exit = new JMenuItem("Exit");

        game.add(new JSeparator());
        game.add(exit);

        return game;
    }

    private JMenu initHelpMenu() {
        JMenu help = new JMenu("Help");

        about = new JMenuItem("About");

        help.add(about);

        return help;
    }

    private void addListener(JMenuItem menuItem, ActionListener listener) {
        menuItem.addActionListener(listener);
    }
}
