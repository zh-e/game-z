package com.z.game.start.netty;

import com.z.game.start.config.ConfigManager;
import com.z.game.start.config.ServerConfig;
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

    @Override
    public void run() {

        ServerConfig serverConfig = ConfigManager.getServerConfig();

        EventLoopGroup bossGroup = new NioEventLoopGroup(serverConfig.getNettyBossGroupSize());
        EventLoopGroup workerGroup = new NioEventLoopGroup(serverConfig.getNettyWorkerGroupSize());

        HandlerManager handlerManager = new DefaultWsHandlerManager();
        HandlerInitializer initializer = new HandlerInitializer(handlerManager);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 10240).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).childHandler(initializer);

            Channel ch = b.bind(serverConfig.getNettyPort()).sync().channel();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> stopServer(bossGroup, workerGroup)));

            LOGGER.info("Ws server started on port:{}", serverConfig.getNettyPort());

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
