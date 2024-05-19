package com.z.game.start.core;

import com.z.game.start.constant.SysMsgId;
import com.z.game.start.msg.Fail;
import com.z.game.start.msg.Login;
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
            handlerMsg(message);
        }
    }

    public void sendMsg(MessageContent<?> content) {
        channel.writeAndFlush(content);
    }

    public void handlerMsg(MessageContent<?> content) {
        //心跳消息直接返回 不转交给conn处理
        if (SysMsgId.HEART_BEAT == content.getCmd()) {
            this.sendMsg(content);
            return;
        }

        if (SysMsgId.LOGIN == content.getCmd()) {
            Login login = (Login) content.getData();
            String uid = login.getUid();
            String ticket = login.getTicket();

            if (uid.equals(ticket)) {
                MessageContent<Login> resp = new MessageContent<>();
                resp.setCmd(SysMsgId.LOGIN);
                resp.setUid(uid);
                resp.setData(login);

                this.sendMsg(resp);
            } else {
                Fail fail = new Fail();
                fail.setTip("登陆校验失败");

                MessageContent<Fail> resp = new MessageContent<>();
                resp.setCmd(SysMsgId.LOGIN);
                resp.setUid(uid);
                resp.setData(fail);
                this.sendMsg(resp);
            }
            return;
        }

        //TODO 状态


    }

}
