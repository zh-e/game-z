package com.z.game.start.msg;

import com.z.game.start.constant.SysMsgId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MessageCmd(cmd = SysMsgId.HEART_BEAT)
public class Ping implements Message {

    private int sn;

}
