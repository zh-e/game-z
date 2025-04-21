package com.z.game.start.netty.handler;

import com.z.game.start.constant.SysMsgId;
import com.z.game.start.msg.IMessage;
import com.z.game.start.msg.PingMsg;
import com.z.game.start.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.Arrays;

public class WsDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf in = frame.content();

        int len = in.readInt();
        int version = in.readByte();
        int cmd = in.readInt();
        int flag = in.readByte();
        byte[] body = new byte[len - 6];
        in.readBytes(body);
        in.release();

        if (cmd == SysMsgId.HEART_BEAT) {
            PingMsg pingMsg = JsonUtils.fromJson(body, PingMsg.class);

            ctx.fireChannelRead(pingMsg);
        }


    }
}
