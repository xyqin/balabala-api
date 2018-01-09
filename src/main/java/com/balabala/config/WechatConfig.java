package com.balabala.config;

import com.balabala.wechat.WechatProperties;
import com.balabala.wechat.mp.WxMpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatConfig {

    @Bean
    public WxMpClient wechatAppClient(WechatProperties wechatAppProperties) {
        return new WxMpClient(wechatAppProperties);
    }

    @Bean
    public WxMpClient wechatWebClient(WechatProperties wechatWebProperties) {
        return new WxMpClient(wechatWebProperties);
    }

    @Bean
    @ConfigurationProperties("wechat.app")
    public WechatProperties wechatAppProperties() {
        return new WechatProperties();
    }

    @Bean
    @ConfigurationProperties("wechat.web")
    public WechatProperties wechatWebProperties() {
        return new WechatProperties();
    }

}
