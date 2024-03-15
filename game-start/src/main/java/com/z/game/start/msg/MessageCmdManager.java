package com.z.game.start.msg;

import java.util.concurrent.ConcurrentHashMap;

public class MessageCmdManager {

    private static final ConcurrentHashMap<Integer, Class<? extends Message>> CMD_MESSAGE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Class<? extends Message>, Integer> MESSAGE_CMD = new ConcurrentHashMap<>();

    public static void registerCmd(int cmd, Class<? extends Message> clazz) {
        CMD_MESSAGE.put(cmd, clazz);
        MESSAGE_CMD.put(clazz, cmd);
    }

    public static Class<? extends Message> getMessageType(int cmd) {
        return CMD_MESSAGE.get(cmd);
    }

    public static int getCmdByMessage(Class<? extends Message> clazz) {
        Integer cmd = MESSAGE_CMD.get(clazz);
        return cmd == null ? -1 : cmd;
    }

}
