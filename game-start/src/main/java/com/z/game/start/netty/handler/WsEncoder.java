package com.z.game.start.netty.handler;

import com.z.game.start.msg.IMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class WsEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof IMessage)) {
            super.write(ctx, msg, promise);
            return;
        }

        IMessage iMessage = (IMessage) msg;

        ByteBuf content = ctx.alloc().buffer();

        ctx.write(new BinaryWebSocketFrame(content), promise);
    }
}
