package com.matchbridge.match;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.matchbridge") 
@MapperScan("com.matchbridge.match.mapper") 
public class BridgeMatchServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BridgeMatchServiceApplication.class, args);
    }
}
