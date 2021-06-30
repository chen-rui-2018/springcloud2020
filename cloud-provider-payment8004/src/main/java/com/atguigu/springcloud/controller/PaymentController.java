package com.atguigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author： chenr
 * @date： Created on 2021/6/30 15:23
 * @version： v1.0
 * @modified By:
 */
@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/zk")
    public Object paymentzk() {
         return "SpringCloud with zookeeper: " + serverPort + "\t" + UUID.randomUUID().toString();
    }

}
