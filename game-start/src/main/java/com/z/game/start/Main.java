package com.z.game.start;

import com.z.game.start.config.ConfigManager;
import com.z.game.start.config.ServerConfig;
import com.z.game.start.core.Node;
import com.z.game.start.netty.WsServer;
import com.z.game.start.util.JsonUtils;

public class Main {

    public static void main(String[] args) {
        //初始化配置
        ConfigManager.init();

        String nodeId = System.getProperty("nodeId", "word0");

        Node node = new Node(nodeId, "");


        ServerConfig serverConfig = ConfigManager.getServerConfig();
        new WsServer(serverConfig.getNettyBossGroupSize(),
                serverConfig.getNettyWorkerGroupSize(),
                serverConfig.getNettyPort()).start();

    }


}
