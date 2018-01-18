package com.barablah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by xyqin on 2017/4/1.
 */
@EnableScheduling
@SpringBootApplication
public class BarablahApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarablahApplication.class, args);
    }

}
