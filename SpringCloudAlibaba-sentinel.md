---
title:Sentinel
---

官网：`https://sentinelguard.io/`

`SpringCloudAlibaba`文档：`https://spring-cloud-alibaba-group.github.io/github-pages/2021/zh-cn/index.html#_sentinel_%E4%BB%8B%E7%BB%8D`

### 什么是 Spring Cloud Alibaba Sentinel？

Spring Cloud Alibaba Sentinel 是一个轻量级的流量控制组件，它主要关注的是流量的控制、熔断和降级，以保护服务不受异常流量的影响。它与 Spring Cloud Alibaba Nacos、Spring Cloud Alibaba Dubbo 等组件一起，构成了 Spring Cloud Alibaba 微服务框架的一部分。

### 它能干什么？

1. **流量控制**：
   - **限制流量**：Sentinel 可以帮助你限制进入服务的流量，防止服务过载。
   - **热点防护**：可以限制某个资源或参数的访问频率，避免热点资源成为瓶颈。
2. **熔断降级**：
   - **快速失败**：当某个服务出现问题时，Sentinel 可以立即停止对该服务的调用，避免雪崩效应。
   - **优雅降级**：当服务压力过大时，可以选择性地拒绝某些请求，保证核心业务的正常运行。
3. **集群限流**：
   - **分布式流量控制**：Sentinel 支持跨服务的流量控制，可以在集群中进行统一的流量控制。
4. **实时监控**：
   - **监控服务状态**：Sentinel 可以实时监控服务的状态，包括 QPS、响应时间等指标。
   - **预警机制**：可以设置阈值，当服务状态超过阈值时触发报警。

### 下载运行

下载地址：`https://github.com/alibaba/Sentinel/releases`

运行命令： java -jar xxx

客户端地址：`http://localhost:8080`

 初始账号密码：sentinel

### 简单使用

1. 建模块

2. 导入依赖

   ```xml
   <dependencies>
       <dependency>
           <groupId>com.alibaba.cloud</groupId>
           <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
   
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-actuator</artifactId>
       </dependency>
   
       <dependency>
           <groupId>com.alibaba.cloud</groupId>
           <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
       </dependency>
   ```

3. yml

   ```yml
   server:
     port: 8401
   spring:
     application:
       name: cloud-sentinel-server
     cloud:
       nacos:
         discovery:
           server-addr: localhost:8848
       sentinel:
         transport:
           dashboard: localhost:8080
           port: 8719
   ```

   1. **`spring.cloud.sentinel.transport.dashboard: localhost:8080`**：
      - **含义**：指定了 Sentinel 控制台的地址为 `localhost:8080`。
      - **作用**：服务实例会向这个地址的 Sentinel 控制台发送监控数据，以便在 Sentinel 控制台上查看服务的状态和进行流量控制配置。
   2. **`spring.cloud.sentinel.transport.port: 8719`**：
      - **含义**：指定了服务实例与 Sentinel 控制台之间通信的端口为 8719。
      - **作用**：服务实例通过这个端口与 Sentinel 控制台进行通信，发送监控数据和接收流量控制指令。

4. Controller测试

   发送请求，到8080控制台去看看情况

### 流控规则

流控规则如下配置

![](.\image\Snipaste_2024-08-15_19-14-50.png)

#### 流控模式

1. **直接 (默认)**：
   - **含义**：直接针对被保护的资源进行限流。
   - **应用场景**：适用于对单个资源进行限流的情况。
2. **关联**：
   - **含义**：通过关联其他资源的流量来间接控制被保护资源的流量。
   - **应用场景**：适用于需要根据其他资源的流量来间接控制某个资源的情况。
3. **链路**：
   - **含义**：对整个链路或一组资源进行限流。
   - **应用场景**：适用于对整个链路或一组相关资源进行限流的情况。

#### 流控效果

1. **快速失败**：
   - **含义**：当达到限流阈值时，直接拒绝后续请求。
   - **应用场景**：适用于需要快速响应的情况，以避免系统过载。
