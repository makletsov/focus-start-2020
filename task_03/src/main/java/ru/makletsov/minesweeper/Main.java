package ru.makletsov.minesweeper;

import ru.makletsov.minesweeper.io.ImagesLoader;
import ru.makletsov.minesweeper.io.RecordsRepository;
import ru.makletsov.minesweeper.model.GameMode;
import ru.makletsov.minesweeper.model.Record;
import ru.makletsov.minesweeper.model.RecordsTable;
import ru.makletsov.minesweeper.presenter.Presenter;
import ru.makletsov.minesweeper.view.Markup;

import javax.swing.*;
import java.util.Collection;

public class Main {
    private static final String ERROR_PANEL_TITLE = "Error!";

    public static void main(String[] args) {
        ImagesLoader imagesLoader = new ImagesLoader();
        RecordsRepository recordsManager = new RecordsRepository();

        try {
            Collection<Record> loadedRecords = recordsManager.loadRecords();
            RecordsTable recordsTable = new RecordsTable(loadedRecords);

            Markup markup = new Markup(
                imagesLoader.loadWindowIconImage(),
                imagesLoader.loadRestartButtonImages(),
                imagesLoader.loadCellStateImages(),
                imagesLoader.loadMinesCountImages(),
                imagesLoader.loadNumbersImages()
            );

            Presenter presenter = new Presenter(GameMode.BEGINNER, recordsTable, markup);
            presenter.addRecordsConsumer(recordsManager);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), ERROR_PANEL_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}
