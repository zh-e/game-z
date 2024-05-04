package com.z.game.start.netty.handler;

import com.z.game.start.constant.SysMsgId;
import com.z.game.start.msg.Message;
import com.z.game.start.msg.MessageContent;
import com.z.game.start.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WsEncoder extends ChannelOutboundHandlerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(WsEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof MessageContent)) {
            super.write(ctx, msg, promise);
            return;
        }

        MessageContent<?> message = (MessageContent<?>) msg;

        byte[] bytes = JsonUtils.toJsonBytes(message.getData());
        int version = message.getVersion();
        int flag = message.getFlag();
        int cmd = message.getCmd();

        ByteBuf content = ctx.alloc().buffer(bytes.length + 10);
        content.writeInt(bytes.length + 6);
        content.writeByte(version);
        content.writeInt(cmd);
        content.writeByte(flag);
        content.writeBytes(bytes);

        ctx.write(new BinaryWebSocketFrame(content), promise);
    }
}
