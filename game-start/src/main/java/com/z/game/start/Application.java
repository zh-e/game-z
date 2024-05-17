package com.z.game.start;

import com.z.game.start.config.ConfigManager;
import com.z.game.start.constant.Constants;
import com.z.game.start.core.ConnPort;
import com.z.game.start.core.Node;
import com.z.game.start.core.PackageScan;
import com.z.game.start.core.Port;
import com.z.game.start.netty.WsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws InterruptedException {
        String nodeId = System.getProperty("nodeId", Constants.DEFAULT_WORD_NAME);

        //初始化配置
        ConfigManager.init();
        //扫包 维护注解类
        PackageScan.init();

        //初始化当前节点
        Node node = new Node(nodeId, "");
        //初始化处理链接的port
        initConnPort(node);

        //netty长链接建立
        WsServer wsServer = new WsServer();
        wsServer.start();
        wsServer.join();

        //最终节点启动
        node.startUp();

        LOGGER.info("application start up success");
    }

    private static void initConnPort(Node node) {
        int connPortCount = ConfigManager.getServerConfig().getConnPortCount();
        for (int i = 0; i < connPortCount; i++) {
            Port port = new ConnPort(Constants.CONN_PORT_NAME_PRE + i);
            port.startUp(node);
        }


    }


}
