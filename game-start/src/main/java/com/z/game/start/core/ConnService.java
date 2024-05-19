package com.z.game.start.core;

import com.z.game.start.msg.MessageContent;
import io.netty.channel.Channel;
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

    private final Channel channel;

    private final ConcurrentLinkedQueue<MessageContent<?>> datas = new ConcurrentLinkedQueue<>();

    public ConnService(String id, Port port, Channel channel) {
        super(id, port);
        this.channel = channel;
    }

    public void putData(MessageContent<?> message) {
        datas.add(message);
    }

    public void delayClose() {

    }

    @Override
    public void pulse() {
        while (!datas.isEmpty()) {
            MessageContent<?> message = datas.poll();
            this.sendMsg(message);
        }
    }

    public void sendMsg(MessageContent<?> content) {
        channel.writeAndFlush(content);
    }

}
