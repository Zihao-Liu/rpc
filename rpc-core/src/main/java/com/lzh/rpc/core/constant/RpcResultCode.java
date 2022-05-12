package com.lzh.rpc.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-12
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum RpcResultCode {
    /**
     * code
     */
    SUCCESS(0),
    ERROR(500),
    ;
    private final int code;
}
