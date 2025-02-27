package com.z.game.start.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ConfigManager {

    @Getter
    private static ServerConfig serverConfig;

    public static void init() {
        load("server.properties");
    }

    private synchronized static void load(String propertiesFile) {
        Properties properties = new Properties();

        try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (in != null) {
                properties.load(in);
            }

            int groupSize = getIntProperty(properties, "server.netty.boss.group.size", 1);
            int workerSize = getIntProperty(properties, "server.netty.boss.worker.size", 16);
            int port = getIntProperty(properties, "server.netty.port", 8080);
            int connPortCount = getIntProperty(properties, "server.port.conn.count", 8);

            serverConfig = new ServerConfig(port, groupSize, workerSize, connPortCount);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getIntProperty(Properties properties, String key, int defaultValue) {
        Object property = properties.getProperty(key);
        if (Objects.isNull(property)) {
            return defaultValue;
        }
        return Integer.parseInt(property.toString());
    }

}
