package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： chenr
 * @date： Created on 2021/7/2 16:31
 * @version： v1.0
 * @modified By:
 */
@RestController
@Slf4j
public class OrderFeignController {
    @Autowired
    private PaymentFeignService service;
    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable long id){
        return  service.getPaymentById(id);
    }

    @GetMapping("/consumer/payment/feign/timeout")
    public String paymentFeignTimeout(){
        //openFeign Ribbon  客户端一般默认等待1秒钟
        return service.paymentFeignTimeout();
    }
}
