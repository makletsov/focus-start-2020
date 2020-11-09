package ru.makletsov.minesweeper.model;

public class Record {
    private final GameMode gameMode;
    private final String owner;
    private final long duration;

    public Record(GameMode gameMode, String owner, long duration) {
        this.gameMode = gameMode;
        this.owner = owner;
        this.duration = duration;
    }

    public String getOwner() {
        return owner;
    }

    public long getDurationInSeconds() {
        return duration;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String stringify() {
        return gameMode + " " + owner + " " + duration;
    }
}
