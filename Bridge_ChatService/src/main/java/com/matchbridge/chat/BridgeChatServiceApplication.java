package com.matchbridge.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.matchbridge") // controller, service 다 스캔
@MapperScan("com.matchbridge.chat.mapper") // MyBatis Mapper 위치
public class BridgeChatServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BridgeChatServiceApplication.class, args);
    }
}
