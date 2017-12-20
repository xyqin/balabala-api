package com.balabala.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xyqin on 2017/4/3.
 */
@Configuration
public class ErrorConfig {

    @Bean
    public CustomErrorAttributes errorAttributes() {
        return new CustomErrorAttributes();
    }

}
