package com.lzh.rpc.server.exporter;

import com.google.common.collect.Maps;
import com.lzh.rpc.core.config.RpcServiceMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
@Slf4j
public class ServerExporterFactory {
    public static final Map<String, RpcServiceMeta> META_MAP = Maps.newConcurrentMap();

    public static void putService(RpcServiceMeta meta) {
        log.info("put rpc service: {}", meta.toString());
        META_MAP.compute(meta.getName(), (key, old) -> {
            if (Objects.nonNull(old)) {
                log.error("duplicate rpc service: {}, expected one", meta.getName());
                // TODO rpc exception
                throw new RuntimeException();
            }
            return meta;
        });
    }
}
