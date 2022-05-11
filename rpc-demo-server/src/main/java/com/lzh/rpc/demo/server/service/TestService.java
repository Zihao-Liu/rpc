package com.lzh.rpc.demo.server.service;

import com.lzh.demo.facade.TestFacade;
import com.lzh.rpc.server.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-11
 * @since 1.0.0
 */
@Service
@RpcService(type = TestFacade.class)
public class TestService implements TestFacade {
    @Override
    public String testMethod() {
        return "OK";
    }

    @Override
    public String testMethod(String foo) {
        return "HELLO: " + foo;
    }
}
