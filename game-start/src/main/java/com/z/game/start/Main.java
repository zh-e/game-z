package com.z.game.start;

import com.z.game.start.netty.WsServer;

public class Main {

    public static void main(String[] args) {
        new WsServer(8888).start();

    }


}
