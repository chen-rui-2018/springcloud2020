package com.atguigu.springcloud.alibaba.serivce;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:11
 * @version： v1.0
 * @modified By:
 */
public interface StorageService {

    void decreaseStorage( Long productId,Integer count);
}
