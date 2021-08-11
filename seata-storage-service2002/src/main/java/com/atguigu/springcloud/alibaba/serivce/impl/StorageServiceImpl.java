package com.atguigu.springcloud.alibaba.serivce.impl;

import com.atguigu.springcloud.alibaba.dao.StorageDao;
import com.atguigu.springcloud.alibaba.serivce.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author： chenr
 * @date： Created on 2021/8/11 14:19
 * @version： v1.0
 * @modified By:
 */
@Service
@Slf4j
public class StorageServiceImpl  implements StorageService {

    @Autowired
    private StorageDao storageDao;

    @Override
    public void decreaseStorage(Long productId, Integer count) {
        log.info("扣减 库存开始");
        storageDao.decreaseStorage(productId,count);
        log.info("扣减 库存结束");

    }
}
