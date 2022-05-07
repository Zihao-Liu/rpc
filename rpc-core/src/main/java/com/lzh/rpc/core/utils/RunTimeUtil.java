package com.lzh.rpc.core.utils;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-07
 * @since 1.0.0
 */
public final class RunTimeUtil {
    private RunTimeUtil() {

    }

    public static int processors() {
        return Runtime.getRuntime().availableProcessors();
    }
}
