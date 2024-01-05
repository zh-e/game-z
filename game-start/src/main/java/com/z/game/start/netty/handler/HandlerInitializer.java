package com.z.game.start.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class HandlerInitializer extends ChannelInitializer<SocketChannel> {

    private final HandlerManager handlerManager;

    public HandlerInitializer(HandlerManager handlerManager) {
        this.handlerManager = handlerManager;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        handlerManager.init(pipeline);
    }
}
