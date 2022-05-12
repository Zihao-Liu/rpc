package com.lzh.rpc.server.factory;

import com.lzh.rpc.server.properties.ServerProperties;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
public interface ServerFactory {
    ServerProperties getProperties();

    void start() throws InterruptedException;
}
