---
title:服务调用-OpenFeign
---

## OpenFeign的基础

#### 什么是 OpenFeign？

OpenFeign 是 Netflix Feign 的一个实现，它是一个声明式 HTTP 客户端，使得编写 REST 客户端变得非常简单。它通过注解的方式让开发者能够轻松地定义服务间的远程调用接口，而不需要关心底层的 HTTP 通信细节

#### OpenFeign 的特点：

1. **简洁性**：OpenFeign 允许你通过简单的注解来定义客户端接口。
2. **可读性**：通过接口定义服务调用，提高代码的可读性和可维护性。
3. **易用性**：集成简单，配置少，易于理解和使用。
4. **扩展性**：支持多种编码器和解码器，如 Jackson、Gson 等。
5. **容错机制**：可以很容易地与 Hystrix 或 Resilience4j 等库集成，实现服务调用的熔断、重试等功能。

#### 为什么选择 OpenFeign 而不是 RestTemplate？

1. **声明式 vs. 编程式**：OpenFeign 采用声明式风格定义 HTTP 请求，而 RestTemplate 是基于编程式的方法。这意味着 OpenFeign 的接口定义更加简洁和清晰，而 RestTemplate 需要更多的样板代码来构建请求。
2. **易于集成**：OpenFeign 更容易与 Spring Cloud 生态系统中的其他组件集成，如 Eureka、Hystrix 等。它也更容易与 Spring Boot 项目一起使用，提供了开箱即用的支持。
3. **更好的错误处理**：OpenFeign 提供了更强大的错误处理能力，可以通过异常处理机制来处理各种 HTTP 错误状态。
4. **性能**：虽然两者之间的性能差异不大，但在某些情况下，OpenFeign 可能会因为其设计而具有更好的性能表现。
5. **代码组织**：OpenFeign 使得代码更加模块化，每个服务接口都可以独立定义，这有助于代码的组织和管理。
6. **社区支持**：OpenFeign 作为 Spring Cloud 的一部分，得到了广泛的社区支持和文档资源。

#### 怎么用OpenFeign

具体的信息一定要看官网：`https://docs.spring.io/spring-cloud-openfeign/reference/spring-cloud-openfeign.html#netflix-feign-starter`

1. **导入依赖**

   ```java
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   ```

2. **在需要调用的模块的主启动类上加注解@EnableFeignClients来开启调用功能**

   ```java
   @SpringBootApplication
   @EnableDiscoveryClient
   @EnableFeignClients
   public class MainFeign9002 {
       public static void main(String[] args) {
           SpringApplication.run(MainFeign9002.class,args);
       }
   }
   ```

