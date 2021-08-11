package com.atguigu.springcloud.alibaba.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:12
 * @version： v1.0
 * @modified By:
 */
@Mapper
public interface StorageDao {
    /**
     * 扣减 库存
     * @param productId
     * @param count
     */
    void decreaseStorage(@Param("productId") Long productId,@Param("count") Integer count);
}
