package com.lzh.rpc.server.net;

import com.lzh.rpc.core.config.RpcServiceMeta;
import com.lzh.rpc.core.protocol.RpcRequest;
import com.lzh.rpc.core.protocol.RpcResponse;
import com.lzh.rpc.server.annotation.RpcMethod;
import com.lzh.rpc.server.annotation.RpcService;
import com.lzh.rpc.server.exporter.ServerExporterFactory;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-12
 * @since 1.0.0
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {
    private static final String HEART_BEAT_ID = "rpc-heart-beat";

    private final NettyServer nettyServer;

    protected NettyServerHandler(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof RpcRequest)) {
            ctx.fireChannelRead(msg);
            return;
        }
        RpcRequest request = (RpcRequest) msg;
        if (HEART_BEAT_ID.equals(request.getRequestId())) {
            log.debug("hear heart beat from -> {}", ctx.channel().remoteAddress());
            return;
        }
        log.debug("channelRead -> {}", ctx.channel().remoteAddress());
        processMessage(ctx, request);
    }

    private void processMessage(ChannelHandlerContext ctx, RpcRequest request) {
        RpcResponse response;
        try {
            String className = request.getClassName();
            RpcServiceMeta meta = ServerExporterFactory.find(className);
            if (Objects.isNull(meta)) {
                log.error("class not found, {}", className);
                throw new ClassNotFoundException(className);
            }
            handle(ctx, meta.getBean(), request);
        } catch (Exception e) {
            response = RpcResponse.error(request.getRequestId(), e);
            sendResponse(ctx, response);
        }
    }

    private void handle(ChannelHandlerContext ctx, Object bean, RpcRequest request) throws NoSuchMethodException {
        Class<?> clazz = bean.getClass();
        Method method = clazz.getMethod(request.getMethodName(), request.getParamTypes());

        Long timeout = getTimeout(method, clazz);
        if (Objects.isNull(timeout) || timeout <= 0L) {
            CompletableFuture.runAsync(() -> {
                RpcResponse response = invoke(bean, method, request);
                sendResponse(ctx, response);
            });
            return;
        }
        ExecutorService executors = nettyServer.getWorkerPool();
        CompletableFuture
                .supplyAsync(() -> invoke(bean, method, request), executors)
                .acceptEitherAsync(
                        timeoutFuture(request, timeout),
                        (either) -> sendResponse(ctx, either),
                        executors
                );
    }

    private void sendResponse(ChannelHandlerContext ctx, RpcResponse response) {
        if (ctx.channel().isActive()) {
            ctx.channel().writeAndFlush(response);
        }
    }

    private Long getTimeout(Method method, Class<?> clazz) {
        RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
        if (Objects.nonNull(rpcMethod) && rpcMethod.timeout() > 0L) {
            return rpcMethod.timeout();
        }
        RpcService rpcService = clazz.getAnnotation(RpcService.class);
        if (Objects.nonNull(rpcService) && rpcService.timeout() > 0L) {
            return rpcService.timeout();
        }
        return null;
    }

    private RpcResponse invoke(Object obj, Method method, RpcRequest request) {
        RpcResponse response;
        Object result;
        try {
            if (method.getReturnType() == void.class) {
                result = RpcResponse.EmptyResult.build();
                method.invoke(obj, request.getParams());
            } else {
                result = method.invoke(obj, request.getParams());
            }
            response = RpcResponse.ok(request.getRequestId(), result);
        } catch (Exception e) {
            response = RpcResponse.error(request.getRequestId(), e);
        }
        return response;
    }

    private CompletableFuture<RpcResponse> timeoutFuture(RpcRequest request, Long timeout) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                return RpcResponse.error(request.getRequestId(), e);
            }
            return null;
        }, nettyServer.getWorkerPool());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.channel().close();
    }
}
