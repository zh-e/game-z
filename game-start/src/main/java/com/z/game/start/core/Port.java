package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Port implements Actuator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Port.class);

    private Node node;

    @Getter
    private final String portId;

    private final ThreadHandler threadHandler;

    private final ConcurrentHashMap<String, Service> services = new ConcurrentHashMap<>();

    //接收到待处理的请求
    private final ConcurrentLinkedQueue<Call> calls = new ConcurrentLinkedQueue<>();

    //接收到的请求返回值
    private final ConcurrentLinkedQueue<Call> callResults = new ConcurrentLinkedQueue<>();

    public Port(String portId) {
        this.portId = portId;
        this.threadHandler = new ThreadHandler(this, portId);
    }

    public void startUp(Node node) {
        this.node = node;
        if (this instanceof ConnPort) {
            node.addConnPort((ConnPort) this);
        } else {
            node.addPort(this);
        }

        this.threadHandler.startUp();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        for (Service s : services.values()) {
            s.stop();
        }
    }

    @Override
    public void runOnce() {
        pulseServices();
    }

    public void addService(Service service) {
        services.put(service.getId(), service);
    }

    protected void pulseServices() {
        for (Service service : services.values()) {
            try {
                service.pulse();
            } catch (Exception e) {
                LOGGER.error("发现异常: port={}, e:", portId, e);
            }
        }
    }

    public void addCall(Call call) {
        calls.add(call);
    }

    public void addCallResult(Call call) {
        callResults.add(call);
    }

}
