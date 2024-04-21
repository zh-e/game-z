package com.z.game.start.netty.handler;

import com.z.game.start.constant.SysMsgId;
import com.z.game.start.msg.IMessage;
import com.z.game.start.util.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class WsServerHandler extends SimpleChannelInboundHandler<IMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) throws Exception {
        if (msg.getCmd() == SysMsgId.HEART_BEAT) {
            ctx.writeAndFlush(msg);
        }
    }
}
