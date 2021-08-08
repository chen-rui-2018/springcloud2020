package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author： chenr
 * @date： Created on 2021/7/28 17:56
 * @version： v1.0
 * @modified By:
 */
@RestController
@Slf4j
public class ConsumerController {

    public  static final  String SERVICE_URL = "http://nacos-payment-provider";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/consumer/fallback/{id}")
//    @SentinelResource(value = "fallback") //没有配置
//    @SentinelResource(value = "fallback",fallback = "handlerFallback") //只配置fallback
//    @SentinelResource(value = "fallback",blockHandler = "blockHandler") //只配置blockHandler
    @SentinelResource(value = "fallback",fallback = "handlerFallback",blockHandler = "blockHandler",exceptionsToIgnore = {IllegalArgumentException.class}) // 配置blockHandler,配置fallback
    public CommonResult<Payment> fallback(@PathVariable("id") Long id) {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id, CommonResult.class);
        if (id == 4){
            throw new IllegalArgumentException("IllegalArgumentException ,非法的参数异常");
        }else if (result.getData() == null){
            throw new NullPointerException("NullPointerException ,该ID 没有对应的记录, 空指针 异常");
        }
        return result;
    }
    //falback 示例
    public CommonResult handlerFallback(@PathVariable("id") Long id,Throwable e) {
        Payment payment = new Payment(id,"null");

        return new CommonResult(444,"兜底异常 handlerFallback ,exception 内容 "+ e.getMessage(),payment) ;
    }
    //blockHandler 示例
    public CommonResult blockHandler(@PathVariable("id") Long id, BlockException e) {
        Payment payment = new Payment(id,"null");

        return new CommonResult(445,"blockHandler-sentinel限流 ,blockException 内容 "+ e.getMessage(),payment) ;
    }


    @GetMapping(value = "/consumer/paymentSQL/{id}")
    @SentinelResource(value = "openFallback",blockHandler = "blockHandler")
    public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id){
        return paymentService.paymentSQL(id);
    }


}

