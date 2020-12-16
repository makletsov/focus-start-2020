package ru.makletsov.minesweeper.presenter;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private final long delay;
    private final long period;

    private Timer timer;

    public GameTimer(long delay, long period) {
        this.delay = delay;
        this.period = period;

        timer = new Timer();
    }

    public void turnOn(TimerTask timerTask) {
        timer.schedule(timerTask, delay, period);
    }

    public void turnOff() {
        timer.cancel();
        timer = new Timer();
    }
}
