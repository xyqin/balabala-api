package com.balabala.config;

import com.balabala.netease.NeteaseClient;
import com.balabala.netease.NeteaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NeteaseConfig {

    @Bean
    public NeteaseClient neteaseClient(NeteaseProperties neteaseProperties) {
        return new NeteaseClient(neteaseProperties);
    }

    @Bean
    @ConfigurationProperties("netease")
    public NeteaseProperties neteaseProperties() {
        return new NeteaseProperties();
    }

}
