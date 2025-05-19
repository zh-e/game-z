package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;

public class Service implements Actuator {

    protected final Port port;

    public Service(Port port) {
        this.port = port;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void runOnce() {

    }
}
