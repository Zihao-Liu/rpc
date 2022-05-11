package com.lzh.rpc.server.factory;

import com.lzh.rpc.server.properties.ServerProperties;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
public class SpringServerFactory extends BaseServerFactory {

    protected SpringServerFactory(ServerProperties serverProperties) {
        super(serverProperties);
    }

    @Override
    public void start() {

    }
}
