package com.atguigu.springcloud.alibaba.dao;

import com.atguigu.springcloud.alibaba.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author： chenr
 * @date： Created on 2021/8/10 18:17
 * @version： v1.0
 * @modified By:
 */
@Mapper
public interface OrderDao {
    /**
     * 新建订单
     * @param order
     */
    void createOrder(Order order);

    /**
     * 修改订单状态
     * @param userId
     * @param status
     */
    void updateOrder(@Param("userId") Long userId,@Param("status") Integer status);
}
