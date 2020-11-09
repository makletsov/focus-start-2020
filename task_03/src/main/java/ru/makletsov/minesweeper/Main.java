package ru.makletsov.minesweeper;

import ru.makletsov.minesweeper.io.ImagesManager;
import ru.makletsov.minesweeper.io.RecordsManager;
import ru.makletsov.minesweeper.model.GameMode;
import ru.makletsov.minesweeper.presenter.Presenter;
import ru.makletsov.minesweeper.view.Markup;

import javax.swing.*;

public class Main {
    private static final String ERROR_PANEL_TITLE = "Error!";

    public static void main(String[] args) {
        ImagesManager imagesManager = new ImagesManager();
        RecordsManager recordsManager = new RecordsManager();

        try {
            Markup markup = new Markup(
                    imagesManager.getMainImage(),
                    imagesManager.getRestartButtonImages(),
                    imagesManager.getCellStateImages(),
                    imagesManager.getMinesCountImages(),
                    imagesManager.getNumbersImages()
            );

            Presenter presenter = new Presenter(GameMode.BEGINNER, recordsManager.getRecords(), markup);
            presenter.addRecordsConsumer(recordsManager);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), ERROR_PANEL_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}
