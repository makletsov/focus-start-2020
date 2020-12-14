package ru.makletsov.minesweeper.model;

public class Record {
    private GameMode gameMode;
    private String owner;
    private long duration;

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
