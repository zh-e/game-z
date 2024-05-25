package com.z.game.start.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MethodManager {

    public static final ConcurrentHashMap<String, Method> METHOD = new ConcurrentHashMap<>();

    public static void registerMethod(Class<?> clazz, Method method) {
        METHOD.put(createKey(clazz, method), method);
    }

    public static Method getMethod(String key) {
        return METHOD.get(key);
    }

    public static String createKey(Class<?> cls, Method method) {
        //类全路径
        String clazzName = cls.getName();
        //函数名
        String methodName = method.getName();
        //参数类型字符串
        String methodParam = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(",", "(", ")"));

        return clazzName + ":" + methodName + methodParam;
    }

}
