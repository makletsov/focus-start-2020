package ru.makletsov.minesweeper.presenter;

import ru.makletsov.minesweeper.model.Record;

import java.util.Collection;

public interface RecordsConsumer {
    void saveRecords(Collection<Record> records);
}
