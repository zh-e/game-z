package com.z.game.start.core;

import com.z.game.start.msg.MessageContent;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 链接时间处理
 */
public class ConnService extends Service {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private boolean online = true;

    private final ConcurrentLinkedQueue<MessageContent<?>> datas = new ConcurrentLinkedQueue<>();

    public ConnService(Port port) {
        super(port);
    }

    public void putData(MessageContent<?> message) {
        datas.add(message);
    }

    public void delayClose() {

    }


}
