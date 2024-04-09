package com.z.game.start.support;

public class TickTimer {

    private long interval = -1;

    private long nextTime = -1;

    public TickTimer(int interval) {
        start(interval);
    }

    public void start(int interval) {
        this.interval = interval;
        this.nextTime = System.currentTimeMillis() + interval;
    }

    public void stop() {
        this.interval = -1;
        this.nextTime = -1;
    }

    public boolean isOnce(long currentTime) {
        if (interval == -1) {
            return false;
        }

        return nextTime <= currentTime;
    }

    public boolean isPeriod(long currentTime) {
        if (isOnce(currentTime)) {
            this.nextTime = currentTime + interval;
            return true;
        }
        return false;
    }

    public boolean isStart() {
        return interval == -1;
    }

}
