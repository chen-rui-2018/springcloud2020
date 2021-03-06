# springcloud2020
spingcloud 学习 一
1:新建 父工程project空间创建  

2.服务注册中心总结:
  组件名称  语言  cap   健康服务检查  对外暴露接口  springcloud 集成
  Eureka   java  AP    可配支持      http         已集成
  Consul   go    CP    支持          http/dns     已集成
  Zookeeper java CP    支持          客户端        已集成
  Nacos   java  AP/CP  支持          客户端        已集成
  
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

7.Hystrix 
  Hystrix 是一个用于处理分布式系统的 延迟 和 容错 的开源库,在分布式系统里,许多依赖不可避免的会调用失败,比如超时,异常等; 
  Hystrix 能够保证在一个依赖出问题的情况下, 不会导致整体服务失败, 避免级联故障 ,以提高分布式系统的弹性;
  
  "断路器" 本身是一种开关装置,当某个服务单元发生故障后,通过断路器的故障监控(类似熔断保险丝), 向调用方返回一个符合预期的 , 可处理的备选响应(FallBack),
  而不是长时间的等待或者抛出调用方无法处理的异常;
  这样就保证了服务调用方的线程不会被长时间,不必要地占用, 从而避免了故障在分布式系统中的蔓延,乃至雪崩;
  
  fallback  : 服务降级      --服务器忙, 请稍后再试, 不让客户端等待并立即返回一个友好提示 fallback;
                            --哪些情况会触发降级:  程序运行异常, 超时, 服务熔断触发服务降级, 线程池/信号量 打满也会导致服务降级;
                            
  break      : 服务熔断      --类比保险丝达到最大服务访问后,直接拒绝访问,拉闸限电,然后调用附文件及的方法并返回友好提示;
                            --就是保险丝  服务的降级-> 进而熔断 -> 恢复调用链路
                            熔断机制:
                            熔断机制是应对雪崩效应的一种微服务链路保护机制,当扇出链路的某个微服务出错不可用或者响应时间太长时,
                            会进行服务的降级,进而熔断该节点的微服务调用,快速返回错误的响应信息;
                            当检测到该节点微服务调用响应正常后,恢复调用链路;
                            在springcloud框架里,熔断机制通过Hystrix实现, Hystrix 会监控微服务间调用的状况, 
                            当失败的调用到一定的阈值,缺省是5秒内20次调用失败,就会启动熔断机制,熔断机制的注解是@HystrixCommand;
                             开 - 半开 - 关 :
                             失败率到达设定比例   -> 开;
                             周期时间内尝试调用   -> 半开;
                             服务恢复            -> 关;
                            
  flowlimit  : 服务限流      -- 秒杀 高并发等操作, 严禁一窝蜂的过来拥挤, 大家排队,一秒钟N个,有序进行;
  
  jmeter 压测 cloud-provider-hystrix-payment8001 controller timeout 接口 导致其他(OK) 接口变慢:
  tomcat的默认的工作线程数被打满了, 没有多余的线程来分解压力和处理;
  
  服务超时: 超时不再等待;
  服务宕机: 出错要有兜底;
  1.服务端先找服务自身问题: 设置自身调用超时时间的峰值,峰值内可以正常运行;
                         超过了时间峰值 需要有兜底的方法处理, 作为服务降级fallback;
                         业务类:服务方法使用 @HystrixCommand 报异常后处理- 一旦调用服务方法失败并抛出错误信息后,会自动调用@HystrixCommand 标注好的fallbackMethod调用类中的指定方法;
                         主启动: @EnableCircuitBreaker 注解 开启@HystrixCommand 的使用
                    
  2.客户端:               配置方式和服务端一样;
  
  服务降级问题: 每个方法配置一个降级方法 - 方法膨胀 , 和业务逻辑混在一起, 代码混乱;
  解决:         1.  增加通用 和 独享的 fallback  各自分开 避免膨胀,合理减少了代码量;
                   @DefaultProperties(defaultFallback=""") + @HystrixCommend
                2. 开启feign 的 Hystrix  新建类 实现 FeignClient ,为每个接口进行降级;
                    @FeignClient(value = "cloud-provider-hystrix-payment",fallback = PaymentFallbackServiceImpl.class)
                    public interface PaymentHystrixService {}
                    @Component
                    public class PaymentFallbackServiceImpl implements PaymentHystrixService {}
  熔断:         1. @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback" ,commandProperties = {
                           @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),//是否开启断路器
                           @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), // 请求次数 - 请求总数阀值
                           @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), // 时间范围 - 快照时间窗
                           @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), // 失败率达到多少时断路 - 错误百分比阀值
                   })
                 熔断打开: 请求不再近些调用当前服务,内部设置时钟一般为MTTR(平均故障处理时间), 当打开时长达到所设时钟则进入半熔断;
                 熔断关闭: 熔断关闭不会对服务进行熔断;
                 熔断半开: 部分请求根据规则调用当前服务,如果请求成功且符合规则 则认为当前服务恢复正常,关闭熔断;
                 断路器器作用情况:
                    涉及到断路器的三个重要参数: 快照时间窗,请求总数阀值,错误百分比阀值;
                    1.快照时间窗: 断路器确定是否打开需要统计一些请求和错误数据,而统计的时间范围就是快照时间窗,默认为最近的10s;
                    2.请求总数阀值: 在快照时间窗内,必须满足请求总数阀值才有资格熔断.默认为20,意味着在10秒内,如果该Hystrix 命令的调用次数不足20次,即使所有的
                                   请求都超时或其他原因失败,断路器都不会打开;
                    3.错误百分比阀值: 当请求总数在快照时间窗口内超过了总数阀值,比如发生了30次调用,如果在这30次调用中,有15次发生了超时异常,也就是超过了50%
                                    的错误百分比,在默认设定50%阀值情况下,这时候就会将断路器打开;
                 断路器开启或关闭条件: 
                     1. 当满足一定的阀值的时候(默认10秒内超过20个)
                     2. 当失败率达到一定的时候(默认10秒内超过50%)
                     3. 达到以上阀值,断路器将开启;
                     4. 当开启时,所有请求都不会进行转发;
                     5. 一段时间后(默认是5秒), 这个时候 断路器是半开状态,会让其中一个请求转发.如果成功,断路器会关闭,若失败,继续开启.重复4和5;      
                 断路器打开之后:
                     1.再有请求调用的时候,将不会调用主逻辑,而是直接调用降级fallback,通过熔断.实现了自动地发现错误并将降级逻辑切换为主逻辑,减少响应延迟的效果;
                     2.原来的主逻辑如何恢复: Hystrix 实现了自动恢复功能;
                           当断路器打开后,对主逻辑进行熔断后,hystrix 会启动一个休眠时间窗, 这个时间窗内,降级逻辑是临时的成为主逻辑,
                           当休眠时间窗到期,断路器将进入半开状态,释放一次请求到原来的主逻辑上,如果此次请求正常返回,那么断路器将继续闭合;
                           主逻辑恢复,如果此次请求依然有问题,断路器继续进入打开状态,休眠时间窗重新计时;
                 All配置:
                 @HystrixCommand(fallbackMethod = "str_fallbackMethod",groupKey = "strGroupCommand", commandKey = "strCommand", threadPoolKey = "strThreadPool",
                  commandProperties = {
                      // 设置隔离策略, THREAD 表示线程池, SEMAPHORE: 信号隔离池
                      @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                      // 当隔离策略选择信号池隔离时, 用来设置信号池的大小,(最大并发数)
                      @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10"),
                      // 配置命令执行的超时时间
                      @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10"),
                       // 是否启用 超时时间
                      @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
                      //  执行超时时是否中断
                      @HystrixProperty(name = "execution.isolation.thread.interrupOnTimeout", value = "true"),
                      //  执行被取消时 是否中断
                      @HystrixProperty(name = "execution.isolation.thread.interrupOnCancel", value = "true"),
                       //  允许回调方法执行的最大并发数
                      @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "10"),  
                      //  服务降级是否启用,是否执行回调函数
                      @HystrixProperty(name = "fallback.enable", value = "true"),  
                      //  是否启用断路器
                      @HystrixProperty(name = "circuitBreaker.enable", value = "true"), 
                      //  请求次数 - 请求总数阀值
                      @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                      //  失败率达到多少时断路 - 错误百分比阀值
                      @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                      // 时间范围 - 快照时间窗
                      @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), 
                      // 断路器强制打开
                      @HystrixProperty(name = "circuitBreaker.forceOpen", value = "false"), 
                       // 断路器强制关闭
                      @HystrixProperty(name = "circuitBreaker.forceClosed", value = "false"), 
                       // 滚动时间窗设置,该时间用于断路器判断健康度时需要收集信息的持续时间
                      @HystrixProperty(name = "metrics.rollingState.timeInMilliseconds", value = "10000"),
                       // 该属性用来设置滚动时间窗统计指标信息时划分桶的数量, 断路器在收集指标信息时,会根据
                       // 设置的时间窗长度拆分成多个桶,来累计各度量值, 每个桶记录了一段时间内的采集指标
                       // 比如 10  秒拆分成 10 个桶来收集, 所以 timeInMilliseconds 必须能被 numBuckets 整除 否则会抛出异常
                      @HystrixProperty(name = "metrics.rollingState.numBuckets", value = "10000"), 
                  
                  }
                  
                  )
     服务限流
     
8.Gateway
  SpringCloud Gateway 是spring cloud 的一个全新项目, 基于spring5.0 + springboot 2.0 和 project Reactor 等技术开发的网关,它旨在为微服务架构
  提供一种简单有效的统一的API路由管理方式;
  SpringCloud Gateway 作为 spring cloud 生态系统中的网关, 目标是代替zuul 在springcloud 2.0 以上版本中,没有对新版本的zuul2.0 以上最新高性能版本进行集成,
  仍然还是使用的的 zuul 1.x  非Reactor 模式的老版本,而为了提升网关的性能, springcloud gateway 是基于webflux 框架实现的, 而webflux框架底层则使用了高性能的Reactor模式通信框架Netty;
  SpringCloud Gateway 的目标提供统一的路由方式且基于Filter 链的方式提供了网关基本的功能,例如 安全/监控/指标/限流;
  
  三大核心概念:
        Route(路由): 路由是构建网关的基本模块,它由ID,目标URI,一系列的断言和过滤器组成,如果断言为true则匹配该路由;
        Predicate(断言): 参考的是java8的java.util.function.Predicate
                         开发人员可以匹配HTTP请求中的所有内容,(请求头/请求参数), 如果请求与断言相匹配则进行路由;
        Filter(过滤): 指的是Spring 框架中GatewayFilter的实例, 使用过滤器,可以在请求被路由前或者之后对请求进行修改;
        总体: 路由转发 执行过滤链;
        
   predicate:
      spring cloud gateway 将路由匹配作为spring webflux handlerMapping 基础架构的一部分.
      spring cloud gateway 包括许多内置的route predicate 工厂. 所有这些Predicate 都与HTTP请求的不同属性匹配, 多个Route Predicate 工厂可以进行组合;
      spring cloud gateway 创建route对象时, 使用RoutePredicateFactory 创建Predicate对象,Predicate 对象可以赋值给Route Spring cloud gateway 包含许多内置的 Route Predicate Factories;
      所有这些谓词都匹配HTTP 请求的不同属性, 多种谓词工厂可以组合,并通过逻辑and;
      
      1.After Route Predicate
         在xxx 时间以后: 
         - After=2021-07-12T17:28:57.437+08:00[Asia/Shanghai]
      2.Before Route Predicate
         在xxx 时间之前: 
      3.Between Route Predicate
         在xxx 时间 和 xxx时间 之间: 
      4.Cookie Route Predicate
          - Cookie=username,zzyy  
          curl http://localhost:9527/payment/lb --cookie "username=zzyy"
      5.Header Route Predicate
           - Header=X-Request-Id,\d+
          curl http://localhost:9527/payment/lb -H "X-Request-Id:123"
      6.Host Route Predicate
          - Host=**.somehost.org,**.antherhost.org
           curl http://localhost:9527/payment/lb -H "Host:www.somehost.org"
      7.Method Route Predicate
          - Method=GET
      8.Path Route Predicate
         - Path=/payment/getPaymentById/**
      9.Query Route Predicate
         - Query=username,\d+
        curl http://localhost:9527/payment/lb?username=123
   Predicate 就是为了实现一组匹配规则,让请求过来找到对应的route 进行处理;
   
  GatewayFilter Factories
  路由过滤器可用于修改进入的HTTP 请求和返回 的HTTP响应,路由过滤只能指定路由进行使用;
  Spring cloud Gateway 内置了多种路由过滤器, 他们都由GatewayFilter 的工厂类来产生;
  spring cloud gateway filter:  
                           生命周期:pre  post
                           种类: GatewayFilter / GlobalFilter 
  自定义全局GlobalFilter  implements GlobalFilter,Ordered
  
9.配置中心 configServer 
  Spring cloud config 为微服务架构中的微服务提供集中化的外部配置支持,配置服务器为 各个不同微服务应用 的所有环境提供了一个 中心化的外部配置;
  spring cloud config 分为服务端和客户端两部分;
  服务端也称为 分布式配置中心 它是一个独立的微服务应用, 用来连接配置服务器, 并为客户端提供获取配置信息, 加密/解密信息等访问接口;
  客户端 则是通过指定的配置中心来管理应用资源, 以及与业务相关的配置内容, 并在启动的时候从配置中心获取和加载配置信息;
  配置服务器默认采用 git 来存储配置信息, 这样就有助于对环境配置进行版本管理, 并且可以通过git客户端 工具来方便的管理和访问配置内容;
  
  配置文件获取方式: /{label}/{application}-{profile}.yml    master 分支: http://http://config-3344.com:3344/master/config-dev.yml 
                                                                       http://http://config-3344.com:3344/master/config-test.yml 
                                                          dev    分支: http://http://config-3344.com:3344/dev/config-test.yml 
                                                                       http://http://config-3344.com:3344/dev/config-dev.yml 
                                                                       
                 /{application}-{profile}.yml              master 分支: http://http://config-3344.com:3344/config-dev.yml 
                                                                        http://http://config-3344.com:3344/config-test.yml 
                                                           dev    分支: http://http://config-3344.com:3344/config-test.yml 
                                                                        http://http://config-3344.com:3344/config-dev.yml
                 /{application}/{profile}[/{lable}]        http://http://config-3344.com:3344/config/dev/master  
                                                           http://http://config-3344.com:3344/config/dev/dev              
  application.yml 是用户级的资源配置项 
  bootstrap.yml 是系统级的,优先级更高
  
  spring cloud 会创建一个 " Bootstrap Context",作为spring 应用的 Application Context 的父上下文. 初始化的时候 Bootstrap Context 负责从 外部源 
  加载配置属性并解析配置,这两个上下文共享一个从外部获取的 "Environment";
  Bootstrap 属性有高优先级, 默认情况下, 它们 不会被本地覆盖, Bootstrap context 和 Application Context 有着不同的约定, 所以新增了一个 bootstrap.yml
  文件,保证Bootstrap Context 和 Application Context 配置的分离;
  要将 Client 模块下的application.yml 文件修改为 bootstrap.yml 这是很关键的;
  因为bootstrap.yml 是比 application.yml 先加载的 bootstrap.yml 优先级高于 application.yml;
  
  问题: Linux运维修改GitHub 上的配置文件内容后做调整;
        刷新3344(configServer) 配置中心立即响应;
        刷新3355(configClient) 客户端没有任何响应;
        --3355(客户端) 没有变化 除非自己重启或者重新加载;
  动态刷新:
      手动刷新: @RefreshScope
               public class ConfigClientController {}
               curl -X POST "http://localhost:3355/actuator/refresh"
        
  
10.消息总线
     在微服务交媾的系统中,通常会使用轻量级的消息代理来构建一个公用的消息主题,并让系统中所有微服务实例都连接起来,由于该主题中产生的消息会被所有实例监听和消费, 所以称为消息总线, 
     在总线上的各个实例,都可以方便地广播一些需要让其他连接在该主题上的实例都知道的消息,
     
     基本原理:
        configClient 实例都监听MQ中同一个topic(默认是springCloudBus 交换机) 当服务刷新数据时,它会吧这个消息放到topic 中 这样 其他监听同一 topic 的服务就能得到通知,然后去更新自身的配置;
     分布式自动刷新配置功能;
     
     spring cloud bus 配合 spring cloud config 使用可以实现配置的动态刷新;
     Spring Cloud Bus 是用来将分布式系统的节点 与 轻量级消息系统链接起来的框架 , 它 整合了java 的 事件处理机制 和消息中间件的功能;
     Bus 支持两种消息代理: RabbitMQ 和 kafka
     
     通知方式: 客户端/bus/refresh, 刷新所有客户端配置;
              服务端ConfigServer 的 /bus/refresh 端点, 而刷新所有客户端配置; 推荐使用
     
     广播通知: 配置客户端 configCliet 和服务端 ConfigServer 配置 amqp 支持 
              向配置服务端 发起 bus-refresh 请求,刷新所有客户端配置;
              curl -X POST "http://localhost:3344/actuator/bus-refresh"
     定点通知: 不想全部通知,只想定点通知
              指定具体某一个实例生效而不是全部
              curl -X POST "http://ip(configServer):端口(configServer)/actuator/bus-refresh/{destination}"
              curl -X POST "http://localhost:3344/actuator/bus-refresh/config-client:3355"
              /bus/refresh请求不再发送到具体的服务实例上,而是发给config server 并通过destination参数类指定需要跟新配置的服务或实例
              
11.Spring Cloud Stream
   屏蔽底层消息中间件的差异,降低切换成本,统一消息的编程模型;
   是一个构建消息驱动微服务的框架;
   应用程序通过inputs 或者 outputs 来与spring cloud stream 中的binder 对象交互;
   通过我们配置来binding(绑定), 而Spring Cloud Stream 的 binder 对象负责与消息中间件交互;
   所以,我们只需要搞清楚如何与Spring Cloud Stream 交互就可以方便使用消息驱动的方式;
   
   通过使用 Spring Integration 来连接消息代理中间件以实现消息时间驱动
   Spring Cloud Stream 为一些供应商 的消息中间件产品提供了个性化的自动化配置实现, 引用了发布-订阅.消费组,分区的三个核心概念;
   目前仅支持 rabbitMQ 和 Kafka;
   
   在没有绑定器这个概念的情况下,我们的springboot 应用要直接与消息中间件进行信息交互的时候, 由于各个消息中间构建的初衷不同;
   他们的实现细节上会有较大的差异性;
   通过定义绑定器作为中间层,完美地实现了 应用程序与消息中间件细节之间的隔离;
   通过向应用程序暴露统一的channel 通道, 使得应用程序不需要再考虑各种不同的消息中间件实现;
   
   通过定义绑定器 Binder 作为中间层, 实现了应用程序与消息中间件细节之间的隔离;
   
   Stream 对消息中间件的进一步封装,可以做到代码层面对中间件的无感知, 甚至于动态的切换中间件(rabbitmq 切换为 kafka) ,
   使得微服务开发的 高度解耦, 服务可以关注更多自己的业务流程;
   Stream 中的消息通信方式 遵循了发布-订阅 模式;
   标准流程: 
          Binder: 连接中间件,屏蔽差异;
          Channel: 通道,是队列Queue  的一种抽象, 在消息通讯系统中就是实现存储和转发的媒介,通过channel对队列进行配置;
          Source 和 Sink : 简单的可理解为参照对象时Spring Cloud Stream 自身, 从Stream 发布消息就是 输出, 接收消息就是输入;
          
          @Input 注解标识输入通道, 通过该输入通道接收到的消息进入应用程序;
          @Output 注解标识输出通道, 发布的消息将通过该通道离开应用程序;
          @StreamListener 监听队列,用于消费者的队列的消息接收;
          @EnableBinding 指信道 channel 和 exchang 绑定在一起;
   场景: 订单系统做集群部署,都会从rabbitMQ中获取订单信息,
         如果一个订单同时被两个服务获取到, 那么就会造成数据错误, 我们要避免这种情况;
         用stream中的消息分组来解决;
         在Stream 中处于同一个group中的多个消费者是竞争关系, 就能够保证消息只会被其中一个应用消费一次, 不同组是可以全面消费的(重复消费)
   重复消费: 
         默认分组group 是不同的,组流水号不一样 被认为不同组,可以消费;
         自定义配置分组,将相同类型的消费者划分为同一组,形成竞争关系,只有一个消费者可以进行消费,解决重复消费问题;
         
   持久化: 配置分组后 可以消费宕机后未进行消费的服务端发送到mq的消息, 不会造成消息丢失, 未配置分组 则会造成消息丢失;
   
12. Spring cloud Sleuth 分布式请求链路跟踪
    在微服务框架中,一个由客户端发起的请求, 在后端系统中会经过多个不同的服务节点调用,来协同产生最后的请求结果,每一个前段请求都会形成一条复杂的分布式服务调用链路;
    链路中的任何一环出现高延时或错误都会引起整个请求最后的失败;
    Spring cloud Sleuth 提供了一套完整的服务跟踪的解决方案,在分布式系统中提供追踪解决方案并且兼容支持了zipkin;
    
    表示一个请求链路,一条链路通过Trace Id 唯一标识, Span 标识 发起的请求信息,各span 通过parent Id 关联起来;
                                                                             service4
                                                                             Trace Id = x                                     
     service1                 service2              service3          ->    span id = D    
     Trace Id = x            Trace Id = x          Trace Id = x              parent id = C
     span id = A        ->   span id = B       ->  span id = C  
     parent Id = null        parent Id = A         parent Id = B
                                                                             service 5
                                                                             Trace Id = x    
                                                                        ->   span id = E           
                                                                             parent id = c
                                                                             
13.spring cloud Alibaba
  spring cloud Alibaba , 它是由一些 阿里巴巴 的开源组件, 和云产品组成的, 这个项目的目的是为了让大家所熟知的spring 框架, 其优秀的设计模式,和抽象理念,以给使用阿里巴巴 产品的java开发者
  带来使用spring boot 和spring cloud 的更多便利;
  1.服务的限流降级: 默认支持 servlet, Feign, RestTemplate , Dubbo 和 RocketMQ  限流降级的接入, 可以在运行时 通过 控制台 实时修改 限流降级 规则, 还支持 查看限流降级 metrics 控制;
  2.服务注册与发现: 适配 sprig cloud 服务注册与发现标准, 默认集成了 Ribbon 的支持;
  3.分布式配置管理: 支持分布式系统的 外部化配置 , 配置更改时 自动化刷新;
  4.消息驱动能力: 基于 spring cloud stream 为微服务 应用构建消息驱动能力;
  5.阿里云对象存储: 阿里云提供的海量,安全,低成本,高可靠的云存储服务. 支持在任何应用, 任何时间, 任何地点 存储和访问 任意类型的数据;
  6.分布式任务调度: 提供秒级,精准,高可靠,高可用的 定时(基于 cron 表达式) 任务调度服务, 同事提供分布式的任务执行模型, 如网格任务, 网格任务支持 海量子任务 均匀分配到 所有worker( schedulerx-client ) 上执行;
14. spring cloud Alibaba Nacos 服务注册和配置中心
  Nacos 前四个字母分别为 Naming 和 Configuration 的前两个字母, 最后的s 为Service;
        一个更易于构建云原生应用的动态服务发现,配置管理,和服务管理平台;
        Nacos: Dynamic Naming and Configuration Service;
        Nacos 就是注册中心 + 配置中心的组合;
        替代 eureka 作为注册中心, 替代Config 作为配置中心;
        https://github.com.alibaba/Nacos
        官方文档: https://nacos.io/zh-cn/index.html
                 https://spring-cloud-alibaba-group.github.io/github-pages/greenwich/spring-cloud-alibaba.html#_spring_cloud_alibaba_nacos_discovery;
  Nacos 支持 AP 和 CP 模式的切换
        C 是所有节点在同一时间看到的数据是一致的; 而A的定义是所有的请求都会收到响应;
  何时选择何种模式: 
        一般来说如果不需要存储服务级别的信息且服务实例是通过nacos-client 注册,并能够保持心跳上报,name就可以选择AP模式, 当前主流的服务 如spring cloud 和 Dubbo 服务,都适用于
        AP模式, AP模式为了服务的可能性而减弱一致性,因此AP模式下只支持注册临时实例;
        如果需要在服务级别编辑或者存储配置信息,那么CP是必须的, K8S服务 和DNS 服务则适用于CP模式,.
        CP模式下则支持注册持久化实例, 此时则是以Raft协议为集群运行模式, 该模式下注册实例之前必须先注册服务,如果服务不存在,则会返回错误;
        
        curl -X PUT '$NACOS_SERVER:8848/nacos/v1/ns/operator/swithces?entry=serverMode&value=CP'
        
  Nacos 配置中心
  https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html
        之所以要配置 spring.application.name, 是因为它是构成Nacos 配置管理 dataId 字段的一部分;
        在Nacos Spring Cloud 中 dataId 的完整格式如下:
        ${prefix}-${spring.profile.active}.${file-extension}
        prefix 默认为 spring.application.name 的值, 也可以通过配置项, spring.cloud.nacos.config.prefix 来配置;
        spring.profile.active 即为当前环境对应的profile, 详情可以参考spring boot 文档; 注意当 spring.profile.active 为空时,对应的连接符 - 也将不存在,dataId的拼接方式变成 
           ${prefix}.${file-extension}
        file-extension 为配置内容的数据格式, 可以通过配置项 spring.cloud.nacos.config.file-extension 来配置. 目前只支持 properties 和yaml 类型;
        通过 spring Cloud 原生注解 @RefreshScope 实现自动更新;
        最后公式 : yaml
            ${spring.application.name}-${spring.profile.active}-${spring.cloud.nacos.config.file-extension}
        
  nacos 配置: namespace + group + dataId
        类似java 里面的package 名和 类名 最外层的namespace 是可以用于区分部署环境的, group 和dataId 逻辑上区分两个目标对象;  
        默认情况: 
           Namespace = public ,Group = DEFAULT_GROUP, 默认 cluster 是 DEFAULT;
        nacos 默认的命名空间是public ,NameSpace 主要用来时间隔离.
        比方说我们现在有三个环境: 开发 ,测试 生产环境,我们可以创建三个Namespace 不同的Namespace 之间是隔离的;
        Group 默认 是DEFAULT_GROUP ,Group 可以把不同的微服务划分到同一个分组里面去;
        Service 就是微服务, 一个service 可以包含多个 Cluster(集群) , Nacos 默认Cluster 是 DEFAULT , Cluster 是对指定微服务的一个虚拟划分, 
        比方说为了容灾, 将Service 微服务分别部署在了杭州机房 和 广州机房
        这时就可以给杭州机房 的Service 微服务起一个集群名称(HZ),
        给广州机房的Service 微服务起一个集群名称(GZ), 还可以尽量让同一个机房的微服务互相调用,以提升性能.
        最后是Instance,就是微服务的实例;
        
        DataID 方案: 指定spring.profile.active 和配置文件的 DataID 来使不同环境下读取不同的配置;
                     默认空间 + 默认分组 + 新建dev 和 test 两个DataID
                     通过spring.profile.active 属性就能进行多环境下配置文件的读取;
  Nacos 集群和持久化配置
        http://nacos.io/zh-cn/docs/cluster-mode-quick-start.html
        默认nacos 使用嵌入式数据库(derby) 实现的存储, 所以, 如果启动有多个默认配置下的 Nacos 节点, 数据存储 是存在一致性问题的.
        为了解决这个问题, Nacos 采用了 集中式存储的方式来支持集群化部署, 目前支持Mysql 的存储;
        Nacos 支持的三种部署模式:
                       单机模式: - 用于测试和单机使用;
                       集群模式: - 用于生产环境, 确保高可用;
                       多集群模式: - 用于多数据中心场景;
        1 个nginx + 3个Nacos + 1个mysql
        https://github.com/alibaba/nacos/releases/tag/1.1.4
        nacos :
        application.properties   使用外接数据库
        spring.datasource.platform=mysql
        db.num=1
        db.url.0=jdbc:mysql:172.17.0.3:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
        db.user=root
        db.password=1234Qwer
        
        cluster.conf 集群配置
           192.168.2.20:3333
           192.168.2.20:4444
           192.168.2.20:5555
        startup.sh  启动文件  增加 分端口启动 -p
        while getopts ":m:f:s:p:" opt
        do
            case $opt in
                m)
                    MODE=$OPTARG;;
                f)
                    FUNCTION_MODE=$OPTARG;;
                s)
                    SERVER=$OPTARG;;
                p)
                    PORT=$OPTARG;;
                ?)
                echo "Unknown parameter"
                exit 1;;
            esac
        done
        ...
        nohup $JAVA -Dserver.port=${PORT} ${JAVA_OPT} nacos.nacos >> ${BASE_DIR}/logs/start.out 2>&1 &
        Nginx:
        upstream cluster{
             server 127.0.0.1:3333; 
             server 127.0.0.1:4444; 
             server 127.0.0.1:5555;      
        
            }
        
            server {
                listen       1111;
                server_name  localhost;
        
                #charset koi8-r;
        
                #access_log  logs/host.access.log  main;
        
                location / {
                   # root   html;
                   # index  index.html index.htm;
                    proxy_pass http://cluster;
                }
            }
15.Spring Cloud Alibaba Sentinel 实现熔断与限流
         官网: https://github.com/alibaba/Sentinel
         面向云原生微服务的流量控制/熔断/降级组件
         1.单独一个组件,可以独立出来;
         2.直接界面化的细粒度统一配置;
         
         Sentinel 具有以下特征:
         
         丰富的应用场景：Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景，例如秒杀（即突发流量控制在系统容量可以承受的范围）、消息削峰填谷、集群流量控制、实时熔断下游不可用应用等。
         完备的实时监控：Sentinel 同时提供实时的监控功能。您可以在控制台中看到接入应用的单台机器秒级数据，甚至 500 台以下规模的集群的汇总运行情况。
         广泛的开源生态：Sentinel 提供开箱即用的与其它开源框架/库的整合模块，例如与 Spring Cloud、Dubbo、gRPC 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。
         完善的 SPI 扩展点：Sentinel 提供简单易用、完善的 SPI 扩展接口。您可以通过实现扩展接口来快速地定制逻辑。例如定制规则管理、适配动态数据源等。
       
     Sentinel 分为两个部分:
          核心库: (java客户端) 不依赖任何框架/库,能够运行于所有java运行时环境,同时对 Dubbo/ Spring Cloud 等框架也有较好的支持;    
          控制台: (Dashboard) 基于Spring Boot 开发 打包后可以直接运行, 不需要额外的Tomcat 等应用容器;
      Sentinel: 为懒加载机制, 执行一次访问后可查看对应的 注册进来的服务;
      流控规则: 
          基本介绍:
              资源名称: 唯一名称,默认请求路径;
              针对来源: Sentinel 可以针对调用者限流,填写微服务名称, 默认default (不区分来源)
              阈值类型/单机阈值: -QPS(每秒钟的请求数量): 当调用该api 的QPS时 达到阈值的时候进行限流;
                               -线程数: 当调用该api的线程数量达到阈值时, 进行限流;
              是否集群: 不需要集群;
              流控模式: -直接: api 达到限流条件时, 直接限流;
                       -关联: 当关联的组员达到阈值时,就限流自己; (当与A关联的资源B达到阈值后,就限流自己, B惹事,A 担责任  -- 订单服务  支付服务, 支付服务压力大 限流订单服务) 
                       -链路: 只记录指定链路上的流量( 指定资源 从入口资源进来的流量,如果达到阈值,就进行限流[api级别的针对来源]
         
               流控效果: -快速失败: 直接失败,异常;
                        -warm up: 根据codeFactor (冷加载因子,默认3) 的值, 从阈值/codeFactor,经过预热时长,才达到设置的QPS阈值;
                                  公式:阈值除以coldFactor,经过预热时长后才会达到阀值;
                                  WarmUpController
                        -排队等待: 匀速排队, 让请求以匀速的速度通过, 阈值类型必须设置为QPS,否则无效;
      降级规则:  
             RT(平均响应时间,秒级):
               -平均响应时间,超出阈值 且 在时间窗口内通过的请求>=5, 两个条件同时满足后触发降级;
               -窗口期过后关闭断路器;
               -RT 最大4900ms(更大的需要通过-Dcsp.sentinel.statistic.max.rt=xxxx 才能生效)
             异常比例(秒级):
               QPS >= 5 且异常比例(秒级统计) 超过阈值时, 触发降级; 时间窗口结束后,关闭降级;
             异常数(分钟级):
               异常数(分钟统计) 超过阈值时, 触发降级; 时间窗口结束后,关闭降级;若timewindow 小于60s 则结束熔断状态后仍可能再进入熔断状态;
             
      Sentinel 熔断降级会在调用链路中 某个资源出现不稳定状态时, ( 例如调用超时, 或异常比例升高),对这个资源的调用进行限制, 让请求快速失败, 避免影响到其它资源而导致级联错误;
      当资源被降级后, 在接下来的降级时间窗口内,对该资源的调用都自动熔断(默认行为是抛出 DegradeException)
      Sentinel 的断路器是没有半开状态的; 半开- Hystrix ;
      热点规则: 热点参数限流
             热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的 Top K 数据，并对其访问进行限制。比如：
             商品 ID 为参数，统计一段时间内最常购买的商品 ID 并进行限制
             用户 ID 为参数，针对一段时间内频繁访问的用户 ID 进行限制
             热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。  
             @SentinelResource(value = "testHotKey",blockHandler = "deal_testHotKey") value 资源原名称, blockHandler 自定义兜底方法;
             参数例外项:
                   配置参数值的例外项 , 参数值为设置的参数值- 遵循新的热点规则; 热点参数注意项, 参数必须是基本类型或者String;
      @SentinelResource : 处理的是Sentinel 控制台 配置的违规情况, 有blockHander方法配置的兜底处理;
      RuntimeException : java 运行时异常,   @SentinelResource 不管 RuntimeException;
        -- @SentinelResource   主管配置 sentinel 配置出错,运行出错该走异常走异常;
      系统规则: 
            Sentinel 系统自适应限流从整体维度对应用入口流量进行控制，结合应用的 Load、CPU 使用率、总体平均 RT、入口 QPS 和并发线程数等几个维度的监控指标，
            通过自适应的流控策略，让系统的入口流量和系统的负载达到一个平衡，让系统尽可能跑在最大吞吐量的同时保证系统整体的稳定性。
            系统规则支持以下的模式：
            Load 自适应（仅对 Linux/Unix-like 机器生效）：系统的 load1 作为启发指标，进行自适应系统保护。当系统 load1 超过设定的启发值，
                 且系统当前的并发线程数超过估算的系统容量时才会触发系统保护（BBR 阶段）。系统容量由系统的 maxQps * minRt 估算得出。设定参考值一般是 CPU cores * 2.5。
            CPU usage（1.5.0+ 版本）：当系统 CPU 使用率超过阈值即触发系统保护（取值范围 0.0-1.0），比较灵敏。
            平均 RT：当单台机器上所有入口流量的平均 RT 达到阈值即触发系统保护，单位是毫秒。
            并发线程数：当单台机器上所有入口流量的并发线程数达到阈值即触发系统保护。
            入口 QPS：当单台机器上所有入口流量的 QPS 达到阈值即触发系统保护。
    @SentinelResource:
            1.按资源名称限流 + 后续处理:
                             @GetMapping(value = "/byResource")
                             @SentinelResource(value = "byResource",blockHandler = "handlerException")
                             返回自定义的处理结果
                             问题: 关闭服务. 流控规则消失 临时配置;
            2.按url地址限流 + 后续处理:
                              @GetMapping(value = "/rateLimit/byUrl")
                              @SentinelResource(value = "byUrl")
                             返回sentinel自带的处理结果 Blocked by Sentinel (flow limiting)
                             
            3.上面方案面临的问题:
                 - 系统默认的,没有体现我们自己的业务要求;
                 - 依照现有条件, 我们自定义的处理方法又和业务逻辑代码耦合在一块,不直观;
                 - 每个业务添加一个兜底方法,代码膨胀;
                 - 全局统一的处理方法没有体现;
            4.客户自定义限流处理逻辑:
                  创建 CustomerBlockHandler 类用于自定义限流处理逻辑;
                   @GetMapping(value = "/rateLimit/customerBlockHandler2")
                      @SentinelResource(value = "customerBlockHandler2",
                              blockHandlerClass = CustomerBlockHandler.class,
                              blockHandler = "handlerException2")
                     public CommonResult customerBlockHandler2(){  }        
                  
                 ------------------------------------------------------------- 
                  
                  public class CustomerBlockHandler {
                  
                      public static CommonResult handlerException(BlockException exception){
                          return  new CommonResult(444,"按客户自定义,global handlerException-------1");
                      }
                      public static  CommonResult handlerException2(BlockException exception){
                          return  new CommonResult(444,"按客户自定义,global handlerException-------2");
                      }
            5.更多注解属性说明:
                      Sentinel 三个核心api : SphU 定义资源
                                            Tracer 定义统计
                                            ContextUtil 定义上下文
            
       -- fallback 和  blockHandler
           @SentinelResource(value = "fallback",fallback = "handlerFallback",blockHandler = "blockHandler") // 配置blockHandler,配置fallback
           public CommonResult<Payment> fallback(@PathVariable("id") Long id) {}
           //falback 示例
           public CommonResult handlerFallback(@PathVariable("id") Long id,Throwable e) {}
            //blockHandler 示例
           public CommonResult blockHandler(@PathVariable("id") Long id, BlockException e) {}
           只配置 fallback 或只配置 blockHandler  则只有配置的生效;
           若blockHandler 和 fallback 都进行了配置, 则被限流降级而抛出 BlockException 时 只会进入bolckerHandler处理;
           
       -- 忽略属性:
          @SentinelResource(value = "fallback",fallback = "handlerFallback",blockHandler = "blockHandler",exceptionsToIgnore = {IllegalArgumentException.class})
    -- sentinel 配置持久化
       1. 服务重启后 配置规则会消失;
       策略1:将限流配置规则 持久化 进Nacos 保存, 重要刷新8401 某个rest 地址, sentinel 控制台 的流控规则就能够看到,
             只要nacos 里面的配置不删除, 针对8401 上sentinel 上的流控规则持续有效;
          --example [
                       {
                           "resource":"/rateLimit/byUrl",
                           "limiitApp":"default",
                           "grade":1,
                           "count":1,
                           "starategy":0,
                           "controlBehavior":0,
                           "clustarMode":false
           
                       }
                    ]
           "resource":资源名称,
           "limiitApp":来源应用,
           "grade":阈值类型, 0 表示线程数 1 表示QPS,
           "count":单机阈值,
           "starategy":流控模式 0-直接, 1-关联, 2-链路,
           "controlBehavior":流控效果 0-快速失败, 1-表示warm-up,2,排队等待;
           "clustarMode":是否集群
           
           重启 8401 服务后 调用 配置了流控的 路径 sentinel 配置会自动出现
           
16: Spring cloud Alibaba Seata 处理分布式事务
    1.一次业务操作 需要跨多个数据源或需要跨多个系统进行远程调用,就会产生分布式事务问题;
      单体应用 被拆分成微服务应用, 原来的 三个模块被拆分成三个独立的应用, 分别使用三个独立的数据源, 业务操作 需要 调用三个服务来完成.
      此时 每个服务内部的数据一致性由本地事务来保证, 但是全局的数据一致性问题没有办法保证;
 Seata: 是一款开源的分布式事务解决方案,致力于在微服务架构下提供高性能和简单易用的分布式事务服务;
    一个典型的分布式事务过程: 
                     分布式事务处理过程--- ID + 三组件模型:
                                               Transactin ID XID : 全局唯一的事务ID;
                                               三组件概念:
                                                      Transaction Coordinator (TC) : 事务协调器 - 维护全局和分支事务的状态, 驱动全局事务提交或回滚;
                                                      Transaction Manager (TM) :  事务管理器 - 定义全局事务的范围, 开始全局事务,提交或回滚全局事务;
                                                      Resource Manager (RM) :资源管理器 - 管理分支事务处理的资源,与TC交谈以注册分支事务 和 报告分支事务的状态,并驱动分支事务提交或回滚;
    处理过程: 1.TM 向 TC 申请开启一个全局事务, 全局事务创建成功,并生成一个全局唯一的XID;
             2.XID 在微服务 调用链路的上下文中传播;
             3.RM 向TC 注册分支事务, 将其纳入 XID 对应全局事务的管辖;
             4,TM  向 TC 发起 针对 XID 的全局 提交 或者 回滚决议;
             5.TC 调度 XID 下管辖 的全部分支 事务完成提交 或 回滚请求;
             
             
  下载: https://github.com/seata/seata/releases 
  使用: 首先修改 file.conf --  D:\mysoft\seata-server-1.4.1\seata\conf\file.conf
          1.备份原始file.conf 文件;
          2.主要修改 -  自定义事务组名称, 事务组日志存储模式为db  , 数据库连接信息
          file.conf :
                   -service 模块
                           service {
                              vgroup_mapping.my_test_tx_group = "fsp_tx_group"
                             }
                   -store 模块 
                          store {
                            ## store mode: file、db
                            mode = "db" 
                          
                            ## file store property
                            file {
                              ## store location dir
                              dir = "sessionStore"
                            }
                          
                            ## database store property
                            db {
                              ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp) etc.
                              datasource = "dbcp"
                              ## mysql/oracle/h2/oceanbase etc.
                              db-type = "mysql"
                              driver-class-name = "com.mysql.jdbc.Driver"
                              url = "jdbc:mysql://192.168.2.20:3306/seata"
                              user = "root"
                              password = "1234Qwer"
                            }
                          }
          registry.conf:
                  registry {
                    # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
                    type = "nacos"
                  
                    nacos {
                      serverAddr = "192.168.2.20:8848"
                      namespace = ""
                      cluster = "default"
                    }
                   }
业务说明:
       当用户下单时, 会在订单服务中创建一个订单,然后通过远程调用库存服务来扣减下单商品的库存, 再通过远程调用账户服务来扣减用户账户里面的余额, 最后在订单服务中 修改订单状态为已完成;
       跨越三个数据库,及两次远程调用, 很明显会有分布式事务问题;
       下订单--> 扣库存-->减账户(余额)-->改订单状态;
       订单服务: seate-order-service
       库存服务: seate-storage-service
       账户服务: seate-account-service
 Simple Extensible Autonomous Transaction Architecture 简单可扩展自治事务框架 
       TC seata 服务器; 事务的管理端;
       TM @GlobalTransaction 标有全局事务注解的方法; 事务的发起方;
       RM 事务的参与方; 
       全局事务执行流程:
        1.TM  开启分布式事务(TM 向 TC 注册全局事务记录);
        2.按业务场景,编排数据库,服务等事务内部资源( RM 向 TC 汇报资源准备状态);
        3.TM 结束分布式事务, 事务一阶段结束(TM 通知 TC 提交/回滚 分布式事务);
        4.TC 汇总事务信息, 决定分布式事务是提交还是回滚;
        5.TC 通知所有 RM 提交/回滚 资源,事务二阶段结束;
        
        AT / TCC / SAGA / XA 模式:
        
        AT 模式:
           一阶段加载: seata 会拦截 "业务sql"
           -1. 解析sql 语义 找到"业务sql" 要更新的业务数据, 在业务数据被更新前,将其保存成"before image";
           -2. 执行 "业务sql" 更新业务数据, 
           -3. 在业务数据更新之后 将其保存为 after image; 最后生成行锁;
           以上操作 全部在一个数据库事务内完成, 这样保证了一阶段操作的原子性;
           二阶段提交: 
               二阶段如是顺利提交的话, 因为"业务sql"在一阶段已经提交至数据库, 所以Seata 框架 
               只需要将一阶段保存的快照数据和行锁删掉,完成数据清理即可;
           二阶段回滚:
               二阶段如果回滚, Seata就需要回滚一阶段已经执行的"业务sql",还原业务员数据;
               回滚方式便是使用"before image" 还原业务数据; 但在还原前要首先校验脏写, 对比 "数据库当前业务数据" 和 "after image",
               如果两份数据完全一致,就说明没有脏写, 可以还原业务数据, 如果不一致就说明有脏写, 出现脏写 就需要转人工处理;
17.主键生成服务: 集群高并发情况下如何保证分布式唯一全局id;
     硬性要求:
           - 全局唯一
           - 趋势递增 B数保存索引,提高插入性能;
           - 单调递增
           - 信息安全
           - 含时间戳      
     可用性要求:
           - 高可用
           - 低延迟
           - 高QPS
   一般通用方案: 
             - UUID  (universally unique identifier) 的标准形式 包含 32 个 16 进制数字, 以连字号 分为五段, 形式为8-4-4-4-12的36个字符;
                     性能非常高,本地生成,没有网络消耗;
                     入数据库的性能较差:
                              1.无序, 无法预测他的生成顺序,不能生成递增有序数字;
                              2.主键, UUID 作为主键时在特定环境会存在一些问题;
                                    比如做DB主键的场景下, UUID 就非常不适合,MySql官方有明确的建议主键要尽量越短越好,36个字符长度的UUID不符合要求;
                              3.索引,B+数索引的分裂
                                    既然分布式id是主键, 然后主键包含索引,然后mysql的索引是通过B+树来实现的,每一次新的UUID数据的插入, 为了查询的优化,都会对索引
                                    底层的B+树进行修改,因为UUID数据是无序的, 所以每一次UUID数据 的插入都会对主键生成的B+树进行很大的修改,这一点很不好.
                                    插入完全无序,不但会导致一些中间节点产生分裂,也会白白创造出很多不饱和的节点, 这样大大降低了数据库插入的性能;
                     只符合唯一性;
             - 数据库自增主键: 数据库自增Id和mysql数据库的replace into 实现的;
                            replace into 和 insert 功能类似 不同点在于: replace into 首先尝试插入数据列表中, 如果发现已经有此行数据(根据主键或唯一索引判断)
                            则先删除 再插入,否则直接插入新数据;
                            replace into 的含义 就是插入一条记录, 如果表中唯一索引遇到冲突,则替换老数据;
                      数据库自增id 机制不适合分布式id;
                      - 系统水平扩展比较困难, 比如定义好了步长 和机器台数后,如果要添加机器该怎么做?
                        假设现在有一台机器发号是1,2,3,4,5 (步长是1), 这个时候需要扩容一台机器,可以这样做, 把第二台 机器的初始值 比第一台超过很多,
                        貌似还好,但是如果有多台,系统水平扩展方案复杂难以实现;    
                      - 数据库压力还是很大, 每次获取id 都要读写一次数据库, 非常影响性能, 不符合分布式id里面的低延迟和高QPS规则;  
             - 基于redis 生成全局id策略:   
                      - 因为redis 是单线程的, 天生保证原子性, 可以使用原子操作 INCR 和 INCRBY 来实现;
                      - 在redis集群情况下, 同样和mysql 一样需要设置不同的增长步长,同时 key 一定要设置有效期;
                        可以使用redis 集群来获取更高的吞吐量;
                      - 假如 有5台redis 可以初始化每台为 1 2 3 4 5 然后步长为5;
                        各redis生成的id为:
                        A: 1 6  11 16 21
                        B: 2 7  12 17 22
                        C: 3 8  13 18 23
                        D: 4 9  14 19 24 
                        E: 5 10 15 20 25  
                     维护redis集群麻烦, 费事;
     snowflake:  Twitter 的分布式自增Id 算法 snowflake;
              Twitter 的snowflake解决了这种需求, 最初Twitter 把存储系统从Mysql 迁移到 Cassandra(由FaceBook 开发一套开源分布式NoSQL数据库系统) 因为Cassandra
              没有顺序ID 生成机制, 所以开发这样一套全局唯一ID生成服务;
            Twitter的分布式雪花算法snowFlake, 经过测试 每秒能够产生26万个自增可排序的id;
            1.Twitter 的 SnowFlake 生成 ID 能够按照时间有序生成;
            2.SnowFlake 算法生成的id结果是一个64bit大小的整数, 为一个Long 类型,(转换成字符串后长度最多19;)
            3.分布式系统内不会产生ID 碰撞(由dataCenter 和 workerId作区分)并且效率较高;
          结构:
            - 1bit符号位:      
                      不用, 因为二进制中最高位是符号位,1表示负数,0表示正数.
                      生成的id一般都是用正整数,所以最高位固定为0;
            - 41bit 时间戳位:
                      用来记录时间戳,毫秒级.
                      41位可以表示2^{41}-1个数字;
                      如果只用来表示正整数,可以表示的数值范围是:0 至2^{41}-1,  减一的原因是可表示的数值范围 是从0开始 而不是从1开始;
                      也就是说41位可以表示2^{41}-1 个毫秒值, 转化成单位年则是 69年 (2^{41}-1)/(1000*60*60*24*365);
            - 10bit 工作进程位: 工作机器id, 用来记录工作机器id;
                      可以部署在2^{10} = 1024 个节点,包括 5位datacenter 和5 位 workerId;
                      5位可以表示的最大正整数是 2^{5}-1 = 31, 即可用 0--31 这个32个数字,来表示不同的datacenter 和 workerId;
            - 12bit 序列号位:  序列号 用来记录 同毫秒内产生的不同id;
                    12位 可表示的最大正整数是2^{12}-1 = 4095 ,即可用0--4094 这4095 个数字,来表示同一机器同一时间戳(毫秒)内尝试的4095 个ID 序号; 
          SnowFlake可以保证:
          所有生成的id按时间趋势递增;
          整个分布式系统内不会产生重复id,(因为有datacenter 和 workerId 来区分);
          git地址:https://github.com/twitter-archive/snowflake.git
          优点: 
             毫秒数在高位, 自增序列在低位, 整个id都是趋势递增的;
             不依赖数据库等第三方系统,以服务的方式进行部署,稳定性更高,生成id的性能也是非常高的;
             可以根据自身业务特性分配bit位,非常灵活;
          缺点:
             依赖机器时钟,如果机器时钟回拨,会导致重复id生成;
             在单机上是递增的,但是由于涉及到分布式环境,每台机器上的时钟不可能完全同步,有时候会出现不适全局递增的情况;
             (但是一般的分布式id 只需要趋势递增, 并不是严格递增,90%的需求都只需要趋势递增)             
      补充: 百度开源的 分布式 唯一 id 生成器 UidGenerator
            leaf -- 美团点评分布式id生成系统
         
         