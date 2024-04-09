package com.z.game.start.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 链接port
 * 仅处理链接事件
 */
public class ConnPort extends Port {

    private static final ConcurrentHashMap<String, ConnService> connServiceMap = new ConcurrentHashMap<>();

    public ConnPort(String portId) {
        super(portId);

    }

}
