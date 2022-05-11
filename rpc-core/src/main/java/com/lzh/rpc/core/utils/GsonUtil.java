package com.lzh.rpc.core.utils;

import com.google.gson.Gson;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-07
 * @since 1.0.0
 */
public final class GsonUtil {

    private static final Gson GSON = new Gson();

    private GsonUtil() {
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
