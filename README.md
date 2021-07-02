# springcloud2020
spingcloud 学习 一
1:新建 父工程project空间创建  

2.服务注册中心总结:
  组件名称  语言  cap  健康服务检查  对外暴露接口  springcloud 集成
  Eureka   java  AP   可配支持      http         已集成
  Consul   go    CP   支持          http/dns     已集成
  Zookeeper java CP   支持          客户端        已集成
  
3. CPA
  CAP: 理论关注粒度是数据,而不是整体系统设计的;
  C: consistency 强一致性
  A: availability 可用性
  P: partition tolerance 分区容错性
  最多只能同时较好的满足两个;
  CAP理论核心是:一个分布式系统不可能很好的满足一致性,可用性,和分区容错性这三个需求;
  因此 根据CAP原理将 noSql数据库分成了满足CA原则,满足CP原则和满足AP原则三大类;
  CA: 单点集群 满足一致性 可用性的系统 通常在扩展性上不太强;
  CP: 满足一致性,分区容忍性的系统,通常性能不是特别高;
  AP: 满足可用性,分区容忍性的系统,通常可能对一致性要求要低一些;
  
4.  Ribbon: 维护模式
  Spring cloud ribbon 是基于 netflix ribbon 实现的一套 客户端 负载均衡的工具;
  
  简单的说, ribbon 是Netflix发布的开源项目 主要功能是提供客户端的软件负载均衡算法和服务调用, ribbon 客户端组件提供一系列完善的配置项- 
  - 连接超时 重试等. 简单的说 就是在配置文件中列出load balancer (简称LB) 后面所有的机器, ribbon 会自动的帮助你基于某种规则(轮询/随机...)
  去连接这些机器,我们很容易使用ribbon 实现自定义的负载均衡算法;

  Nginx vs Ribbon
  Nginx 是服务端负载均衡, 客户所有的请求都会交给nginx,然后由nginx实现转发请求,即负载均衡是由服务端实现的;
  Ribbon  本地负载均衡,在调用微服务接口时,会在注册中心上获取注册信息服务列表之后缓存到jvm本地,从而在本地实现RPC远程服务调用技术;
  
  集中是LB: 在服务的消费方和提供方之间使用独立的LB设施(硬件,F5  软件 nginx),由改设施负责把访问请求通过某种策略转发至服务提供方;
  进程内LB: 将LB逻辑集成到消费方,消费方从服务注册中心获知有哪些地址可用,然后自己再从这些地址中选择出一个合适的服务器.
           Ribbon就属于进程内LB,它只是一个类库,集成与消费方进程,消费方通过它来获取到服务提供方地址;
           
  Ribbon 在工作时分成两步
  一,先选择EurekaServer 它优先选择在同一区域内负载较少的server;
  二,再根据用户指定的策略,从server 取到的服务注册列表中选择一个地址;
  IRule->AbstractLoadBalancerRule
  RoundRobinRule : 轮询
  RandomRule : 随机
  RetryRule : 先按照RoundRobinRule的策略获取服务,如果获取失败则在指定时间内会进行重试;
  WeightedResponseTimeRule : 对 RoundRobinRule 的扩展,响应速度越快的实例选择权重越大,越容易被选择;
  BestAvailableRule : 会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务,然后选择一个并发量最小的服务;
  AvailabilityFilteringRul : 会先过滤掉故障实例,选择并发量较小的实例;
  ZoneAvoidanceRule : 默认规则,复合判断server 所在区域的性能和server的可用性选择服务器;
  
  负载均衡算法: rest 接口第几次请求数% 服务器集群数量 = 实际调用的服务器位置下标,每次服务重启后rest计数从1开始
  List<ServiceInstance> instances  = discoveryClient.getInstance("cloud-payment-service");
  list[0] = 127.0.0.1:8001
  list[1] = 127.0.0.1:8002
  8001 和 8002组成集群,总数为2
  1 1%2 = 1 list[1] 127.0.0.1:8002 
  2 2%2 = 0 list[0] 127.0.0.1:8001
  3 3%2 = 1 list[1] 127.0.0.1:8002
  
5.RestTemplate
  getForObject  返回对象为响应实体中数据转化成的对象,基本可以理解为json
  getForEntity  返回对象为ResponseEntity 对象,包含了响应中的一些重要信息,比如响应头,状态码,响应体等;
  postForObject
  postForEntity
  GET
  POST
6.Feign
  Feign是一个声明式的WebService客户端.使用Feign能让编写web service 客户端更加简单
  它的使用方法是定义一个服务接口然后再上面添加注解. Feign 也支持可拔插式的编码器和解码器.
  SpringCloud 对Feign进行了封装 使其支持了Spring MVC 标准注解 和 HttpMessageConverters. 
  Feign 可以与Eureka 和Ribbon 组合使用 以支持负载均衡;
  
  Feign 旨在使编写java http 客户端变得更加容易.
  之前使用 Ribbon + RestTemplate 时,利用RestTemplate 对http请求进行处理,形成了一套模板化的调用方法.
  但在实际开发中,由于对服务依赖的调用可能不止一处,往往一个接口会被多处调用,所以通常会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用;
  所以Feign 在此基础上做了进一步封装,由他来帮助我们定义和实现依赖服务接口的定义.
  在Feign 的实现下, 我们只需要创建一个接口并且使用注解的方式来配置它(example: Dao 上标注 Mapper注解, 微服务接口上标注 Feign注解)
  即可完成对服务提供方的接口绑定,简化了使用spring cloud Ribbon 时,自动封装服务调用客户端的开发量;
  
  Feign 集成的 Ribbon
  利用Ribbon维护payment的服务列表信息, 并且通过轮询实现了客户端负载均衡,而与Ribbon不同的是 通过Feign 只需要定义服务绑定接口且以声明式的方法,
  优雅而简单的实现服务调用;
  
  main : @EnableFeignClients
  service(interface) :  @FeignClient(value = "cloud-payment-service") value 为服务名称 
  method : 与要调用的controller 保持一致   @GetMapping("/payment/getPaymentById/{id}")
                                         CommonResult<Payment> getPaymentById(@PathVariable(value = "id") long id) ;
                                         
  feign : 日志级别
  NONE : 默认的, 不显示任何日志;
  BASIC : 仅记录请求方法, URL ,响应状态码及执行时间;
  HEADERS : 除了basic 中定义的信息外,还有请求和响应头的信息;
  FULL :    除了 headers 中定义的信息之外, 还有请求和响应的正文及元数据;                                      
                     