package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;
import lombok.Getter;

public class Service implements Actuator {

    @Getter
    private final String id;

    protected final Port port;

    public Service(String id, Port port) {
        this.id = id;
        this.port = port;
    }

    public void startUp() {
        port.addService(this);
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

    public void pulse() {

    }

}
