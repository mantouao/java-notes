---
title:网关-gateway
---

## 什么是网关及用途

**定义：** 网关是一个服务器程序，它作为客户端和多个后端服务之间的中介。在微服务架构中，通常指的是API网关。

#### **用途：**

1. **统一入口：** 对外提供一个统一的服务访问入口。
2. **路由管理：** 根据请求路径将请求转发给不同的服务。
3. **负载均衡：** 在多个实例间分配流量。
4. **安全控制：** 实现认证鉴权等安全措施。
5. **监控统计：** 收集请求数据用于监控和服务质量改进。
6. **协议转换：** 在不同协议之间进行转换。
7. **限流降级：** 控制并发数，避免过载。

#### 常见网关类型及其区别

1. Zuul

   （由Netflix开发）

   - **特点：** 较早出现，功能全面，支持多种路由策略。
   - **缺点：** 性能相对较低，配置复杂。

2. Spring Cloud Gateway

   - **特点：** 基于Spring Framework 5、Project Reactor和WebFlux，性能优秀。
   - **优点：** 配置简单，社区活跃，文档丰富。

3. Kong

   - **特点：** 开源且可扩展性强，支持多种插件。
   - **应用场景：** 适合大规模部署。

4. Nginx

   - 特点：
     - 高性能的反向代理服务器，稳定性强。
     - 配置灵活，可以根据需要定制。
     - 支持多种负载均衡算法。
   - 适用场景：
     - 对性能和稳定性有极高要求的应用场景。
     - 需要手动配置来实现高级特性的项目。

## GateWay

Spring Cloud Gateway是基于Spring Framework 5、Project Reactor和WebFlux的新一代API网关框架。它提供了比Zuul更强大的功能，并且更易于使用。

**Web (Spring MVC)**

Spring MVC是Spring框架的一部分，用于构建传统的Web应用程序。它使用Servlet API，并且是基于阻塞I/O的。

**特点**

- **阻塞式I/O：** 每个请求都需要等待直到响应被完全处理完毕。
- **线程池：** 使用线程池来处理每个请求，每个请求占用一个线程。
- **请求-响应模型：** 请求到达时，控制器处理请求并返回响应。
- **同步编程模型：** 编程模型较为直观，易于理解和调试。
- **支持：** 支持各种视图技术，如Thymeleaf、FreeMarker等。

**适用场景**

- **低延迟、低并发的应用：** 当应用不需要处理大量并发连接时。
- **需要渲染复杂HTML页面的应用：** 对于需要动态生成HTML页面的应用。
- **传统的企业级应用：** 大多数现有的Spring MVC应用都属于这一类。

**WebFlux**

WebFlux是Spring 5引入的一个新的响应式Web框架，它利用了非阻塞I/O和响应式编程模型。

**特点**

- **非阻塞I/O：** 不需要等待每个操作完成，可以立即处理下一个请求。
- **响应式编程模型：** 基于事件驱动和异步处理，使用Reactor模式。
- **无状态：** 没有线程上下文切换，因此可以处理更多的并发请求。
- **流处理：** 支持流式数据处理，可以高效地处理大数据量传输。
- **性能：** 由于减少了线程切换开销，对于高并发场景性能更好。

**适用场景**

- **高并发、高吞吐量的应用：** 如API网关、消息系统等。
- **需要处理大量数据流的应用：** 如实时数据分析、流媒体服务等。
- **微服务架构中的服务：** 适合构建轻量级、高性能的服务。

#### 工作流程

1. **接收请求：** 客户端向网关发送HTTP请求。
2. **解析配置：** 网关根据配置文件中的定义查找匹配的路由规则。
3. **断言匹配：** 对每个路由规则，网关使用断言来检查请求是否符合该路由规则。
4. **过滤器执行：** 如果断言匹配成功，则执行相关的过滤器。
5. **请求转发：** 经过过滤器处理后，请求被转发到目标服务。
6. **响应处理：** 目标服务处理请求并返回响应。
7. **响应过滤：** 网关再次执行响应过滤器。
8. **返回响应：** 最终响应被返回给客户端。

#### 简单示例

背景搭建：

生产者：提供两个用来测试的接口

