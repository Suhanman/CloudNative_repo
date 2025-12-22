package com.matchbridge.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.matchbridge") // controller, service 다 스캔
@MapperScan("com.matchbridge.auth.mapper") // MyBatis Mapper 위치
public class BridgeAuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BridgeAuthServiceApplication.class, args);
    }
}
