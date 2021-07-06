package com.atguigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author： chenr
 * @date： Created on 2021/7/5 14:29
 * @version： v1.0
 * @modified By:
 */
@Service
public class PaymentService {

    public String paymentInfo_OK(Integer id){
        return "线程池: " + Thread.currentThread().getName() + "paymentInfo_OK,id:"+id+"\t"+"O(∩_∩)O哈哈~";
    }
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")})
    public String paymentInfo_TimeOut(Integer id){
        int time = 3;

        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池: " + Thread.currentThread().getName() + "paymentInfo_TimeOut,id:"+id+"\t"+"O(∩_∩)O哈哈~"+ " 耗时"+time+"秒钟";
    }
    public String paymentInfo_TimeOutHandler(Integer id){

        return "线程池: " + Thread.currentThread().getName() + "8001服务繁忙或报错,请稍后再试!,id:"+id+"\t"+"o(╥﹏╥)o";
    }
    // == 服务熔断

    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback" ,commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), // 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间范围
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), // 失败率达到多少时断路
    })
    public String paymentCircuitBreaker(Integer id){
        if(id < 0) {
            throw new RuntimeException("id 不能为负数id ="+id);
        }
        String  serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" +"调试成功! 流水号:"+serialNumber;
    }
    public  String paymentCircuitBreaker_fallback(Integer id){
        return "id 不能为负数, 请稍后再试,o(╥﹏╥)o id:" + id;
    }
}
