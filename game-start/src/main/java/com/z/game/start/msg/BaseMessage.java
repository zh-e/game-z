package com.z.game.start.msg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseMessage {

    private int cmd;

    private int version;

    private int flag;

}
