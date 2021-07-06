package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author： chenr
 * @date： Created on 2021/7/5 15:26
 * @version： v1.0
 * @modified By:
 */
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_global_fallbackMethod")
public class OrderHystrixController {
    @Resource
    private PaymentHystrixService paymentHystrixService;

    @RequestMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result = paymentHystrixService.paymentInfo_OK(id);
        log.info("=============="+result);
        return result;
    }
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")})
    @RequestMapping("/consumer/payment/hystrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id){
        int age = 10/0;
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        log.info("============"+result);
        return result;
    }
    @HystrixCommand
    @RequestMapping("/consumer/payment/hystrix/timeout/default/{id}")
    public String paymentInfo_TimeOut_default(@PathVariable("id") Integer id){
        int age = 10/0;
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        log.info("============"+result);
        return result;
    }
    public String paymentInfo_TimeOutHandler(Integer id){

        return "线程池: " + Thread.currentThread().getName() + "我是80消费端_服务端繁忙或报错 请检查自身,请稍后再试!,id:"+id+"\t"+"o(╥﹏╥)o";
    }

    // 下面是全局fallback
    public String payment_global_fallbackMethod(){
        return "Global异常信息处理,请稍后再试,(＃￣～￣＃)";
    }
}
