package com.atguigu.springcloud.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * @author： chenr
 * @date： Created on 2021/7/30 15:49
 * @version： v1.0
 * @modified By:
 */
@Service
public class ResourceService {
    @SentinelResource("doSomething")
    public String doSomething(){
        return "=====> doSomething";
    }
    @SentinelResource("NotDoSomething")
    public String notDoSomething(){
        return "=====> notDoSomething";
    }
}
