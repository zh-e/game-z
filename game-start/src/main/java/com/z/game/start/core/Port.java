package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;
import lombok.Getter;

public class Port implements Actuator {

    @Getter
    private final String portId;

    public Port(String portId) {
        this.portId = portId;
    }

    public void startUp(Node node) {

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