```yml
@RestController
public class PayGateWayController {
    @Resource
    private PayService payService;
    @GetMapping("/pay/gateway/get/{id}")
    public ResultData<Pay> getPay(@PathVariable("id") Integer id) {
        Pay pay = null;
        try {
            pay = payService.getPayById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "查询失败");
        }
        return ResultData.success(pay);
    }
    @GetMapping("/pay/gateway/info")
    public ResultData<String> getInfo() {
        return ResultData.success("gateway info : " + UUID.randomUUID());
    }

}
```

消费者：调用接口服务

```java
@RestController
public class OrderGateWayController {
    @Resource
    private FeignAPI feignAPI;
    @GetMapping("/feign/pay/gateway/get/{id}")
    public ResultData getById(@PathVariable("id") Integer id) {
        return feignAPI.getPay(id);
    }
    @GetMapping("/feign/pay/gateway/info")
    public ResultData getGateWayInfo() {
        return feignAPI.getInfo();
    }
}
```

搭建网关:

1. 新建一个模块当网关模块

   ```java
   @SpringBootApplication
   @EnableDiscoveryClient
   
   public class Main9527 {
       public static void main(String[] args) {
           SpringApplication.run(Main9527.class, args);
       }
   }
   ```

2. 导入依赖（gateway网关，consul注册中心（利用注册中心调服务），健康检查）

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-gateway</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-consul-discovery</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-actuator</artifactId>
       </dependency>
   </dependencies>
   ```

3. yml配置

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
   ```

4. feign接口

   ```java
   //@FeignClient(value = "cloud-provider-payment")
   @FeignClient(value = "cloud-gateway")
   public interface FeignAPI {}
   ```

   把feign的调用目标改成网关的哪个服务

#### Gateway应用基础

1. 重要词汇：
   - **Route(路由)**：网关的基本构建块。它由 ID、目标 URI、谓词集合和过滤器集合定义。如果聚合谓词为真，则路由匹配。
   - **Predicate(谓词)**：这是一个Java 8 函数谓词。输入类型是Spring 框架`ServerWebExchange`。这允许您匹配 HTTP 请求中的任何内容，例如标头或参数。
   - **Filter(过滤器)**：这些是`GatewayFilter`使用特定工厂构建的实例。在这里，您可以在发送下游请求之前或之后修改请求和响应。

配置谓词和过滤器有两种方式：快捷方式和完全展开参数。

快捷：

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: after_route
        uri: https://example.org
        predicates:
        - Cookie=mycookie,mycookievalue
```

全展开：

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: after_route
        uri: https://example.org
        predicates:
        - name: Cookie
          args:
            name: mycookie
            regexp: mycookievalue
```



##### Predicate

Spring Cloud Gateway 包含许多内置路由谓词工厂。

| 谓词名称     | 作用                                                         |
| ------------ | ------------------------------------------------------------ |
| `After`      | 匹配发生在指定日期(格式是java ZonedDateTime)之后的请求。     |
| `Before`     | 匹配发生在指定日期(格式是java ZonedDateTime)之前的请求。     |
| `Between`    | 匹配发生在两个指定日期(格式是java ZonedDateTime)之间的请求。 |
| `Cookie`     | 匹配包含特定名称和值的Cookie的请求。                         |
| `Header`     | 匹配包含特定名称和值的请求头的请求。                         |
| `Host`       | 匹配特定主机名的请求。                                       |
| `Method`     | 匹配特定HTTP方法（如GET、POST等）的请求。                    |
| `Path`       | 匹配特定路径的请求。                                         |
| `Query`      | 匹配包含特定查询参数的请求。                                 |
| `RemoteAddr` | 匹配来自特定IP地址范围的请求。                               |

**示例**

1. After

   获取时间

   ```java
   ZonedDateTime now = ZonedDateTime.now();
   System.out.println(now);
   ```

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
               - After=2024-08-12T18:35:30.561533500+08:00[Asia/Shanghai]
   ```

2. Before

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
               - Before=2024-08-12T18:38:30.561533500+08:00[Asia/Shanghai]
   ```

3. Between

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
               - Between=2024-08-12T18:38:30.561533500+08:00[Asia/Shanghai], 2024-08-12T18:40:30.561533500+08:00[Asia/Shanghai]
   ```

4. Cookie

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
               - Cookie=username,ww
   ```

5. Header

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
               - Header=username,ww
   ```

6. Host

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
               - Host=**.bb.com
   ```

