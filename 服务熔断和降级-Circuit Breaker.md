---
服务熔断和降级-Circuit Breaker ：Resilience4J
---

## 服务熔断和降级解决了什么问题

#### 服务熔断 (Circuit Breaker)

1. **防止雪崩效应**：
   - 当一个服务出现问题时，如果大量的请求不断重试，可能会导致整个系统出现连锁故障，最终导致系统崩溃。
   - 服务熔断通过快速失败机制，避免了请求堆积，防止了雪崩效应的发生。
2. **提高系统的可用性**：
   - 通过快速失败和有限的降级服务，确保系统的核心功能仍然可用，避免因为部分服务故障而影响整体用户体验。
3. **保护外部资源**：
   - 避免不必要的资源浪费，例如数据库连接、网络带宽等，保护关键资源的稳定性和可用性。
4. **快速恢复**：
   - 在服务恢复时，熔断器会进入半开状态，允许一小部分流量通过以测试服务是否真正恢复，从而快速安全地恢复服务。

#### 服务降级 (Service Degradation)

1. **减轻系统压力**：
   - 当系统负载过高时，服务降级可以暂时牺牲非核心功能，减少系统负担，保证核心功能的正常运行。
2. **提高响应速度**：
   - 通过跳过某些耗时的操作，可以提高系统的响应速度，为用户提供更快的反馈。
3. **避免系统过载**：
   - 通过减少请求处理的复杂度，可以避免系统因过载而导致的完全不可用。
4. **提升用户体验**：
   - 即使在系统部分功能受限的情况下，用户仍然可以使用基本的服务，从而提高用户体验。

## 什么是Circuit Breaker 和 Resilience4J

#### Circuit Breaker

断路器模式是一种在软件设计中用于处理故障和避免级联失败的设计模式。它的灵感来源于电力系统中的断路器，当电路过载时，断路器会打开以防止进一步的损害。在软件中，断路器模式可以保护一个系统免受另一个不稳定或不可用的服务的影响。

断路器的工作原理如下：

1. **Closed（闭合状态）**：这是断路器的默认状态。请求被正常转发到下游服务。
2. **Open（打开状态）**：如果在闭合状态下连续出现多次故障（例如超时、异常），断路器将切换到打开状态。此时，所有请求都会被立即拒绝，而不是发送给下游服务。
3. **Half-Open（半开状态）**：经过一段时间后（称为“冷却时间”），断路器会切换到半开状态，允许少量请求通过以测试下游服务是否已经恢复。如果这些测试请求成功，则断路器返回到闭合状态；如果再次失败，则重新打开并等待下一个冷却周期。

#### Resilience4J

Resilience4J 是一个轻量级的 Java 库，它提供了一组用于实现断路器模式以及其他弹性模式的 API，如重试、降级、超时等。它的目标是简化 Java 微服务中的容错机制，并提高系统的健壮性。

Resilience4J 的主要特点包括：

- **轻量级**：不需要任何运行时依赖，只需要添加相应的 JAR 文件即可使用。
- **易于集成**：支持多种 Java 框架和库，比如 Spring Boot, Micronaut, Quarkus 等。
- **配置灵活**：可以通过代码或配置文件来定制各种策略。
- **API 设计友好**：提供了装饰器模式的实现，使得添加弹性行为变得简单直接。

#### Circuit Breaker 和 Reslience4J 的关系

- **Circuit Breaker** 是一种设计模式，它用于保护服务免受故障或延迟增加的影响。当检测到下游服务出现故障时，断路器会打开，从而阻止更多的请求发送到故障的服务，直到该服务恢复正常。
- **Resilience4J** 是一个具体的实现库，它提供了 Java 开发者可以使用的工具来实施断路器模式和其他容错模式，如重试、超时、降级等。

也可以理解成java中接口和实现类的关系，reslience4j 是 circuit breaker的一个实现。

#### 为什么Resilience4J替代了Hystrix

1. **Hystrix 停止维护**：
   - Hystrix 由 Netflix 开发并在 2011 年发布，它是一个非常强大的容错库，主要用于处理分布式系统的延迟和容错问题。
   - 然而，随着时间的推移，Netflix 宣布 Hystrix 将不再进行积极的开发和维护。这意味着 Hystrix 不再接受新的功能请求或 bug 修复，这可能会影响到它的长期支持和兼容性。
