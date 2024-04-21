package com.z.game.start.netty.handler;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class DefaultWsHandlerManager extends HandlerManager{



    @Override
    public void addProtocolHandlers(ChannelPipeline pipeline) {
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", true));
        pipeline.addLast(new IdleStateHandler(1, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(new WsEncoder());
        pipeline.addLast(new WsDecoder());
    }

    @Override
    public void addMetricsHandlers(ChannelPipeline pipeline) {
        //TODO 监控
    }

    @Override
    public void addBizHandlers(ChannelPipeline pipeline) {
        pipeline.addLast(new WsServerHandler());
    }

}