2. **Warm Up**：
   - **含义**：在限流阈值逐渐提升的过程中，允许更多的请求逐渐进入，而不是突然达到最大阈值。
   - **应用场景**：适用于需要平滑过渡到最大限流阈值的情况，以避免系统瞬间承受大量请求的压力。
3. **排队等待**：
   - **含义**：当达到限流阈值时，新来的请求会被放入队列中等待处理。
   - **应用场景**：适用于需要保证服务响应时间稳定的情况，即使请求较多也能保持良好的用户体验。

### 熔断规则

<img src=".\image\Snipaste_2024-08-15_19-40-39.png" style="zoom:75%;" />

#### 属性

|                    |                                                              |            |
| :----------------: | :----------------------------------------------------------- | :--------- |
|       Field        | 说明                                                         | 默认值     |
|      resource      | 资源名，即规则的作用对象                                     |            |
|       grade        | 熔断策略，支持慢调用比例/异常比例/异常数策略                 | 慢调用比例 |
|       count        | 慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值 |            |
|     timeWindow     | 熔断时长，单位为 s                                           |            |
|  minRequestAmount  | 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入） | 5          |
|   statIntervalMs   | 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）   | 1000 ms    |
| slowRatioThreshold | 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）           |            |

#### 熔断策略

- 慢调用比例 (`SLOW_REQUEST_RATIO`)：选择以慢调用比例作为阈值，需要设置允许的慢调用 RT（即最大的响应时间），请求的响应时间大于该值则统计为慢调用。当单位统计时长（`statIntervalMs`）内请求数目大于设置的最小请求数目，并且慢调用的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断。
- 异常比例 (`ERROR_RATIO`)：当单位统计时长（`statIntervalMs`）内请求数目大于设置的最小请求数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。异常比率的阈值范围是 `[0.0, 1.0]`，代表 0% - 100%。
- 异常数 (`ERROR_COUNT`)：当单位统计时长内的异常数目超过阈值之后会自动进行熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。

### @SentinelResource注解

`@SentinelResource` 是 Sentinel 提供的一个注解，用于标记需要进行流量控制的方法。这个注解可以应用于类或方法级别，用于定义流量控制规则。

####`@SentinelResource` 注解的作用

1. **流量控制**：
   - `@SentinelResource` 可以用来定义流量控制规则，包括直接流控、关联流控和链路流控。
   - 它可以限制某个方法或类的 QPS（每秒查询数），从而防止服务过载。
2. **异常处理**：
   - `@SentinelResource` 还可以定义异常处理逻辑，当方法抛出异常时，可以指定一个备用的方法来处理异常。
3. **降级处理**：
   - 除了流量控制外，还可以定义降级处理逻辑，当流量控制生效时，可以指定一个备用的方法来处理请求。

#### 如何使用 `@SentinelResource` 注解

1. **基本使用**：
   - 在方法上添加 `@SentinelResource` 注解，并指定资源名称。
   - 可以指定流控规则，例如 QPS 限制。
2. **异常处理**：
   - 可以通过 `exceptionsToIgnore` 属性指定忽略哪些异常。
   - 通过 `fallback` 属性指定一个备用的方法，当原方法抛出异常时，会调用这个备用方法。
3. **降级处理**：
   - 通过 `fallback` 属性指定一个备用的方法，当流量控制生效时，会调用这个备用方法。
   - 可以通过 `blockHandler` 属性指定一个备用的方法，专门处理被限流的请求。

#### 示例

下面是一个简单的示例，展示了如何使用 `@SentinelResource` 注解：

java深色版本

