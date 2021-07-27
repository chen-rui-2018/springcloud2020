package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author： chenr
 * @date： Created on 2021/7/21 15:24
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosConfigClientMain3377 {
    public static void main(String[] args) {
         SpringApplication.run(NacosConfigClientMain3377.class,args);

    }
}
