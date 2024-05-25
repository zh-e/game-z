package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Node implements Actuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

    /**
     * 编号
     */
    @Getter
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

        for (Port port : connPorts) {
            port.stop();
        }

    }

    @Override
    public void runOnce() {

    }

    public void sendCall(Call call) {
        String toNodeId = call.getTo().getNodeId();
        if (nodeId.equals(toNodeId)) {
            callHandle(call);
        } else {
            // TODO 不同NODE 待处理
            LOGGER.error("暂不支持跨NODE消息处理。call:{}", call);
        }

    }

    private void callHandle(Call call) {
        Port port = ports.get(call.getTo().getPortId());
        if (port == null) {
            LOGGER.info("callHandle unknown port {}", call.getTo().getPortId());
            return;
        }
        switch (call.getType()) {
            case Call.TYPE_RPC:
            case Call.TYPE_MIX:
                port.addCall(call);
                break;
            case Call.TYPE_RPC_RETURN:
                port.addCallResult(call);
                break;
            case Call.TYPE_PING:
                //TODO remote ping
                break;
            default:
                LOGGER.error("callHandle unknown type: {}", call.getType());
        }
    }

}