```java
1import com.alibaba.csp.sentinel.annotation.SentinelResource;
2import org.springframework.web.bind.annotation.GetMapping;
3import org.springframework.web.bind.annotation.RestController;
4
5@RestController
6public class SentinelExampleController {
7
8    // 定义一个受流量控制的方法
9    @GetMapping("/product")
10    @SentinelResource(value = "product", fallback = "handleProductFallback")
11    public String getProductDetails() {
12        // 业务逻辑
13        return "Product details";
14    }
15
16    // 备用方法，用于处理异常
17    public String handleProductFallback() {
18        return "Oops, something went wrong. Please try again later.";
19    }
20
21    // 定义另一个受流量控制的方法
22    @GetMapping("/checkout")
23    @SentinelResource(value = "checkout", blockHandler = "handleCheckoutBlock")
24    public String checkout() {
25        // 业务逻辑
26        return "Checkout successful!";
27    }
28
29    // 备用方法，用于处理被限流的请求
30    public String handleCheckoutBlock() {
31        return "Oops, the checkout is too busy now. Please try again later.";
32    }
33}
```

解释

1. **`@SentinelResource(value = "product", fallback = "handleProductFallback")`**：
   - **`value`**：指定资源名称，这里是 `product`。
   - **`fallback`**：指定一个备用方法 `handleProductFallback`，当 `getProductDetails` 方法抛出异常时，会调用这个备用方法。
2. **`@SentinelResource(value = "checkout", blockHandler = "handleCheckoutBlock")`**：
   - **`value`**：指定资源名称，这里是 `checkout`。
   - **`blockHandler`**：指定一个备用方法 `handleCheckoutBlock`，当 `checkout` 方法被限流时，会调用这个备用方法。

### 热点规则

#### 热点规则概念

热点规则是指对 API 请求中的热点参数进行限流。热点参数通常是那些频繁出现的参数值，如商品 ID、用户 ID 等。当这些参数值成为热点时，可能会导致服务过载。热点规则可以帮助你统计这些热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。

#### 应用场景

热点规则适用于以下场景：

1. **商品 ID 为参数**：
   - **场景**：在电商网站中，某些商品可能会非常受欢迎，导致商品详情页面的访问量激增。
   - **解决方案**：可以使用热点规则来限制访问频率最高的商品 ID，防止服务过载。
2. **用户 ID 为参数**：
   - **场景**：在社交网络应用中，某些用户的活动可能非常频繁，导致服务器压力增大。
   - **解决方案**：可以使用热点规则来限制访问频率最高的用户 ID，保护服务免受异常流量的影响。
3. **其他参数**：
   - **场景**：任何具有热点特性的参数都可以使用热点规则来限流，例如 IP 地址、地理位置等。
   - **解决方案**：通过对这些参数进行限流，可以防止服务受到异常流量的影响。

![](.\image\Snipaste_2024-08-15_20-09-43.png)

### 授权规则

授权规则是 Sentinel 中用于控制调用方的来源的一种规则。它可以帮助你定义哪些客户端可以访问你的服务，通常有两种模式：白名单和黑名单。

#### 授权规则概念

1. **白名单**：
   - **含义**：只有在白名单中的客户端才允许访问服务。
   - **应用场景**：适用于需要严格控制访问来源的场景。
2. **黑名单**：
   - **含义**：黑名单中的客户端不允许访问服务。
   - **应用场景**：适用于需要排除某些恶意或不可信客户端的场景。

#### 怎么使用

1. 测试的controller

   ```java
   @RestController
   public class SentinelController {
       @GetMapping("/testA")
       public String testA() {
           return "-----------testA";
       }
       @GetMapping("/testB")
       public String testB() {
           return "-----------testB";
       }
   }
   ```

2. 重写RequestOriginParser接口

   ```java
   @Component
   public class MyRequestOriginParser implements RequestOriginParser {
       @Override
       public String parseOrigin(HttpServletRequest httpServletRequest) {
           return httpServletRequest.getParameter("ip");
       }
   }
   ```

3. 配置规则

   ![](.\image\Snipaste_2024-08-15_20-24-02.png)

 4. 测试

    http://localhost:8401/testA?ip=223  ×

    http://localhost:8401/testA?ip=123  ×

    http://localhost:8401/testA               √

    http://localhost:8401/testA?ip=423  √

### 规则持久化

