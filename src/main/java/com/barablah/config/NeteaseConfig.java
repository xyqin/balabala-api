package com.barablah.config;

import com.barablah.netease.NeteaseClient;
import com.barablah.netease.NeteaseProperties;
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
