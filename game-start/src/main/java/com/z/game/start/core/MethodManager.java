package com.z.game.start.core;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class MethodManager {

    public static final ConcurrentHashMap<String, Method> METHOD = new ConcurrentHashMap<>();

    public static Method getMethod(String key) {
        return METHOD.get(key);
    }

}
