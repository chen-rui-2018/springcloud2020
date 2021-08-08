package com.atguigu.springcloud.myhandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;

/**
 * @author： chenr
 * @date： Created on 2021/8/4 17:25
 * @version： v1.0
 * @modified By:
 */

public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException exception){
        return  new CommonResult(444,"按客户自定义,global handlerException-------1");
    }
    public static  CommonResult handlerException2(BlockException exception){
        return  new CommonResult(444,"按客户自定义,global handlerException-------2");
    }
}