2. **面向 Java 8 和函数式编程**：
   - Resilience4J 专为 Java 8 及其后续版本设计，充分利用了 Java 8 中引入的新特性，如 Lambda 表达式和 Stream API，这使得它非常适合现代 Java 开发环境。
   - Hystrix 虽然也能在 Java 8 上运行，但它的设计并没有特别针对 Java 8 的新特性进行优化。
3. **轻量级和模块化**：
   - Resilience4J 是一个轻量级库，依赖于 Vavr 库来实现其功能。Vavr 是一个现代 Java 库，用于函数式编程，这使得 Resilience4J 的使用更为简洁。
   - Hystrix 依赖于 Archaius，这可能会增加项目的依赖树。此外，Resilience4J 的模块化设计使得用户可以选择性地使用所需的组件，减少了不必要的依赖。
4. **API 设计**：
   - Resilience4J 的 API 设计更加现代化，符合 Java 8 的设计风格，使得使用起来更为直观。
   - Hystrix 的 API 设计虽然强大，但在 Java 8 中使用时可能不如 Resilience4J 那样简洁和自然。
5. **Spring Cloud 的支持**：
   - Spring Cloud 推荐使用 Resilience4J 作为其容错解决方案之一，这为使用 Spring 生态系统的项目提供了一个很好的集成选项。
   - Spring Cloud 对 Resilience4J 的支持意味着在 Spring Boot 和 Spring Cloud 项目中更容易集成和配置。
6. **社区和生态系统**：
   - Resilience4J 是一个活跃维护的项目，并且随着 Java 社区的发展而持续更新。这意味着它能够更好地适应现代 Java 开发的需求。
   - Hystrix 虽然仍被广泛使用，但它的社区活动和贡献已经减少，这可能会影响到未来的兼容性和支持。

## Resilience4J

Resilience4J有几个核心的模块

#### CircuitBreaker（断路器）

断路器通过有限状态机实现，有三个普通状态：关闭、开启、半开，还有两个特殊状态：禁用、强制开启。

断路器使用滑动窗口来存储和统计调用的结果。你可以选择基于调用数量的滑动窗口或者基于时间的滑动窗口。基于访问数量的滑动窗口统计了最近N次调用的返回结果。居于时间的滑动窗口统计了最近N秒的调用返回结果。

**过程**

![断路器过程图](.\image\断路器.drawio.png)

**基于数量的滑动窗口**：

基于访问数量的滑动窗口是通过一个有N个元素的循环数组实现。如果滑动窗口的大小等于10，那么循环数组总是有10个统计值。滑动窗口增量更新总的统计值，随着新的调用结果被记录在环形数组中，总的统计值也随之进行更新。当环形数组满了，时间最久的元素将被驱逐，将从总的统计值中减去该元素的统计值，并该元素所在的桶进行重置。

**基于时间的滑动窗口**:

基于时间的滑动窗口是通过有N个桶的环形数组实现。

如果滑动窗口的大小为10秒，这个环形数组总是有10个桶，每个桶统计了在这一秒发生的所有调用的结果（部分统计结果），数组中的第一个桶存储了当前这一秒内的所有调用的结果，其他的桶存储了之前每秒调用的结果。

**断路器是线程安全的:**

- 断路器的状态是原子引用。
- 断路器使用原子操作以无副作用的功能来更新状态。
- 从滑动窗口中记录调用结果和读取快照是同步的。

**配置**

默认在`io.github.resilience4j.circuitbreaker`包下的`CircuitBreakerConfig`类里

|                 配置                  |   默认值    |                             描述                             |
| :-----------------------------------: | :---------: | :----------------------------------------------------------: |
|         failureRateThreshold          |     50      | 以百分比配置失败率阈值。当失败率等于或大于阈值时，断路器状态并关闭变为开启，并进行服务降级。 |
|           slidingWindowType           | COUNT_BASED | 配置滑动窗口的类型,如果滑动窗口类型是COUNT_BASED，将会统计记录最近`slidingWindowSize`次调用的结果。如果是TIME_BASED，将会统计记录最近`slidingWindowSize`秒的调用结果。 |
|           slidingWindowSize           |     100     |                     配置滑动窗口的大小。                     |
|         slowCallRateThreshold         |     100     | 以百分比的方式配置，断路器把调用时间大于`slowCallDurationThreshold`的调用视为满调用，当慢调用比例大于等于阈值时，断路器开启，并进行服务降级。 |
|       slowCallDurationThreshold       | 60000 [ms]  | 配置调用时间的阈值，高于该阈值的呼叫视为慢调用，并增加慢调用比例。 |
| permittedNumberOfCallsInHalfOpenState |     10      |            断路器在半开状态下允许通过的调用次数。            |
|         minimumNumberOfCalls          |     100     | 断路器计算失败率或慢调用率之前所需的最小调用数（每个滑动窗口周期）。例如，如果minimumNumberOfCalls为10，则必须至少记录10个调用，然后才能计算失败率。如果只记录了9次调用，即使所有9次调用都失败，断路器也不会开启。 |
|        waitDurationInOpenState        | 60000 [ms]  |             断路器从开启过渡到半开应等待的时间。             |

