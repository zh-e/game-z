package com.z.game.start.msg;

import com.z.game.start.constant.SysMsgId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PingMsg implements BaseMessage {

    private int sn;

    @Override
    public int getCmd() {
        return SysMsgId.HEART_BEAT;
    }
}
