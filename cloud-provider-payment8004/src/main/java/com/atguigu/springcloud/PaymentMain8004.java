package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author： chenr
 * @date： Created on 2021/6/30 15:13
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication
@EnableDiscoveryClient // 该注解用于向使用consul 或 zookeeper 作为注册中心时注册服务    a93b27e9-3080-44d6-897b-b0b463cf86a9
public class PaymentMain8004 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8004.class,args);
    }
}