#### 服务熔断，降级案例

**COUNT_BASED**:

1. 构建项目环境

   为生产者提供一个服务，写一个异常

   ```java
   @RestController
   public class PayCircuitController {
       @GetMapping("/pay/circuit/{id}")
       public ResultData circuitBreakerTest(@PathVariable("id") Integer id) {
           if (id == -1) {
               throw new RuntimeException("id 不能为负数");
           }
           if (id == 9) {
               try {
                   TimeUnit.SECONDS.sleep(5);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
           return ResultData.success("CircuitBreaker is close" + "\t" + id + "\t" + UUID.randomUUID());
       }
   }
   ```

2. 为消费者添加断路器（一般断路器加在客户端）

   - 导入依赖

     ```xml
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
     </dependency>
     <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-aop</artifactId>
     </dependency>
     <dependency>
     ```

   - 添加断路器配置

     1 开启断路器

     ```yml
     spring:
       cloud:
         openfeign:
           circuitbreaker:
             enabled: true
             group:
     #          分组，更细粒度的控制，没有分组则使用默认的配置
               enabled: true
     ```

     2 resilience4j的配置

     ```yml
     resilience4j:
       circuitbreaker:
         configs:
           default:
             # 失败率阈值
             failureRateThreshold: 50 #超过50%则熔断
             # 滑动窗口类型
             sliding-window-type: COUNT_BASED #表示使用基于数量的滑动窗口
             # 滑动窗口大小
             sliding-window-size: 6
             # 最小调用次数
             minimum-number-of-calls: 6 #计算失败率至少要6个请求
             # 自动从打开状态到半开状态
             automatic-transition-from-open-to-half-open-enabled: true
             # 打开状态的等待时间
             wait-duration-in-open-state: 5000
             # 半开状态允许的调用次数
             permitted-number-of-calls-in-half-open-state: 2 # 半开状态的允许最大的请求数量
             # 记录异常
             record-exceptions:
               - java.lang.Exception
         instances:
           # 服务的实例（为哪个服务配置断路器）
           cloud-provider-payment:
             # 使用default配置
             base-config: default
     ```

   - 编写feign调用

     在feign接口多加一个调用

     ```java
     @FeignClient(value = "cloud-provider-payment")
     public interface FeignAPI {
         @PostMapping("/pay/add")
         public ResultData addOrder(@RequestBody PayDTO payDTO);
         @GetMapping("/pay/delete/{id}")
         public ResultData deleteOrder(@PathVariable("id") Long id);
         @GetMapping("/pay/update")
         public ResultData updateOrder(@RequestBody PayDTO payDTO);
         @GetMapping("/pay/get/{id}")
         public ResultData getOrder(@PathVariable("id") Long id);
         @GetMapping("/pay/getall")
         public ResultData listOrder();
         @GetMapping("/getConsulInfo")
         public ResultData getConsulInfo();
         @GetMapping("/pay/circuit/{id}")
         public ResultData circuitBreakerTest(@PathVariable("id") Integer id);
     }
     ```

     编写controller

     ```java
     @RestController
     public class OrderCircuitController {
         @Resource
         private FeignAPI feignAPI;
         @GetMapping("/feign/circuit/{id}")
         @CircuitBreaker(name = "cloud-provider-payment", fallbackMethod = "myCircuitFallback")
         public ResultData circuitBreakerTest(@PathVariable("id") Integer id) {
             return feignAPI.circuitBreakerTest(id);
         }
         public ResultData myCircuitFallback(Integer id, Throwable throwable) {
             // 降级逻辑
             return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "服务异常，请稍后再试！");
         }
     }
     ```

     用`@CircuitBreaker`注解标注你要掉用哪个实例（在上面的`yml`中实例的名字）来开启哪些的熔断策略配置，`fallbackMethod`来标注你用哪个方法实现服务降级。

   - 测试后发现三次错误访问后，这个请求方法就被锁住了，错误不会扩大到其他的请求方法，实现了服务熔断，在访问错误请求后，出现了我们设定好的降级方法而不是异常，实现了服务降级。

