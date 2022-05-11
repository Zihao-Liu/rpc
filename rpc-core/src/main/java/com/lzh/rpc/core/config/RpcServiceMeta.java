package com.lzh.rpc.core.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcServiceMeta {
    /**
     * 服务名称
     */
    private String name;
    /**
     * 服务的bean对象
     */
    private Object bean;
    /**
     * 接口类型
     */
    public Class<?> type;
}
