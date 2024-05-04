package com.z.game.start.netty.handler;

import com.z.game.start.constant.SysMsgId;
import com.z.game.start.msg.BaseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class WsServerHandler extends SimpleChannelInboundHandler<BaseMessage> {

    private static final Logger LOGGER = LogManager.getLogger(WsServerHandler.class);

    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        if (msg.getCmd() == SysMsgId.HEART_BEAT) {
            ctx.writeAndFlush(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOGGER.info("用户建立链接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        LOGGER.info("用户断开连接");

    }
}