**TIME_BASED**:

和上面一样，改改yml文件就好

```yaml
resilience4j:
  timelimiter:
    configs:
      default:
        # 设置超时时间
        timeout-duration:  10s
  circuitbreaker:
    configs:
      default:
        # 失败率阈值
        failureRateThreshold: 50 #超过50%则熔断
        # 慢请求时间阈值
        slow-call-duration-threshold: 2s
        # 慢请求率阈值
        slow-call-rate-threshold: 30
        # 滑动窗口类型
        sliding-window-type: TIME_BASED #表示使用基于时间的滑动窗口
        # 滑动窗口大小
        sliding-window-size: 2 #记录最近`2`秒的调用结果
        # 最小调用次数
        minimum-number-of-calls: 2 #计算失败率至少要2个请求
        # 自动从打开状态到半开状态
        automatic-transition-from-open-to-half-open-enabled: true
        # 打开状态的等待时间
        wait-duration-in-open-state: 10s
        # 半开状态允许的调用次数
        permitted-number-of-calls-in-half-open-state: 2 # 半开状态的允许最大的请求数量
        # 记录异常
        record-exceptions:
          - java.lang.Exception
    instances:
      # 服务的实例（为哪个服务配置断路器）
      cloud-provider-payment:
        # 使用default配置
        base-config: default
```

#### Bulkhead（舱壁）

**什么是舱壁**

Resilience4J-Bulkhead 是 Resilience4J 库中的一个模块，用于实现舱壁隔离（Bulkhead）模式。舱壁隔离是一种容错模式，它通过限制并发执行的线程数或可用资源的数量来保护系统，防止系统过载。舱壁隔离类似于船只上的舱壁，当一艘船的一个部分受损时，舱壁可以防止水蔓延到整个船只，从而保持船只的浮力。

**舱壁隔离的作用**

1. **限制并发请求数**：
   - 限制同时执行的请求数量，避免过多的并发请求导致系统资源耗尽。
2. **保护资源**：
   - 通过限制对共享资源（如数据库连接池、消息队列等）的访问，防止资源被过度使用。
3. **提高稳定性**：
   - 通过限制并发性，可以减少系统在高峰负载下的抖动，提高系统的稳定性和响应时间的一致性。
4. **快速失败**：
   - 当达到并发请求数上限时，后续请求会被立即拒绝，而不是等待现有请求完成，从而避免了请求堆积。

**怎么用**

Resilience4j提供了两种隔离的实现方式，可以限制并发执行的数量。

- `SemaphoreBulkhead`使用了信号量
- `FixedThreadPoolBulkhead`使用了有界队列和固定大小线程池

`SemaphoreBulkhead`可以在各种线程和I/O模型上正常工作。与Hystrix不同，它不提供基于shadow的thread选项。由客户端来确保正确的线程池大小与隔离配置一致。

##### **SemaphoreBulkhead**

`SemaphoreBulkhead` 是 Resilience4J 中实现舱壁隔离的一种方式，它使用信号量（Semaphore）来限制并发执行的请求数量。信号量是一种同步原语，可以用来控制对共享资源的访问。

**原理**

`SemaphoreBulkhead` 的工作原理是通过一个信号量来管理一个固定大小的许可池。当一个线程想要执行一个受保护的操作时，它必须先从信号量中获取一个许可。如果信号量中还有可用的许可，线程就可以继续执行；如果没有可用的许可，线程将被阻塞，直到有其他线程释放许可。

```java
public SemaphoreBulkhead(String name, @Nullable BulkheadConfig bulkheadConfig, Map<String, String> tags) {
    this.configChangesLock = new Object();
    this.name = name;
    this.config = (BulkheadConfig)Objects.requireNonNull(bulkheadConfig, "Config must not be null");
    this.tags = (Map)Objects.requireNonNull(tags, "Tags must not be null");
    this.semaphore = new Semaphore(this.config.getMaxConcurrentCalls(), this.config.isFairCallHandlingEnabled());
    this.metrics = new BulkheadMetrics();
    this.eventProcessor = new BulkheadEventProcessor();
}
```

