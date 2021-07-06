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
  