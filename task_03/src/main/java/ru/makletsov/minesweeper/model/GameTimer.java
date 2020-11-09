package ru.makletsov.minesweeper.model;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private final long delay;
    private final long period;

    private Timer timer;
    private boolean isOn;

    public GameTimer(long delay, long period) {
        this.delay = delay;
        this.period = period;

        timer = new Timer();
    }

    public boolean isOn() {
        return isOn;
    }

    public void turnOn(TimerTask timerTask) {
        timer.schedule(timerTask, delay, period);
        isOn = true;
    }

    public void turnOff() {
        timer.cancel();
        timer = new Timer();
        isOn = false;
    }
}
