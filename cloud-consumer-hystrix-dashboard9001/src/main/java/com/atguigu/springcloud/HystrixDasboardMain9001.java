package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author： chenr
 * @date： Created on 2021/7/6 17:24
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDasboardMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDasboardMain9001.class,args);
    }
}
