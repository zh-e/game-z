package com.z.game.start.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerConfig {

    private int nettyPort = 8888;

    private int nettyBossGroupSize = 1;

    private int nettyWorkerGroupSize = 16;

    private int connPortCount = 8;

}
