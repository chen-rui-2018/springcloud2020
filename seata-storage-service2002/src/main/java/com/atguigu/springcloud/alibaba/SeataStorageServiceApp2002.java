package com.atguigu.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:25
 * @version： v1.0
 * @modified By:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class SeataStorageServiceApp2002 {
    public static void main(String[] args) {

        SpringApplication.run(SeataStorageServiceApp2002.class,args);
    }
}
