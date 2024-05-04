package com.z.game.start.netty.handler;

import com.z.game.start.msg.BaseMessage;
import com.z.game.start.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class WsEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof BaseMessage)) {
            super.write(ctx, msg, promise);
            return;
        }

        BaseMessage message = (BaseMessage) msg;

        byte[] bytes = JsonUtils.toJsonBytes(message);
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
