package com.z.game.start.msg;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageCmd {

    int cmd() default -1;

}
