package com.balabala.config;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Created by xyqin on 2017/4/1.
 * 使用redis作为session存储，传输方式为http头信息，session过期时间为30天
 */
@EnableRedisHttpSession(redisNamespace = "api", maxInactiveIntervalInSeconds = 2592000)
public class HttpSessionConfig {

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

}
