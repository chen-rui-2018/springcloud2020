package com.atguigu.springcloud;

import com.atguigu.myrule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * @author： chenr
 * @date： Created on 2021/6/25 17:17
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication
@EnableEurekaClient
//@RibbonClient(name = "cloud-payment-service" ,configuration = MySelfRule.class)
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class,args);
    }
}
