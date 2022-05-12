package com.lzh.rpc.server.properties;

import com.lzh.rpc.core.constant.RpcConstant;
import com.lzh.rpc.core.utils.RunTimeUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-07
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = RpcConstant.SERVER_PROPERTY_PREFIX)
public class ServerProperties {
    /**
     * 是否启用Rpc Server，默认为false不会开启
     */
    private boolean enabled = false;
    /**
     * Server启动端口号，端口号为-1表示自动扫描可用端口号
     */
    private int port = -1;
    /**
     * 服务线程数量
     */
    private int workers = RunTimeUtil.processors() * 2;
}
