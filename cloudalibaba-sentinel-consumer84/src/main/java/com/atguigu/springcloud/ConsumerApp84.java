package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author： chenr
 * @date： Created on 2021/7/28 17:54
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApp84 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp84.class,args);
    }

}
