package com.lzh.rpc.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-07
 * @since 1.0.0
 */
@Slf4j
public final class HostUtil {
    /**
     * 本地地址
     */
    private static InetAddress LOCAL_HOST;

    static {
        try {
            LOCAL_HOST = Inet4Address.getLocalHost();
            log.info("rpc host: {}", LOCAL_HOST.getHostAddress());
        } catch (Exception e) {
            log.error("getLocalHost error, ", e);
        }
    }

    private HostUtil() {

    }

    public static String getHostAddress() {
        return LOCAL_HOST.getHostAddress();
    }
}
