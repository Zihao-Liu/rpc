package com.lzh.rpc.server;

import com.lzh.rpc.core.utils.GsonUtil;
import com.lzh.rpc.server.exporter.ServerPostProcessor;
import com.lzh.rpc.server.factory.BaseServerFactory;
import com.lzh.rpc.server.factory.ServerFactory;
import com.lzh.rpc.server.factory.SpringServerFactory;
import com.lzh.rpc.server.properties.ServerProperties;
import com.lzh.rpc.server.properties.YmlPropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-07
 * @since 1.0.0
 */
@Slf4j
@PropertySources(value = {
        @PropertySource(value = "classpath:rpc.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:rpc.yml", ignoreResourceNotFound = true, factory = YmlPropertySourceFactory.class),
        @PropertySource(value = "classpath:rpc-server.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:rpc-server.yml", ignoreResourceNotFound = true, factory = YmlPropertySourceFactory.class)
})
@EnableConfigurationProperties({ServerProperties.class})
public class ServerAutoConfiguration {
    @Resource
    private ServerProperties serverProperties;

    @PostConstruct
    private void post() {
        log.info("server properties: {}", GsonUtil.toJson(serverProperties));
    }

    @Primary
    @Bean(name = "springServerFactory")
    @ConditionalOnMissingBean(ServerFactory.class)
    @ConditionalOnProperty(prefix = "rpc.server", name = "enabled", havingValue = "true")
    public SpringServerFactory springServerFactory() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return BaseServerFactory.init(SpringServerFactory.class, serverProperties);
    }

    @Bean(name = "serverPostProcessor")
    @DependsOn({"springServerFactory"})
    @ConditionalOnProperty(prefix = "rpc.server", name = "enabled", havingValue = "true")
    public ServerPostProcessor serverPostProcessor() {
        return new ServerPostProcessor();
    }
}
