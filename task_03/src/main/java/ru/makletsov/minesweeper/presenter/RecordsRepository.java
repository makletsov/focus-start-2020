package ru.makletsov.minesweeper.presenter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.makletsov.minesweeper.model.Record;

public class RecordsRepository implements RecordsConsumer {
    private static final String DATA_FILE_PATH = "data/records.dat";
    private static final String EXCEPTION_TEXT = "RecordsManager. Cannot write records to file: ";

    private final Path path;
    private final ObjectMapper mapper;

    public RecordsRepository() {
        path = Paths.get(DATA_FILE_PATH).toAbsolutePath().normalize();
        mapper = new ObjectMapper();
    }

    @Override
    public void saveRecords(Collection<Record> records) {
        try {
            Files.write(path, stringifyRecords(records));
        } catch (IOException e) {
            throw new RuntimeException(EXCEPTION_TEXT + e.getMessage());
        }
    }

    public Collection<Record> loadRecords() {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .map(this::getRecordFromString)
                    .collect(Collectors.toList());
        } catch (IOException | IllegalArgumentException e) {
            return List.of();
        }
    }

    private Collection<String> stringifyRecords(Collection<Record> records) {
        return records
                .stream()
                .map(this::getRecordAsString)
                .collect(Collectors.toList());
    }

    private Record getRecordFromString(String string) {
        try {
            return mapper.readValue(string, Record.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String getRecordAsString(Record record) {
        try {
            return mapper.writeValueAsString(record);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}


