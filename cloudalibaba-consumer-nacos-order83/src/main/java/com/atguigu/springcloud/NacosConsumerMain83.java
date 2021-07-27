package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author： chenr
 * @date： Created on 2021/7/19 18:00
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosConsumerMain83 {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerMain83.class,args);
    }
}
