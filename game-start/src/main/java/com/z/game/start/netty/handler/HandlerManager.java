package com.z.game.start.netty.handler;

import io.netty.channel.ChannelPipeline;

public abstract class HandlerManager {

    public void init(ChannelPipeline pipeline) {
        addProtocolHandlers(pipeline);
        addMetricsHandlers(pipeline);
        addBizHandlers(pipeline);
    }

    public abstract void addProtocolHandlers(ChannelPipeline pipeline);

    public abstract void addMetricsHandlers(ChannelPipeline pipeline);

    public abstract void addBizHandlers(ChannelPipeline pipeline);

}
