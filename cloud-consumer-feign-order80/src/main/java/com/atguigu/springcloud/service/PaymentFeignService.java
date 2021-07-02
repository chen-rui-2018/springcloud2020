package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author： chenr
 * @date： Created on 2021/7/2 16:25
 * @version： v1.0
 * @modified By:
 */
@Component
@FeignClient(value = "cloud-payment-service")
public interface PaymentFeignService {

    @GetMapping("/payment/getPaymentById/{id}")
    CommonResult<Payment> getPaymentById(@PathVariable(value = "id") long id) ;

    @GetMapping("/payment/feign/timeout")
    String paymentFeignTimeout();
}
