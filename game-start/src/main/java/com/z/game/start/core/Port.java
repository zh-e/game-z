package com.z.game.start.core;

import com.z.game.start.core.interfaces.Actuator;
import com.z.game.start.util.JsonUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

    //接收到的请求返回值 TODO 结果待处理
    private final ConcurrentLinkedQueue<Call> callResults = new ConcurrentLinkedQueue<>();
    //本次心跳需要处理的请求
    private final List<Call> pulseCalls = new ArrayList<>();

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
        //挑选本次心跳要执行的
        pulseCallToExec();

        //执行本次
        pulseCalls();

        //下游service执行
        pulseServices();
    }

    public void addService(Service service) {
        services.put(service.getId(), service);
    }

    public Service getService(String serviceId) {
        return services.get(serviceId);
    }

    protected void pulseCallToExec() {
        while (!calls.isEmpty()) {
            pulseCalls.add(calls.poll());
        }

        while (!callResults.isEmpty()) {
            LOGGER.info("结果返回 待处理，call:{}", JsonUtils.toJson(callResults.poll()));
        }
    }

    protected void pulseCalls() {
        while (!pulseCalls.isEmpty()) {
            // 因为下面的try中需要与出栈入栈配合 所以这句就不放在try中了
            Call call = pulseCalls.remove(0);

            try {
                // 利用反射执行Call请求
                Service serv = getService(call.getTo().getServiceId());
                if (serv == null) {
                    LOGGER.error("执行Call队列时无法找到处理服务：call={}", call);
                } else {
                    Method method = MethodManager.getMethod(call.getMethodKey());
                    method.invoke(serv, call.getMethodParam());
                    //TODO 支持返回值
                }
            } catch (Exception e) {
                LOGGER.error("执行Call队列时发生错误: call={}m=, e:", call, e);
            }
        }
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

    private void callReturn(long callId, CallPoint to, Param param) {
        Call call = new Call();
        call.setId(callId);
        call.setType(Call.TYPE_RPC_RETURN);
        call.setMethodParam(new Object[0]);
        call.setTo(to);
        call.setFrom(new CallPoint(node.getNodeId(), portId));
        call.setReturns(param);

        sendCall(call);
    }

    private void sendCall(Call call) {
        node.sendCall(call);
    }

}
