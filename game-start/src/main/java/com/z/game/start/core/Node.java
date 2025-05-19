package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Node implements Actuator {

    /**
     * 编号
     */
    private String nodeId;

    /**
     * 地址
     */
    private String nodeAddr;

    private static volatile Node INSTANCE;

    /**
     * 是否正在运行
     */
    private volatile boolean running;

    private List<ConnPort> connPorts = new CopyOnWriteArrayList<>();

    /**
     * 下游所有port
     */
    private ConcurrentHashMap<String, Port> ports = new ConcurrentHashMap<>();

    private ThreadHandler threadHandler;

    public static Node getInstance() {
        if (INSTANCE == null) {
            synchronized (Node.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Node();
                }
            }
        }
        return INSTANCE;
    }

    public void init(String nodeId, String nodeAddr) {
        this.nodeId = nodeId;
        this.nodeAddr = nodeAddr;
        this.running = true;
        this.threadHandler = new ThreadHandler(this, "node");
    }

    public void startUp() {
        threadHandler.startUp();
    }

    public boolean addPort(Port port) {
        return ports.put(port.getPortId(), port) != null;
    }

    public Port getPort(String portId) {
        return ports.get(portId);
    }

    public void addConnPort(ConnPort connPort) {
        connPorts.add(connPort);
    }

    public ConnPort getRandomConnPort() {
        return connPorts.get(ThreadLocalRandom.current().nextInt(connPorts.size()));
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
