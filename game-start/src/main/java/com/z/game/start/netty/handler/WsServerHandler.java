package com.z.game.start.netty.handler;

import com.z.game.start.constant.Constants;
import com.z.game.start.constant.SysMsgId;
import com.z.game.start.core.ConnService;
import com.z.game.start.core.Node;
import com.z.game.start.core.Port;
import com.z.game.start.msg.Message;
import com.z.game.start.msg.MessageContent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class WsServerHandler extends SimpleChannelInboundHandler<MessageContent<? extends Message>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsServerHandler.class);

    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    private static final AtomicInteger ID_AUTO = new AtomicInteger(0);

    private ConnService conn;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageContent content) throws Exception {
        //消息放入队列
        conn.putData(content);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOGGER.info("用户建立链接");

        ONLINE_COUNT.incrementAndGet();

        Port port = Node.getInstance().getRandomConnPort();


        //初始化连接 判断断线重连在登录回调中做
        conn = new ConnService(Constants.CONN_SERVICE_NAME_PRE + ID_AUTO.incrementAndGet(), port, ctx.channel());

        //启动连接
        conn.startUp();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        LOGGER.info("用户断开连接");

        ONLINE_COUNT.decrementAndGet();
        //设置为离线
        conn.setOnline(false);
        //延时处理
        conn.delayClose();

    }
}
