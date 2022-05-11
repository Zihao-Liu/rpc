package com.lzh.rpc.demo.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-07
 * @since 1.0.0
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.lzh.rpc.demo.server"})
public class RpcDemoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcDemoServerApplication.class, args);
    }
}
