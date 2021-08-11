package com.atguigu.springcloud.alibaba.service.impl;

import com.atguigu.springcloud.alibaba.dao.OrderDao;
import com.atguigu.springcloud.alibaba.domain.Order;
import com.atguigu.springcloud.alibaba.service.AccountService;
import com.atguigu.springcloud.alibaba.service.OrderService;
import com.atguigu.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 10:57
 * @version： v1.0
 * @modified By:
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AccountService accountService;

    /**
     *下订单--> 扣库存-->减账户(余额)-->改订单状态;
     * @param order
     */
    @Override
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    public void createOrder(Order order) {
        log.info("=========>> 开始新建订单");
        orderDao.createOrder(order);

        log.info("=========>> 订单微服务开始调用库存,做扣减 start");
        storageService.decreaseStorage(order.getProductId(),order.getCount());
        log.info("=========>> 订单微服务开始调用库存,做扣减 end");

        log.info("=========>> 订单微服务开始调用账户余额,做扣减 start");
        accountService.decreaseMoney(order.getUserId(),order.getMoney());
        log.info("=========>> 订单微服务开始调用账户余额,做扣减 end");

        // 修改订单的状态 从 0 到 1 ,1代表已完成;
        log.info("=========>> 修改订单状态  start");
        orderDao.updateOrder(order.getUserId(),0);
        log.info("=========>> 修改订单状态  end");

        log.info("=========>> 下订单完成  O(∩_∩)O哈哈~");
    }

    @Override
    public void updateOrder(Long userId, Integer status) {

    }
}
