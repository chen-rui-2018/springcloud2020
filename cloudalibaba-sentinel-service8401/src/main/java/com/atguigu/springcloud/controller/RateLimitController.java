package com.atguigu.springcloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.myhandler.CustomerBlockHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： chenr
 * @date： Created on 2021/8/4 17:03
 * @version： v1.0
 * @modified By:
 */
@RestController
public class RateLimitController  {
    @GetMapping(value = "/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handlerException")
    public CommonResult byResource(){
        return  new CommonResult(200,"按资源名称限流测试OK",new Payment(2021L,"serial001"));
    }

    public CommonResult handlerException(BlockException e){
        return  new CommonResult(444,e.getClass().getCanonicalName()+"\t 服务不可用");
    }

    @GetMapping(value = "/rateLimit/byUrl")
    @SentinelResource(value = "byUrl",blockHandler = "handlerException")
    public CommonResult byUrl(){
        return  new CommonResult(200,"按url限流测试OK",new Payment(2021L,"serial002"));
    }

    @GetMapping(value = "/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException")
    public CommonResult customerBlockHandler(){
        return  new CommonResult(200,"按客户自定义 限流测试OK",new Payment(2021L,"serial002"));
    }
    @GetMapping(value = "/rateLimit/customerBlockHandler2")
    @SentinelResource(value = "customerBlockHandler2",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException2")
    public CommonResult customerBlockHandler2(){
        return  new CommonResult(200,"按客户自定义 限流测试OK",new Payment(2021L,"serial002"));
    }


}
