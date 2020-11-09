package ru.makletsov.minesweeper.io;

import ru.makletsov.minesweeper.model.GameMode;
import ru.makletsov.minesweeper.model.Record;
import ru.makletsov.minesweeper.model.Records;
import ru.makletsov.minesweeper.presenter.RecordsConsumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecordsManager implements RecordsConsumer {
    private static final String DATA_FILE_PATH = "data/records.dat";
    private static final String DEFAULT_OWNER = "None";
    private static final long DEFAULT_DURATION = 999;

    private static final String EXCEPTION_TEXT = "Cannot write records to file.";
    private static final String DATA_FILE_DELIMITER = " ";

    private final Path path;
    private Records records;

    public RecordsManager() {
        path = Paths.get(DATA_FILE_PATH).toAbsolutePath().normalize();

        try {
            records = new Records(Files.lines(path)
                    .map(line -> line.split(DATA_FILE_DELIMITER))
                    .collect(Collectors.toMap(
                            entry -> GameMode.valueOf(entry[0]),
                            entry -> new Record(
                                    GameMode.valueOf(entry[0]),
                                    entry[1],
                                    Long.parseLong(entry[2])
                            ))));
        } catch (IOException e) {
            records = getDefaultRecords();
        }
    }

    @Override
    public void consumeRecords(Records records) {
        this.records = records;

        try {
            Files.write(path, stringifyRecords(records));
        } catch (IOException e) {
            throw new RuntimeException(EXCEPTION_TEXT);
        }
    }

    public Records getRecords() {
        return records;
    }

    private List<String> stringifyRecords(Records records) {
        return EnumSet.allOf(GameMode.class)
                .stream()
                .map(records::getRecord)
                .map(Record::stringify)
                .collect(Collectors.toList());
    }

    private Records getDefaultRecords() {
        Map<GameMode, Record> recordsMap = EnumSet.allOf(GameMode.class)
                .stream()
                .collect(Collectors.toMap(m -> m, m -> new Record(m, DEFAULT_OWNER, DEFAULT_DURATION)));

        return new Records(recordsMap);
    }
}
