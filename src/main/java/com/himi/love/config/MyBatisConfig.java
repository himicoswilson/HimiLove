package com.himi.love.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.himi.love.mapper")
public class MyBatisConfig {
}