package com.atguigu.cloud.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {
    @Bean
    public Retryer retryer(){
//        默认关闭
        return Retryer.NEVER_RETRY;
//        开启（初始重传间隔100ms，最大重传间隔1s，总共尝试传3次）
//        return new  Retryer.Default(100, 1, 3);
    }
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
