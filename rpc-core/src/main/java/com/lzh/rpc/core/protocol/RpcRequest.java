package com.lzh.rpc.core.protocol;

import com.lzh.rpc.core.constant.RpcConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-12
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    private String requestId;

    private String className;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] params;

    public static RpcRequest heartBeat() {
        return RpcRequest.builder()
                .requestId(RpcConstant.HEART_BEAT_ID)
                .build();
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
