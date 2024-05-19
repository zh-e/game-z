package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;

import java.util.Timer;
import java.util.TimerTask;

public class ThreadHandler extends Thread {

    /**
     * 被调用的对象
     */
    private final Actuator actuator;

    /**
     * 是否在运行
     */
    private volatile boolean running;

    /**
     * 心跳间隔
     */
    private final int interval = 20;

    public ThreadHandler(Actuator actuator, String name) {
        super(name);
        this.actuator = actuator;
    }

    public void startUp() {
        if (running) {
            return;
        }
        running = true;

        start();
    }

    public void cleanUp() {
        if (!running) {
            return;
        }
        running = false;
    }

    @Override
    public void run() {
        actuator.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (running) {
                    actuator.runOnce();
                } else {
                    timer.cancel();
                    actuator.stop();
                }

            }
        }, 0L, 1L);
    }

}
