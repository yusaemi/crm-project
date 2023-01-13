package com.sample.crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * RedisConfig. 2020/11/21 7:46 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Component
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
