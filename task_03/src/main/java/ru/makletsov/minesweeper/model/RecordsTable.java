package ru.makletsov.minesweeper.model;

import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RecordsTable {
    private static final String DEFAULT_OWNER_NAME = "Anonymous";

    private final Map<GameMode, Record> table;

    public RecordsTable(Collection<Record> records) {
        if (isExhaustive(records)) {
            this.table = generateFormList(records);
        } else {
            this.table = generateDefaultTable();
        }
    }

    private boolean isExhaustive(Collection<Record> records) {
        return records.size() == GameMode.values().length;
    }

    private Map<GameMode, Record> generateFormList(Collection<Record> records) {
        return records
            .stream()
            .collect(Collectors.toMap(
                Record::getGameMode,
                Function.identity()
            ));
    }

    private Map<GameMode, Record> generateDefaultTable() {
        return EnumSet
            .allOf(GameMode.class)
            .stream()
            .collect(Collectors.toMap(
                Function.identity(),
                m -> new Record(m, DEFAULT_OWNER_NAME, 999)
            ));
    }

    public Collection<Record> getRecords() {
        return table.values();
    }

    public boolean checkForRecord(GameMode gameMode, Duration duration) {
        return duration.toSeconds() < table.get(gameMode).getDuration();
    }

    public void setRecord(GameMode gameMode, @Nullable String recordOwnerName, Duration gameDuration) {
        String owner = Objects.requireNonNullElse(recordOwnerName, DEFAULT_OWNER_NAME);
        long durationInSeconds = gameDuration.toSeconds();
        Record newRecord = new Record(gameMode, owner, durationInSeconds);

        table.put(gameMode, newRecord);
    }
}
