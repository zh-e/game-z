package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;

import java.util.concurrent.ConcurrentHashMap;

public class Node implements Actuator {

    /**
     * 编号
     */
    private final String nodeId;

    /**
     * 地址
     */
    private final String nodeAddr;

    /**
     * 是否正在运行
     */
    private volatile boolean running;

    /**
     * 下游所有port
     */
    private final ConcurrentHashMap<String, Port> ports = new ConcurrentHashMap<>();

    private final ThreadHandler threadHandler;

    public Node(String nodeId, String nodeAddr) {
        this.nodeId = nodeId;
        this.nodeAddr = nodeAddr;
        this.running = true;
        this.threadHandler = new ThreadHandler(this, "node");
    }

    public void startUp() {

    }

    public boolean addPort(Port port) {
        return ports.put(port.getPortId(), port) != null;
    }

    public Port getPort(String portId) {
        return ports.get(portId);
    }

    public boolean delPort(String portId) {
        return ports.remove(portId) != null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }

        for (Port port : ports.values()) {
            port.stop();
        }

    }

    @Override
    public void runOnce() {

    }
}
