package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author： chenr
 * @date： Created on 2021/7/2 14:01
 * @version： v1.0
 * @modified By:
 */
public interface LoadBalancer {

    ServiceInstance instances(List<ServiceInstance> serviceInstanceList);

}
