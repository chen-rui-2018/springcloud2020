package com.atguigu.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author： chenr
 * @date： Created on 2021/7/12 16:48
 * @version： v1.0
 * @modified By:
 */
@Configuration
public class GateWayConfig {
    @Bean
    public RouteLocator myRouters(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_atguigu",r->r.path("/guonei").uri("http://news.baidu.com/guonei")).build();
        routes.route("path_route_atguigu2",r->r.path("/guoji").uri("http://news.baidu.com/guoji")).build();
        return routes.build();
    }
}
