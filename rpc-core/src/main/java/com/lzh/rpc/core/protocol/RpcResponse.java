package com.lzh.rpc.core.protocol;

import com.lzh.rpc.core.constant.RpcConstant;
import com.lzh.rpc.core.constant.RpcResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-12
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    private String requestId;

    private Object result;

    private RpcResultCode code;

    private Throwable throwable;

    public static RpcResponse heartBeat() {
        return RpcResponse.builder()
                .requestId(RpcConstant.HEART_BEAT_ID)
                .build();
    }

    public static RpcResponse ok(String requestId, Object result) {
        return RpcResponse.builder()
                .requestId(requestId)
                .code(RpcResultCode.SUCCESS)
                .result(result)
                .build();
    }

    public static RpcResponse error(String requestId, Throwable t) {
        return RpcResponse.builder()
                .requestId(requestId)
                .code(RpcResultCode.ERROR)
                .throwable(t)
                .build();
    }

    public static class EmptyResult implements Serializable {
        private static final EmptyResult INSTANCE = new EmptyResult();

        private EmptyResult() {
        }

        public static EmptyResult build() {
            return INSTANCE;
        }
    }
}