持久化规则是指将 Sentinel 中定义的规则（如流控规则、授权规则等）保存到外部存储中，例如数据库、文件系统或配置中心，以确保服务重启后规则依然有效。

用nacos持久化

1. 导入依赖

   ```xml
   <dependency>
       <groupId>com.alibaba.csp</groupId>
       <artifactId>sentinel-datasource-nacos</artifactId>
   </dependency>
   ```

2. yml

   ```yml
   server:
     port: 8401
   spring:
     application:
       name: cloud-sentinel-server
     cloud:
       nacos:
         discovery:
           server-addr: localhost:8848
       sentinel:
         transport:
           dashboard: localhost:8080
           port: 8719
         datasource:
           ds1:
             nacos:
               server-addr: localhost:8848
               dataId: ${spring.application.name}
               groupId: DEFAULT_GROUP
               rule-type: flow
               data-type: json
   ```

3. nacos配置

   ![](D:\study notes\springcloud学习\image\Snipaste_2024-08-15_21-00-04.png)

 ```json
 [
   {
     "resource": "/testA",
     "limitApp": "default",
     "grade": 1,
     "count": 1,
     "strategy": 0,
     "controlBehavior": 0,
     "clusterMode": false
   }
 ]
 ```

4. 启动测试

### sentinel 整合 openfeign

1. cloud-provider-payment-nacos7001

   - 导入sentine，openfeign依赖

     ```xml
     <dependency>
         <groupId>com.alibaba.cloud</groupId>
         <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
     </dependency>
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-openfeign</artifactId>
     </dependency>
     ```

   - yml

     ```yml
     server:
       port: 7001
     spring:
       application:
         name: cloud-provider-payment-nacos
       cloud:
         nacos:
           discovery:
             server-addr: localhost:8848
         sentinel:
           transport:
             dashboard: localhost:8080
             port: 8719
     ```

   - controller

     ```java
     @Value("${server.port}")
     private String port;
     @GetMapping(value = "/pay/nacos/info")
     @SentinelResource(value = "nacos-info", blockHandler = "blockHandler")
     public ResultData<String> echo() {
         return ResultData.success("Hello Nacos Discovery" + port);
     }
     
     public ResultData<String> blockHandler(BlockException e) {
         return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "被限流了");
     }
     ```

2. feign接口

   ```java
   @FeignClient(value = "cloud-provider-payment-nacos", fallback =  NacosFeignFallback.class)
   public interface NacosFeign {
       @GetMapping(value = "/pay/nacos/info")
       public ResultData<String> echo();
   }
   ```

   fallback

   ```java
   @Component
   public class NacosFeignFallback  implements NacosFeign {
       @Override
       public ResultData<String> echo() {
           return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "fallback服务降级");
       }
   }
   ```

3. cloud-consumer-order-nacos7002

   - 导入依赖

     ```xml
     <dependency>
         <groupId>com.alibaba.cloud</groupId>
         <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
     </dependency>
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-openfeign</artifactId>
     </dependency>
     ```

   - yml

     ```yml
     feign:
       sentinel:
         enabled: true
     ```

   - 启动类Enablefeignclient

   - controller

   - ```java
     @RestController
     public class OrderController {
         @Resource
         private RestTemplate restTemplate;
         @Value("${service-url.nacos-user-service}")
         private String URL;
     
         @Resource
         private NacosFeign nacosFeign;
         @GetMapping("/consumer/pay/nacos/info")
         public ResultData<String> getInfo() {
     //        return restTemplate.getForObject(URL + "/pay/nacos/info", ResultData.class);
             return nacosFeign.echo();
         }
     }
     ```

   - 注意版本兼容问题，项目可能跑不起来

     springboot3.2 和 cloud2023 和sentinel 1.8.8目前不兼容

     ```xml
     <!--        <spring.boot.version>3.2.0</spring.boot.version>-->
     <!--        <spring.cloud.version>2023.0.0</spring.cloud.version>-->
             <spring.boot.version>3.0.9</spring.boot.version>
             <spring.cloud.version>2022.0.2</spring.cloud.version>
     ```

