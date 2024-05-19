package com.z.game.start.msg;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageContent<T extends Message> {

    private int cmd;

    private int version;

    private int flag;

    private String uid;

    private T data;

    public MessageContent(int cmd, String uid, T data) {
        this.cmd = cmd;
        this.uid = uid;
        this.data = data;
    }

}
