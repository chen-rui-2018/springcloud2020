package com.atguigu.springcloud.com.atguigu.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： chenr
 * @date： Created on 2021/7/14 14:03
 * @version： v1.0
 * @modified By:
 */

@RestController
@RefreshScope
public class ConfigClientController {
    @Value("${config.info}")
    private String configInfo;
    @Value("${server.port}")
    private String serverPort;
    @GetMapping("/configInfo")
    public String getConfigInfo(){
        return "server port: " + serverPort + "\t\n\n configInfo :" +  configInfo;
    }
}
