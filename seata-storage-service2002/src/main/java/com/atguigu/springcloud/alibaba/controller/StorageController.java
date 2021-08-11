package com.atguigu.springcloud.alibaba.controller;

import com.atguigu.springcloud.alibaba.domain.CommonResult;
import com.atguigu.springcloud.alibaba.serivce.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:21
 * @version： v1.0
 * @modified By:
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    /**
     * 扣减 库存
     *
     * @param productId
     * @param count
     * @return
     */
    @PostMapping(value = "/decreaseStorage")
    public CommonResult decreaseStorage(@RequestParam("productId") Long productId, @RequestParam("count") Integer count) {
        storageService.decreaseStorage(productId, count);
        return new CommonResult(200, "扣减库存成功!");
    }
}
