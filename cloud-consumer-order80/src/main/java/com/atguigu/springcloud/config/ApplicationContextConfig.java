package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author： chenr
 * @date： Created on 2021/6/25 17:24
 * @version： v1.0
 * @modified By:
 */
@Configuration
public class ApplicationContextConfig {
    /**
     * 使用@LoadBalanced 注解 赋予RestTemplate负载均衡的能力
     * @return
     */
    @Bean
//    @LoadBalanced
    public RestTemplate getRestTemplate(){

        return new RestTemplate();
    }
}
