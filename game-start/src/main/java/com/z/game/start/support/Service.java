package com.z.game.start.support;

import com.z.game.start.core.Port;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Service {

    String serviceId() default "";

    Class<? extends Port> port() default Port.class;

}
