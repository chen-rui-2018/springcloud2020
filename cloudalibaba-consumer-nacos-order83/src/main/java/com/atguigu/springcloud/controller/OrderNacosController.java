package com.atguigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author： chenr
 * @date： Created on 2021/7/20 14:48
 * @version： v1.0
 * @modified By:
 */
@RestController
@Slf4j
public class OrderNacosController {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.nacos-user-service}")
    private String serverUrl;
    @GetMapping(value = "/consumer/payment/nacos/{id}")
    public  String paymentInfo(@PathVariable(value = "id") Long  id){
        return restTemplate.getForObject(serverUrl+"/payment/nacos/"+id,String.class);
    }
}
