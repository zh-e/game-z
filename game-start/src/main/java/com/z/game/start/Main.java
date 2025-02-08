package com.z.game.start;

import com.z.game.start.config.ConfigManager;
import com.z.game.start.config.ServerConfig;
import com.z.game.start.netty.WsServer;
import com.z.game.start.util.JsonUtils;

public class Main {

    public static void main(String[] args) {
        //初始化配置
        ConfigManager.init();

        ServerConfig serverConfig = ConfigManager.getServerConfig();


        System.out.println(JsonUtils.toJson(serverConfig));

        new WsServer(serverConfig.getNettyBossGroupSize(), serverConfig.getNettyWorkerGroupSize(), serverConfig.getNettyPort()).start();

    }


}
