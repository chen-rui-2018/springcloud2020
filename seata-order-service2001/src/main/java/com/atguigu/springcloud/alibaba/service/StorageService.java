package com.atguigu.springcloud.alibaba.service;

import com.atguigu.springcloud.alibaba.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 10:56
 * @version： v1.0
 * @modified By:
 */
@FeignClient(value = "seata-storage-service")
public interface StorageService {
    /**
     * 扣减 库存
     * @param productId
     * @param count
     * @return
     */
    @PostMapping(value = "/storage/decreaseStorage")
    CommonResult decreaseStorage(@RequestParam("productId") Long productId,@RequestParam("count") Integer count);
}
