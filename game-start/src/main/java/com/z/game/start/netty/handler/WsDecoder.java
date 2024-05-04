package com.z.game.start.netty.handler;

import com.z.game.start.constant.SysMsgId;
import com.z.game.start.msg.Message;
import com.z.game.start.msg.MessageCmdManager;
import com.z.game.start.msg.MessageContent;
import com.z.game.start.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WsDecoder extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(WsEncoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf in = frame.content();

        int len = in.readInt();
        int version = in.readByte();
        int cmd = in.readInt();
        int flag = in.readByte();
        byte[] body = new byte[len - 6];
        in.readBytes(body);
        in.release();

        Class<? extends Message> clazz = MessageCmdManager.getMessageType(cmd);
        Message message = JsonUtils.fromJson(body, clazz);
        MessageContent<Message> content = new MessageContent<>();
        content.setCmd(cmd);
        content.setVersion(version);
        content.setFlag(flag);
        content.setData(message);
        ctx.fireChannelRead(content);
    }
}
