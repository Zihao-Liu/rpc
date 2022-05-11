package com.lzh.rpc.server.factory;

import com.lzh.rpc.server.properties.ServerProperties;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
public abstract class BaseServerFactory implements ServerFactory {

    private final ServerProperties serverProperties;

    protected BaseServerFactory(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    public static <T extends ServerFactory> T init(Class<T> clazz, ServerProperties serverProperties) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return clazz.getDeclaredConstructor(ServerProperties.class).newInstance(serverProperties);
    }

    @Override
    public ServerProperties getProperties() {
        return serverProperties;
    }
}
