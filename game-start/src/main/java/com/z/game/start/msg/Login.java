package com.z.game.start.msg;

import com.z.game.start.constant.SysMsgId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MessageCmd(cmd = SysMsgId.LOGIN)
public class Login implements Message{

    private String uid;

    private String ticket;

}
