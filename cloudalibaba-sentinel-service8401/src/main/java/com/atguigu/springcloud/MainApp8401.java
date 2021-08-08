package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author： chenr
 * @date： Created on 2021/7/28 10:44
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MainApp8401 {
    public static void main(String[] args) {
        SpringApplication.run(MainApp8401.class,args);
    }
}
