package ru.makletsov.minesweeper.view.listeners;

import javax.swing.*;
import java.awt.event.ActionListener;

public class AboutListener {
    private static final String ABOUT_PANEL_TITLE = "About";
    private static final String ABOUT_PANEL_MASSAGE =
        "Made by Makletsov Vasily." + System.lineSeparator() +
            "Novosibirsk, 2020.";

    public static ActionListener get() {
        return e -> JOptionPane.showMessageDialog(
            null,
            ABOUT_PANEL_MASSAGE,
            ABOUT_PANEL_TITLE,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
