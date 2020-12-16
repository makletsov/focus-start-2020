package ru.makletsov.minesweeper;

import ru.makletsov.minesweeper.view.ImageLoader;
import ru.makletsov.minesweeper.presenter.RecordsRepository;
import ru.makletsov.minesweeper.model.Record;
import ru.makletsov.minesweeper.model.RecordsTable;
import ru.makletsov.minesweeper.presenter.PresenterImpl;
import ru.makletsov.minesweeper.view.IconsStorage;

import javax.swing.*;
import java.util.Collection;

public class Main {
    private static final String ERROR_PANEL_TITLE = "Error!";

    public static void main(String[] args) {
        ImageLoader imagesLoader = new ImageLoader();
        RecordsRepository recordsManager = new RecordsRepository();

        try {
            Collection<Record> loadedRecords = recordsManager.loadRecords();
            RecordsTable recordsTable = new RecordsTable(loadedRecords);
            IconsStorage iconsStorage = new IconsStorage(imagesLoader);

            PresenterImpl presenter = new PresenterImpl(GameMode.BEGINNER, recordsTable, iconsStorage);
            presenter.addRecordsConsumer(recordsManager);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), ERROR_PANEL_TITLE, JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
