package ru.makletsov.minesweeper.model;

import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

public class Records {
    private static final String DEFAULT_OWNER_NAME = "Anonymous";

    private final Map<GameMode, Record> records;

    public Records(Map<GameMode, Record> records) {
        this.records = records;
    }

    public Record getRecord(GameMode gameMode) {
        return records.get(gameMode);
    }

    public boolean checkForRecord(GameMode gameMode, Duration duration) {
        return duration.toSeconds() < records.get(gameMode).getDurationInSeconds();
    }

    public void setRecord(GameMode gameMode, @Nullable String recordOwnerName, Duration gameDuration) {
        String owner = Objects.requireNonNullElse(recordOwnerName, DEFAULT_OWNER_NAME);
        long durationInSeconds = gameDuration.toSeconds();
        Record newRecord = new Record(gameMode, owner, durationInSeconds);

        records.put(gameMode, newRecord);
    }
}