其源码的本质还是使用了JUC里的semaphore，传的两个参数分别是最大线程数量和是否是公平调度（类似公平锁和非公平锁）默认是非公平调度

**怎么用**

1. **添加依赖**

   ```xml
   <dependency>
       <groupId>io.github.resilience4j</groupId>
       <artifactId>resilience4j-bulkhead</artifactId>
   </dependency>
   ```

2. **写yml**

   ```yml
   resilience4j:
     bulkhead:
       configs:
         default:
           # 最大并发调用数
           max-concurrent-calls: 2
           # 最大等待时间
           max-wait-duration: 1s
       instances:
         # 云提供者支付
         cloud-provider-payment:
           # 基础配置
           base-config: default
     timelimiter:
       configs:
         default:
           # 超时时间
           timeout-duration: 20s
   ```

3. **controller**

   ```java
   @GetMapping("/feign/bulkhead/{id}")
   @Bulkhead(name = "cloud-provider-payment", fallbackMethod = "myBulkheadFallback", type = Bulkhead.Type.SEMAPHORE)
   public ResultData bulkheadTest(@PathVariable("id") Integer id) {
       return feignAPI.bulkheadTest(id);
   }
   public ResultData myBulkheadFallback(Integer id, Throwable throwable) {
       // 降级逻辑
       return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "服务异常，请稍后再试！");
   }
   ```

   使用`@Bulkhead`注解指明使用哪个配置实例（在yml中的instances）和 服务降级和 类型是哪个

##### **FixedThreadPoolBulkhead**

`FixedThreadPoolBulkhead` 是 Resilience4J 中实现舱壁隔离的另一种方式，它使用固定大小的线程池来限制并发执行的请求数量。与 `SemaphoreBulkhead` 不同，`FixedThreadPoolBulkhead` 直接管理一个线程池，所有的请求都在这个线程池中执行。

**原理**

`FixedThreadPoolBulkhead` 的工作原理是通过一个固定大小的线程池来控制并发执行的任务数量。当一个任务提交给线程池时，如果线程池中的线程数量没有达到最大值，那么这个任务就会被分配给一个新的线程执行。如果线程池中的线程数量已经达到最大值，那么新的任务将被放入一个队列中等待执行。

如果队列也满了，那么 `FixedThreadPoolBulkhead` 会拒绝新的任务，并抛出 `RejectedExecutionException` 或 `BulkheadFullException`，具体取决于配置。

```java
public FixedThreadPoolBulkhead(String name, @Nullable ThreadPoolBulkheadConfig bulkheadConfig, Map<String, String> tags) {
    this.name = name;
    this.config = (ThreadPoolBulkheadConfig)Objects.requireNonNull(bulkheadConfig, "Config must not be null");
    this.tags = (Map)Objects.requireNonNull(tags, "Tags must not be null");
    this.executorService = new ThreadPoolExecutor(this.config.getCoreThreadPoolSize(), this.config.getMaxThreadPoolSize(), this.config.getKeepAliveDuration().toMillis(), TimeUnit.MILLISECONDS, (BlockingQueue)(this.config.getQueueCapacity() == 0 ? new SynchronousQueue() : new ArrayBlockingQueue(this.config.getQueueCapacity())), new BulkheadNamingThreadFactory(name), this.config.getRejectedExecutionHandler());
    this.metrics = new BulkheadMetrics();
    this.eventProcessor = new BulkheadEventProcessor();
}
```

底层使用的自定义的线程池

**怎么用**

1. 导入依赖

   ```xml
   <dependency>
       <groupId>io.github.resilience4j</groupId>
       <artifactId>resilience4j-bulkhead</artifactId>
   </dependency>
   ```

2. 写yml

   ```yml
   resilience4j:
     # 配置超时时间
     timelimiter:
       configs:
         default:
           # 设置超时时间
           timeout-duration: 10s
     # 配置线程池
     thread-pool-bulkhead:
       configs:
         default:
           # 设置核心线程池大小
           core-thread-pool-size: 1
           # 设置最大线程池大小
           max-thread-pool-size: 1
           # 设置队列容量
           queue-capacity: 1
       # 实例配置
       instances:
         # 配置云提供者支付
         cloud-provider-payment:
           # 基础配置
           base-config: default
   ```

   3. controller

      ```java
      @GetMapping("/feign/bulkheadTHREADPOOL/{id}")
      @Bulkhead(name = "cloud-provider-payment", fallbackMethod = "myBulkheadFallback", type = Bulkhead.Type.THREADPOOL)
      public CompletableFuture<ResultData> bulkheadTHREADPOOLTest(@PathVariable("id") Integer id) {
          System.out.println("---------------->");
          try {
              Thread.sleep(3000);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
          System.out.println("<----------------");
          return CompletableFuture.supplyAsync(() -> feignAPI.bulkheadTest(id));
      }
      public CompletableFuture<ResultData> myBulkheadTHREADPOOLFallback(Integer id, Throwable throwable) {
          // 降级逻辑
          return CompletableFuture.supplyAsync(() -> ResultData.error(ReturnCodeEnum.FAIL.getCode(), "服务异常，请稍后再试！"));
      }
      ```

