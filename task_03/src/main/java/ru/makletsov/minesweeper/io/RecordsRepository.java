package ru.makletsov.minesweeper.io;

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
import ru.makletsov.minesweeper.presenter.RecordsConsumer;


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

//    public static void main(String[] args) throws IOException {
//
//        RecordsManager manager = new RecordsManager();
//
//        Map<GameMode, Record> recs = Map.of(
//                GameMode.BEGINNER, new Record(GameMode.BEGINNER, "vasily", 13),
//                GameMode.AMATEUR, new Record(GameMode.AMATEUR, "vasily", 132),
//                GameMode.PROFESSIONAL, new Record(GameMode.PROFESSIONAL, "vasily", 1323));
//
//        RecordsTable table = new RecordsTable(recs);
//
//        List<String> json = manager.stringifyRecords(table);
//
//        System.out.println(json);
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        for (String str: json) {
//            var r = mapper.readValue(str, Record.class);
//            System.out.println(r);
//        }
//    }
}


