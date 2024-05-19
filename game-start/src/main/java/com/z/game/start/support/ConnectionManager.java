package com.z.game.start.support;

import com.z.game.start.core.ConnService;
import com.z.game.start.msg.MessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private static final ConcurrentHashMap<String, ConnService> connectionMap = new ConcurrentHashMap<>();

    public static void register(String uid, ConnService connService) {
        connectionMap.put(uid, connService);
    }

    public static void unregister(String uid) {
        connectionMap.remove(uid);
    }

    public static void sendMsgToUid(String uid, MessageContent<?> content) {
        ConnService connService = connectionMap.get(uid);
        if (connService != null) {
            connService.sendMsg(content);
        }
    }

}
