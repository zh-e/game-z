package com.z.game.start.netty;

import com.z.game.start.netty.handler.DefaultWsHandlerManager;
import com.z.game.start.netty.handler.HandlerInitializer;
import com.z.game.start.netty.handler.HandlerManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WsServer extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(WsServer.class);

    private final int bossGroupSize;

    private final int workerGroupSize;

    private final int port;

    public WsServer(int bossGroupSize, int workerGroupSize, int port) {
        this.bossGroupSize = bossGroupSize;
        this.workerGroupSize = workerGroupSize;
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossGroupSize);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerGroupSize);

        HandlerManager handlerManager = new DefaultWsHandlerManager();
        HandlerInitializer initializer = new HandlerInitializer(handlerManager);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 10240).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).childHandler(initializer);

            Channel ch = b.bind(port).sync().channel();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> stopServer(bossGroup, workerGroup)));

            LOGGER.info("Ws server started on port:{}", port);

        } catch (Exception e) {
            LOGGER.error("Ws server start error", e);
        }
    }

    public void stopServer(EventLoopGroup... eventLoopGroups) {
        for (EventLoopGroup group : eventLoopGroups) {
            group.shutdownGracefully();
        }
    }

}
