package com.lzh.rpc.server.exporter;

import com.lzh.rpc.core.config.RpcServiceMeta;
import com.lzh.rpc.server.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
@Slf4j
public class ServerPostProcessor implements BeanPostProcessor {
    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService annotation = bean.getClass().getAnnotation(RpcService.class);
        if (Objects.isNull(annotation)) {
            return bean;
        }
        String name = StringUtils.defaultIfBlank(annotation.value(), annotation.type().getName());

        RpcServiceMeta meta = RpcServiceMeta.builder()
                .name(name)
                .bean(bean)
                .type(annotation.type())
                .build();

        ServerExporterFactory.putService(meta);
        return bean;
    }
}
