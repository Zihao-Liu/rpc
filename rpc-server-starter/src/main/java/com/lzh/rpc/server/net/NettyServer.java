package com.lzh.rpc.server.net;

import com.google.common.base.Stopwatch;
import com.lzh.rpc.core.utils.HostUtil;
import com.lzh.rpc.core.utils.RunTimeUtil;
import com.lzh.rpc.server.factory.ServerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.*;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-12
 * @since 1.0.0
 */
@Slf4j
@Getter
public class NettyServer {

    private final ServerFactory serverFactory;

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final ServerBootstrap bootstrap;

    private final ExecutorService workerPool;

    private NettyServerHandler serverHandler;

    private NettyServer(ServerFactory serverFactory) {
        this.serverFactory = serverFactory;

        this.bossGroup = new NioEventLoopGroup(new DefaultThreadFactory("boss"));
        this.workerGroup = new NioEventLoopGroup(RunTimeUtil.processors(), new DefaultThreadFactory("workers"));

        this.bootstrap = new ServerBootstrap();

        int workers = serverFactory.getProperties().getWorkers();
        this.workerPool = new ThreadPoolExecutor(workers, workers, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000), new DefaultThreadFactory("worker-pool"));
    }

    public static NettyServer start(ServerFactory serverFactory) throws InterruptedException {
        StopWatch watch = StopWatch.createStarted();
        log.info("netty server is starting...");
        int port = serverFactory.getProperties().getPort();
        if (port == -1) {
            port = HostUtil.getAvailablePort();
        }
        if (port <= 0) {
            log.info("netty server port {} is unavailable", port);
            throw new RuntimeException("unavailable port " + port);
        }
        NettyServer server = new NettyServer(serverFactory);
        server.startAndWait(port);
        log.info("netty server is started, cost: {}", watch.getTime());
        return server;
    }

    private void startAndWait(int port) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        CompletableFuture.runAsync(() -> {
            ChannelFuture future = bootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(getChannelInitializer())
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind(port)
                    .syncUninterruptibly();
            log.info("netty server bind port {} success", port);

            latch.countDown();

            future.channel()
                    .closeFuture()
                    .syncUninterruptibly();
        });
        latch.await();
    }

    private ChannelInitializer<SocketChannel> getChannelInitializer() {
        this.serverHandler = new NettyServerHandler(this);

        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline()
                        .addLast(new IdleStateHandler(0, 0, 10 * 3, TimeUnit.SECONDS))
                        .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                        .addLast(new ObjectEncoder())
                        .addLast(serverHandler);
            }
        };
    }

}
