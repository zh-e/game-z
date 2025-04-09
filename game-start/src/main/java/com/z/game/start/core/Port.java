package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;
import lombok.Getter;

public class Port implements Actuator {

    private Node node;

    @Getter
    private final String portId;

    private ThreadHandler threadHandler;

    public Port(String portId) {
        this.portId = portId;
        this.threadHandler = new ThreadHandler(this, portId);
    }

    public void startUp(Node node) {
        this.node = node;
        node.addPort(this);

        this.threadHandler.startUp();
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