#### RateLimiter  (限流器)

RateLimiter（限流器）是一种用于限制单位时间内请求处理数量的机制。它可以帮助保护系统资源不被过度消耗，防止系统过载，特别是在高并发场景下。RateLimiter 可以用于客户端和服务端，用于控制请求的速率。

**RateLimiter 的原理**

RateLimiter 的工作原理是通过某种算法来计算在单位时间内允许通过的请求数量。当请求超过设定的速率时，额外的请求会被拒绝或延迟处理。

**常见的限流算法**

1. **固定窗口算法 (Fixed Window Algorithm)**

- **原理**：
  - 将时间划分为固定长度的窗口，在每个窗口内统计请求的数量。
  - 如果在一个窗口内的请求超过了阈值，那么超出的部分将被拒绝。
  - 当一个窗口结束时，计数器被重置。
- **优点**：
  - 实现简单。
- **缺点**：
  - 容易产生突发流量问题，即在窗口开始时会出现突发请求。
  - 无法平滑请求的处理。

2. **滑动窗口算法 (Sliding Window Algorithm)**

- **原理**：
  - 类似于固定窗口算法，但窗口被划分为更小的时间片，每个时间片内都会有一个计数器。
  - 在每个时间点，都会计算过去一段时间内的总请求数量。
  - 如果请求数量超过了阈值，超出部分将被拒绝。
- **优点**：
  - 能够更精确地控制请求速率。
  - 减少了突发流量问题。
- **缺点**：
  - 实现相对复杂。
  - 需要存储更多的状态信息。

3. **漏桶算法 (Leaky Bucket Algorithm)**

- **原理**：
  - 请求被放入一个缓冲队列中，队列有一个固定的容量。
  - 队列中的请求以恒定的速率被处理。
  - 如果请求超过了队列的容量，新的请求将被拒绝。
- **优点**：
  - 简单且易于实现。
  - 平滑请求的处理。
- **缺点**：
  - 可能会导致请求积压。
  - 难以调整处理速率。

4. **令牌桶算法 (Token Bucket Algorithm)**（默认）

- **原理**：
  - 一个装有令牌的桶，桶以恒定的速率生成令牌。
  - 请求需要消耗一个令牌才能通过。
  - 如果桶中的令牌不足，请求将被拒绝或延迟。
  - 桶可以有一个最大容量，多余的令牌会被丢弃。
- **优点**：
  - 能够灵活地处理突发流量。
  - 可以调节请求速率。
- **缺点**：
  - 实现比漏桶算法稍微复杂一些。
  - 需要额外的状态管理。v

**怎么用**

1. 导入依赖

   ```xml
   <dependency>
       <groupId>io.github.resilience4j</groupId>
       <artifactId>resilience4j-ratelimiter</artifactId>
   </dependency>
   ```

2. 写yml

   ```yml
   resilience4j:
     ratelimiter:
       configs:
         default:
           # 在一次刷新周期最大的请求数
           limit-for-period: 2
           #  刷新周期
           limit-refresh-period: 1s
           # 线程等待时间
           timeout-duration: 1
       instances:
         cloud-provider-payment:
           base-config: default
   ```

3. controller

   ```java
   @GetMapping("/feign/ratelimit/{id}")
   @RateLimiter(name = "cloud-provider-payment", fallbackMethod = "myRateLimitFallback")
   public ResultData rateLimitTest(@PathVariable("id") Integer id) {
       return feignAPI.rateLimitTest(id);
   }
   public ResultData myRateLimitFallback(Integer id, Throwable throwable) {
       // 降级逻辑
       return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "被限流，请稍后再试！");
   }
   ```

   用`@RateLimiter`注解，用发和前面类似