3. **写OpenFeign的接口和Controller**

   Feign接口

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
   }
   ```

   Controller层

   ```java
   @RestController
   public class OrderController {
       @Resource
       private FeignAPI feignAPI;
   
       @PostMapping ("/feign/pay/add")
       public ResultData addOrder(@RequestBody PayDTO payDTO){
           return feignAPI.addOrder(payDTO);
       }
       @GetMapping("/feign/pay/delete/{id}")
       public ResultData deleteOrder(@PathVariable("id") Long id){
           return feignAPI.deleteOrder(id);
       }
       @GetMapping("/feign/pay/update")
       public ResultData updateOrder(PayDTO payDTO){
           return feignAPI.updateOrder(payDTO);
       }
       @GetMapping("/feign/pay/get/{id}")
       public ResultData getOrder(@PathVariable("id") Long id){
           return feignAPI.getOrder(id);
       }
       @GetMapping("/feign/pay/getall")
       public ResultData listOrder(){
           return feignAPI.listOrder();
       }
       @GetMapping("/feign/getConsulInfo")
       public ResultData getConsulInfo(){
           return feignAPI.getConsulInfo();
       }
   }
   ```

   这个feign接口写出来后可以放在一个功能模块为多个消费者提供调用服务，符合微服务架构的理念。

## OpenFeign高级特性

#### 超时处理

​	在超时处理中，时间的设定长了和短了都不好。OpenFeign的默认超时时间是60s，是不是有点长了，在通常情况下我们都要结合自己的系统去合理的设置超时时间。

1. **两大参数**
   - `connectTimeout`防止因服务器处理时间过长而阻塞呼叫者。
   - `readTimeout`从连接建立时起应用，并在返回响应时间过长时触发。

2. **配置方法**

   - 全局配置：default

   - 局部配置：具体的服务名

     ```yaml
     server:
       port: 9002
     spring:
       application:
         name: cloud-consumer-feign-order
       cloud:
         consul:
           host: localhost
           port: 8500
           discovery:
             service-name: ${spring.application.name}
         openfeign:
           client:
             config:
     #          全局超时设置(写default)
               default:
                 connect-timeout: 4000
                 read-timeout: 4000
     #          局部超时设置(写具体的服务名)
               cloud-provider-payment:
                 connect-timeout: 5000
                 read-timeout: 5000
     ```

     **两个都配置了生效局部的，可以理解成最长匹配（匹配最细粒度的）**

#### 重试处理

​	在feign调用超时后，OpenFeign默认是关闭超时重传的，我们需要自己设置打开

```java
@Configuration
public class OpenFeignConfig {
    @Bean
    public Retryer retryer(){
//        默认关闭
//        return Retryer.NEVER_RETRY;
//        开启（初始重传间隔100ms，最大重传间隔1s，总共尝试传3次）
        return new  Retryer.Default(100, 1, 3);
    }
}
```

#### HttpClient5

##### OpenFeign 为何使用 HttpClient5 而不是原来的 HttpClient4

优势

1. **性能改进**：HttpClient5 在性能方面有所提升，尤其是在高并发场景下，它可以更好地处理大量连接和请求。
2. **资源管理**：HttpClient5 改进了资源管理，比如连接池管理更加高效，可以减少内存泄漏的风险。
3. **TLS 1.3 支持**：HttpClient5 支持最新的 TLS 版本，包括 TLS 1.3，提高了安全性。
4. **API 改进**：HttpClient5 的 API 设计更加现代化和简洁，易于使用。
5. **HTTP/2 支持**：HttpClient5 支持 HTTP/2 协议，可以提高数据传输效率。
6. **依赖项简化**：HttpClient5 的依赖项比 HttpClient4 更加精简，减少了项目中不必要的依赖。

##### 如何使用 HttpClient5

1.  导入依赖

   ```java
   <dependency>
       <groupId>org.apache.httpcomponents.client5</groupId>
       <artifactId>httpclient5</artifactId>
       <version>5.3.1</version>
   </dependency>
   <dependency>
       <groupId>io.github.openfeign</groupId>
       <artifactId>feign-hc5</artifactId>
       <version>13.1</version>
   </dependency>
   ```

2. 开启配置(spring.cloud.openfeign.httpclient.hc5.enable = true)

   ```yml
   spring:
     application:
       name: cloud-consumer-feign-order
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           service-name: ${spring.application.name}
       openfeign:
         client:
           config:
   #          全局超时设置(写default)
             default:
               connect-timeout: 4000
               read-timeout: 4000
   #          局部超时设置(写具体的服务名)
             cloud-provider-payment:
               connect-timeout: 5000
               read-timeout: 5000
         httpclient:
           hc5:
             enabled: true
   ```

#### OpenFeign请求/响应压缩

在微服务架构中，请求响应压缩是一种优化网络传输的有效手段，可以显著减少网络流量并提高服务响应速度。OpenFeign 支持请求响应压缩，可以通过配置来启用这一功能。

可以通过配置文件来开启并使用它

```yaml
spring:
  cloud:
    openfeign:
      compression:
        request:
          # 是否启用压缩
          enabled: true
          # 压缩的MIME类型
          mime-types: 
            - text/xml
            - application/xml
            - application/json
          # 最小请求大小
          min-request-size:  2048
        response:
          # 是否启用压缩
          enabled: true

```

#### Feign 日志

​	为每个创建的Feign客户端创建一个日志记录器。默认情况下，日志记录器的名称是创建Feign客户端时使用的接口的完整类名。Feign日志记录仅响应级别`DEBUG`。为每个创建的Feign客户端创建一个日志记录器。默认情况下，日志记录器的名称是创建Feign客户端时使用的接口的完整类名。Feign日志记录仅响应级别`DEBUG`。

使用步骤：

1. 配置Logger,Level对象

   您可以为每个客户端配置的对象`Logger.Level`，告诉 Feign 要记录多少内容。选项包括：

   - `NONE`, 无日志记录（**默认**）。
   - `BASIC`，仅记录请求方法和URL以及响应状态码和执行时间。
   - `HEADERS`，记录基本信息以及请求和响应标头。
   - `FULL`，记录请求和响应的标头、正文和元数据。

   ```java
   @Bean
   	Logger.Level feignLoggerLevel() {
   		return Logger.Level.FULL;
   	}
   ```

2. 开启日志

   ```yaml
   logging:
     level:
       com:
         atguigu:
           cloud:
             feign:
               FeignAPI: DEBUG
   ```

   格式是 logging.level.(你想要开启哪个feign接口日志的接口全类名).DEBUG

最后呢，nacos配置中心用命名空间，组，dataid来确定唯一的一个配置。
