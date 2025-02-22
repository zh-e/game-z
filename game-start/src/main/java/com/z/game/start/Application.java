package com.z.game.start;

import com.z.game.start.config.ConfigManager;
import com.z.game.start.config.ServerConfig;
import com.z.game.start.core.Node;
import com.z.game.start.netty.WsServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Application {

    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        String nodeId = System.getProperty("nodeId", "word0");
        LOGGER.info("application start node={}", nodeId);

        //初始化配置
        ConfigManager.init();
        Node node = new Node(nodeId, "");


        ServerConfig serverConfig = ConfigManager.getServerConfig();
        new WsServer(serverConfig.getNettyBossGroupSize(),
                serverConfig.getNettyWorkerGroupSize(),
                serverConfig.getNettyPort()).start();

    }


}
