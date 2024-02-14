package com.z.game.start.core.interfaces;

public interface Actuator {

    /**
     * 开始执行
     */
    public void start();

    /**
     * 结束执行
     */
    public void stop();

    /**
     * 执行一次
     */
    public void runOnce();

}
