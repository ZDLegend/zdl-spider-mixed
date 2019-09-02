package com.zdl.spider.mixed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class SpiderMixedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderMixedApplication.class, args);
    }

}
