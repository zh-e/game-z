package com.z.game.start.msg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageContent<T extends Message> {

    private int cmd;

    private int version;

    private int flag;

    private T data;

}
