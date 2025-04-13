package com.z.game.start.netty.handler;

import com.z.game.start.constant.Constants;
import com.z.game.start.core.ConnService;
import com.z.game.start.core.Node;
import com.z.game.start.core.Port;
import com.z.game.start.msg.Message;
import com.z.game.start.msg.MessageContent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class WsServerHandler extends SimpleChannelInboundHandler<MessageContent<? extends Message>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsServerHandler.class);

    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    private ConnService conn;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
        ctx.writeAndFlush(content);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOGGER.info("用户建立链接");

        ONLINE_COUNT.incrementAndGet();

        Port port = Node.getInstance().getPort("");


        //初始化连接 判断断线重连在登录回调中做
        conn = new ConnService(port);

        //启动连接
//        conn.startup();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        LOGGER.info("用户断开连接");

        ONLINE_COUNT.decrementAndGet();

    }
}
