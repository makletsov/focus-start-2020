package ru.makletsov.minesweeper.model;

import ru.makletsov.minesweeper.GameMode;

public class Record {
    private GameMode gameMode;
    private String owner;
    private long duration;

    //need for json serialization
    private Record() {}

    public Record(GameMode gameMode, String owner, long duration) {
        this.gameMode = gameMode;
        this.owner = owner;
        this.duration = duration;
    }

    public String getOwner() {
        return owner;
    }

    public long getDuration() {
        return duration;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