7. Method

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
               - Method=GET, POST
   ```

8. Path

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
             
   ```

9. Query

   ```yml
   server:
     port: 9527
   spring:
     application:
       name: cloud-gateway
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           prefer-ip-address: true
           service-name: ${spring.application.name}
       gateway:
         routes:
           - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment # 指向服务提供者
             predicates:     # 判断条件
               - Path=/pay/gateway/get/**
           - id: cloud-gateway2
   #          uri: http://localhost:8001
             uri: lb://cloud-provider-payment
             predicates:
               - Path=/pay/gateway/info/**
               - Query=payId, \d+
   ```

10. RemoteAddr

    ```yml
    server:
      port: 9527
    spring:
      application:
        name: cloud-gateway
      cloud:
        consul:
          host: localhost
          port: 8500
          discovery:
            prefer-ip-address: true
            service-name: ${spring.application.name}
        gateway:
          routes:
            - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
    #          uri: http://localhost:8001
              uri: lb://cloud-provider-payment # 指向服务提供者
              predicates:     # 判断条件
                - Path=/pay/gateway/get/**
            - id: cloud-gateway2
    #          uri: http://localhost:8001
              uri: lb://cloud-provider-payment
              predicates:
                - Path=/pay/gateway/info/**
                - RemoteAddr=192.168.0.1/24
    ```

11. 自定义谓词

    ```java
    public class AfterRoutePredicateFactory extends AbstractRoutePredicateFactory<Config> {
        public static final String DATETIME_KEY = "datetime";
    
        public AfterRoutePredicateFactory() {
            super(Config.class);
        }
    
        public List<String> shortcutFieldOrder() {
            return Collections.singletonList("datetime");
        }
    
        public Predicate<ServerWebExchange> apply(final Config config) {
            return new GatewayPredicate() {
                public boolean test(ServerWebExchange serverWebExchange) {
                    ZonedDateTime now = ZonedDateTime.now();
                    return now.isAfter(config.getDatetime());
                }
    
                public Object getConfig() {
                    return config;
                }
    
                public String toString() {
                    return String.format("After: %s", config.getDatetime());
                }
            };
        }
    
        public static class Config {
            private @NotNull ZonedDateTime datetime;
    
            public Config() {
            }
    
            public ZonedDateTime getDatetime() {
                return this.datetime;
            }
    
            public void setDatetime(ZonedDateTime datetime) {
                this.datetime = datetime;
            }
        }
    }
    ```

    这是After的实现代码，我们想要自定义断言就跟着写一个

    ```java
    import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
    import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
    import org.springframework.stereotype.Component;
    import org.springframework.web.server.ServerWebExchange;
    
    import java.util.Collections;
    import java.util.List;
    import java.util.function.Predicate;
    
    @Component
    public class BinBinRoutePredicateFactory extends AbstractRoutePredicateFactory<BinBinRoutePredicateFactory.Config> {
    
        public BinBinRoutePredicateFactory() {
            super(BinBinRoutePredicateFactory.Config.class);
        }
    
        @Override
        public Predicate<ServerWebExchange> apply(Config config) {
            return new GatewayPredicate() {
                @Override
                public boolean test(ServerWebExchange serverWebExchange) {
                    return serverWebExchange.getRequest().getQueryParams().getFirst("username").equals(config.getUsername());
                }
            };
        }
        @Override
        public List<String> shortcutFieldOrder() {
            return Collections.singletonList("username");
        }
    
        public static class Config {
    
            private String username;
    
            public String getUsername() {
                return username;
            }
    
            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
    ```

    ```yml
    server:
      port: 9527
    spring:
      application:
        name: cloud-gateway
      cloud:
        consul:
          host: localhost
          port: 8500
          discovery:
            prefer-ip-address: true
            service-name: ${spring.application.name}
        gateway:
          routes:
            - id: cloud-gateway1 #  路由的ID，没有固定规则但要求唯一，建议配合服务名
    #          uri: http://localhost:8001
              uri: lb://cloud-provider-payment # 指向服务提供者
              predicates:     # 判断条件
                - Path=/pay/gateway/get/**
            - id: cloud-gateway2
    #          uri: http://localhost:8001
              uri: lb://cloud-provider-payment
              predicates:
                - Path=/pay/gateway/info/**
                - BinBin=bin
    ```

    测试地址`http://localhost:9527/pay/gateway/info?username=bin`

