package com.lzh.rpc.server.annotation;

import java.lang.annotation.*;

/**
 * RPC服务注解，在需要对外暴露的Service打上注解
 *
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {
    /**
     * 对外暴露的Service名称，不指定则使用type.getName();
     *
     * @return Service名称
     */
    String value() default "";

    /**
     * 服务类型
     *
     * @return 实现的服务类型
     */
    Class<?> type();
}
