package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author： chenr
 * @date： Created on 2021/8/5 17:16
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosPaymentMain9004 {
    public static void main(String[] args) {
        SpringApplication.run(NacosPaymentMain9004.class,args);
    }
}
