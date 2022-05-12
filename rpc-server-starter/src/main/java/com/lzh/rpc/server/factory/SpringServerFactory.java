package com.lzh.rpc.server.factory;

import com.lzh.rpc.server.net.NettyServer;
import com.lzh.rpc.server.properties.ServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import javax.swing.*;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
@Slf4j
public class SpringServerFactory extends BaseServerFactory {

    protected SpringServerFactory(ServerProperties serverProperties) throws InterruptedException {
        super(serverProperties);
        this.start();
    }

    @Override
    public void start() throws InterruptedException {
        StopWatch watch = StopWatch.createStarted();
        log.info("spring server factory is starting...");
        synchronized (SpringServerFactory.class) {
            if (!super.getProperties().isEnabled()) {
                log.info("server is not enabled, skip start");
                return;
            }
            NettyServer.start(this);
        }
        log.info("spring server factory is started, cost: {}", watch.getTime());
    }
}