#### Filter

##### `GatewayFilter`

###### 请求头过滤器

| 过滤器名称            | 作用                   | 示例                                       |
| --------------------- | ---------------------- | ------------------------------------------ |
| `AddRequestHeader`    | 向请求中添加新的请求头 | `AddRequestHeader=X-Request-Header, value` |
| `SetRequestHeader`    | 设置请求头的值         | `SetRequestHeader=X-Request-Header, value` |
| `RemoveRequestHeader` | 删除请求头             | `RemoveRequestHeader=X-Request-Header`     |

###### 请求参数过滤器

| 过滤器名称               | 作用                     | 示例                             |
| ------------------------ | ------------------------ | -------------------------------- |
| `AddRequestParameter`    | 向请求中添加新的查询参数 | `AddRequestParameter=key, value` |
| `SetRequestParameter`    | 设置查询参数的值         | `SetRequestParameter=key, value` |
| `RemoveRequestParameter` | 删除查询参数             | `RemoveRequestParameter=key`     |

###### 响应头过滤器

| 过滤器名称             | 作用                   | 示例                                         |
| ---------------------- | ---------------------- | -------------------------------------------- |
| `AddResponseHeader`    | 向响应中添加新的响应头 | `AddResponseHeader=X-Response-Header, value` |
| `SetResponseHeader`    | 设置响应头的值         | `SetResponseHeader=X-Response-Header, value` |
| `RemoveResponseHeader` | 删除响应头             | `RemoveResponseHeader=X-Response-Header`     |

###### 其他过滤器

| 过滤器名称           | 作用                 | 示例                            |
| -------------------- | -------------------- | ------------------------------- |
| `StripPrefix`        | 移除请求路径中的前缀 | `StripPrefix=1`                 |
| `PrefixPath`         | 添加路径前缀         | `PrefixPath=/prefix`            |
| `RewritePath`        | 重写请求路径         | `RewritePath=/from/.*$, /to/$1` |
| `RequestRateLimiter` | 限制请求速率         | `RequestRateLimiter=0.1, 5`     |
| `SetPath`            | 设置请求路径         | `SetPath=/new/path`             |
| `SetQueryParameter`  | 设置查询参数         | `SetQueryParameter=key, value`  |
| `SetResponseStatus`  | 设置响应状态码       | `SetResponseStatus=401`         |

###### 示例

下面是一个具体的示例，展示了如何在Spring Cloud Gateway的配置文件中使用这些过滤器。

```yml
1spring:
2  cloud:
3    gateway:
4      routes:
5        - id: user-service
6          uri: lb://user-service
7          predicates:
8            - Path=/users/**
9          filters:
10            - AddRequestHeader=X-Request-Header, value
11            - AddRequestParameter=key, value
12            - AddResponseHeader=X-Response-Header, value
13            - StripPrefix=1
14            - PrefixPath=/api/v1
15            - RewritePath=/users/.*$, /users/$1
16            - RequestRateLimiter=0.1, 5
```

###### 总结

- **请求头过滤器**：这些过滤器可以用来向请求中添加、设置或删除请求头。
- **请求参数过滤器**：这些过滤器可以用来向请求中添加、设置或删除查询参数。
- **响应头过滤器**：这些过滤器可以用来向响应中添加、设置或删除响应头。
- **其他过滤器**：这些过滤器可以用来执行路径重写、请求速率限制等其他操作。

#### `GlobalFilter`全局过滤

当请求与路由匹配时，过滤 Web 处理程序会将 的所有实例`GlobalFilter`和 的所有路由特定实例添加`GatewayFilter`到过滤器链中。此组合过滤器链按接口排序`org.springframework.core.Ordered`，您可以通过实现`getOrder()`方法来设置。

全局过滤器实现全局方法日志

```java
@Component
public class MyFilter implements GlobalFilter, Ordered {
    public static final String BEGIN = "BEGIN-TIME";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(BEGIN, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long beginTime = (Long) exchange.getAttributes().get(BEGIN);
            if (beginTime != null) {
                System.out.println("=================================");
                System.out.println(exchange.getRequest().getURI());
                System.out.println(System.currentTimeMillis() - beginTime + "ms");
                System.out.println("=================================");
            }
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```
