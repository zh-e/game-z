package com.z.game.start.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Call {

    /**
     * 远程调用
     */
    public static final int TYPE_RPC = 1000;

    /**
     * 远程调用返回
     */
    public static final int TYPE_RPC_RETURN = 2000;

    /**
     * 整合专用
     */
    public static final int TYPE_MIX = 3000;

    /**
     * 心跳检测
     */
    public static final int TYPE_PING = 4000;

    private long id;

    private int type;

    private CallPoint from = new CallPoint();

    private CallPoint to = new CallPoint();

    public String methodKey;

    private Object[] methodParam;

    public Param returns = new Param();

    public Param param = new Param();

}
