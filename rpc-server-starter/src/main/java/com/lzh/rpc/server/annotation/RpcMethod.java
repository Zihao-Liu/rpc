package com.lzh.rpc.server.annotation;

import java.lang.annotation.*;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcMethod {
    long timeout() default 0L;
}
