package com.finance4car.config;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Created by xyqin on 2017/4/1.
 */
@EnableRedisHttpSession(redisNamespace = "api", maxInactiveIntervalInSeconds = 2592000)
public class HttpSessionConfig {

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

}
