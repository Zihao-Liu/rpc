package com.lzh.rpc.core.utils;

import com.lzh.rpc.core.constant.RpcConstant;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

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

    public static int getAvailablePort() {
        int port = RpcConstant.SERVER_PORT;
        int retryTimes = 10;
        Socket socket = new Socket();
        do {
            try {
                socket.bind(new InetSocketAddress(getHostAddress(), port));
                socket.close();
                return port;
            } catch (Exception e) {
                log.warn("skip bind port: {}, because is unavailable", port);
            }
            port += 10;
        } while (retryTimes-- > 0);
        return 0;
    }
}
