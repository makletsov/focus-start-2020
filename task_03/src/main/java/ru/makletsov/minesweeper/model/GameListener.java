package ru.makletsov.minesweeper.model;

import java.time.Duration;
import java.time.Instant;

public interface GameListener {
    void gameStarted(Instant startTime);

    void gameLost(EndGameEvent endGameEvent);

    void gameWon(Duration gameDuration);
}
