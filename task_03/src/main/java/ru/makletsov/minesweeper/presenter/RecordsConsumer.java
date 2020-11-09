package ru.makletsov.minesweeper.presenter;

import ru.makletsov.minesweeper.model.Records;

public interface RecordsConsumer {
    void consumeRecords(Records records);
}
