---
title: LoadBalancer
---

## Ribbon

​	Ribbon 是一个客户端负载均衡库，最初由 Netflix 开发，后来被整合到了 Spring Cloud 中。它允许应用程序在客户端进行服务实例的选择和调用，而无需服务端提供负载均衡支持。这意味着 Ribbon 是嵌入到每个微服务中的，负责选择合适的后端服务实例进行通信。

## LoadBalancer

​	LoadBalancer 这个术语通常指的是服务端负载均衡器，它位于多个服务实例前面，负责将请求分发给后端的服务实例。这种负载均衡器可以是硬件设备，也可以是软件实现，例如 Nginx、HAProxy 或者是云原生环境下的 Kubernetes Service。

## 为什么 LoadBalancer 可能被视为 Ribbon 的替代方案

1. **管理和服务发现**：
   - 在云原生环境中，Kubernetes 等平台提供了强大的服务发现和负载均衡功能，这些功能在很多情况下比手动配置的 Ribbon 更为高效和可靠。
2. **运维复杂性**：
   - 使用外部 LoadBalancer 或 Kubernetes 内置的服务发现和负载均衡能力可以减少应用程序本身的复杂度，因为不再需要在每个服务实例上实现客户端负载均衡逻辑。
3. **性能**：
   - 专业的负载均衡器（如 Nginx 或 Envoy）通常具有更高的性能，并且可以处理更复杂的路由规则和安全需求。
4. **可观察性和监控**：
   - 外部负载均衡器往往提供更丰富的监控指标和日志记录，这有助于故障排查和性能优化。
5. **安全性**：
   - 负载均衡器可以作为安全屏障，提供 SSL/TLS 终止、DDoS 防护等功能。
6. **统一配置**：
   - 使用外部负载均衡器可以实现统一的配置管理，简化部署流程并降低出错的可能性。

## 区别总结

1. **位置**：
   - **Ribbon**：位于客户端，作为服务的一部分运行。
   - **LoadBalancer**：位于服务端，独立于服务之外。
2. **职责**：
   - **Ribbon**：负责在客户端选择服务实例。
   - **LoadBalancer**：负责接收请求并将请求分发到合适的服务实例。
3. **配置和管理**：
   - **Ribbon**：配置较为简单，但需要在每个服务中单独配置。
   - **LoadBalancer**：通常需要专门的配置和管理，但可以集中管理所有服务实例。
4. **性能和功能**：
   - **Ribbon**：轻量级，适合简单的负载均衡需求。
   - **LoadBalancer**：功能丰富，适用于复杂的负载均衡场景。
5. **适用场景**：
   - **Ribbon**：适用于小型项目或对负载均衡要求不高的场景。

## spring-cloud-starter-loadbalancer组件

`spring-cloud-starter-loadbalancer` 是 Spring Cloud 中的一个组件，它为基于 Spring Boot 的应用提供了客户端负载均衡的支持。这个组件旨在简化微服务架构中的负载均衡配置，并与 Spring Web 客户端（如 `RestTemplate` 和 `WebClient`）集成，使得开发人员可以在不编写额外代码的情况下实现负载均衡。

### 主要特点

1. **自动配置**：`spring-cloud-starter-loadbalancer` 提供了自动配置能力，使得开发者无需手动配置负载均衡逻辑。
2. **集成 Spring Web 客户端**：该组件与 `RestTemplate` 和 `WebClient` 集成，允许这些客户端在进行远程调用时自动执行负载均衡。
3. **与服务发现集成**：通常与服务发现组件（如 Eureka 或 Consul）一起使用，从服务注册中心获取服务实例列表，并根据负载均衡策略选择实例。
4. **灵活的负载均衡策略**：支持多种负载均衡算法，例如轮询（Round Robin）、随机选择（Random）等，并允许开发者自定义负载均衡策略。
5. **简洁的配置**：通过 YAML 或 properties 文件即可配置负载均衡相关的设置。

## 客户端负载均衡和服务端负载均衡

### 客户端负载均衡

想象一下，你是一个餐厅的顾客，你想去一家连锁餐厅吃饭。在这种情况下，你（客户端）需要自己决定去哪家分店（服务实例）吃饭。

- **位置**：你自己决定去哪里吃。
- **选择**：你自己挑选要去哪家分店。
- **实现方式**：你根据自己的喜好（如距离最近、人少等）来选择。
- **好处**：你不需要别人帮你做决定，也不需要排队等候。
- **缺点**：你可能需要自己知道所有分店的位置。

### 服务端负载均衡

再想象一下，这次你还是想去那家连锁餐厅吃饭，但是这次你打电话给餐厅预订部门，他们告诉你去哪一家分店。

- **位置**：预订部门（服务端负载均衡器）告诉你去哪家分店。
- **选择**：预订部门帮你选择分店。
- **实现方式**：预订部门根据分店的忙碌程度、距离等因素来为你安排。
- **好处**：你不用担心选错地方，也不用担心去到分店却发现没位子。
- **缺点**：预订部门如果出问题，你就没法吃饭了。

### 总结

- **客户端负载均衡** 就像你自己选择去哪家分店，简单方便，但可能需要自己掌握信息。
- **服务端负载均衡** 就像有人帮你安排去哪家分店，更高级，但依赖于那个帮你安排的人。

## 使用spring-cloud-starter-loadbalancer组件

使用方法也十分的简单，两步走：1.导包，2. 添加`@Loadbalanced`注解

1. 导包

   loadbalancer组件需要

   ```java
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-loadbalancer</artifactId>
     <version>4.1.0</version>
     <scope>compile</scope>
   </dependency>
   ```

​		但是这个包已经集成到consul的哪个注册和发现的包里了

```java
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

2. 在RestTemplate的bean上加注解@LoadBalanced

   ```java
   @Configuration
   public class RestTemplateConfig {
       @Bean
       @LoadBalanced
       public RestTemplate restTemplate() {
           return new RestTemplate();
       }
   }
   ```

## LoadBalancer组件的原理

1. 用RestTemplate发送请求

   ```java
   public static final String url = "http://cloud-provider-payment";
   @Resource
   private RestTemplate restTemplate;
   
   @GetMapping("/consumer/pay/add")
   public ResultData addOrder(PayDTO payDTO){
       return restTemplate.postForObject(url + "/pay/add", payDTO, ResultData.class);
   }
   ```

2. 注册发现：在consul中获取哪些服务，利用discoveryClient组件

   ```java
   @Resource
   private DiscoveryClient discoveryClient;
   ```

   ```java
   List<String> services = discoveryClient.getServices();
   for (String service : services) {
       System.out.println(service);
   }
   ```

2. 注册发现：获取对应服务有哪些实例

   ```java
   List<ServiceInstance> instances = discoveryClient.getInstances("cloud-provider-payment");
   for (ServiceInstance instance : instances) {
       System.out.println(instance.getInstanceId() + "\t" + instance.getHost() + "\t" +  instance.getPort() + "\t" + instance.getUri());
   }
   ```

3. 负载均衡算法：

   轮询（默认）：通过一个rest接口来记录请求次数，重启rest数清零，实际访问实例 = 请求次数 % 总的实例个数。

   随机分配。

4. 选择好实例后拼接成正确的url地址进行访问

## Spring Cloud LoadBalancer 的负载均衡算法的切换

首先从spring.io 官网得知，LoadBalancer组件有两个实现的负载均衡算法`Round-Robin（轮询）`,`Random（随机）` 实现的接口是ReactiveLoadBalancer ,进去可以看到有个choose方法，可以看到有两个实现类，分别就是随机和轮询。

`ReactiveLoadBalancer`默认使用的实现是。`RoundRobinLoadBalancer`要切换到其他实现（针对选定服务或所有服务），可以使用@LoadBalancerClient 来传递配置来切换到随机。

```java
@Configuration
@LoadBalancerClient(name = "cloud-payment-service", configuration = RestTemplateConfig.class)
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(loadBalancerClientFactory
                .getLazyProvider(name, ServiceInstanceListSupplier.class),
                name);
    }
}
```

我们要利用@LoadBalancerClient注解来传递我们的负载均衡客户端配置信息，包括负载均衡客户端的名称和配置类。

接着把ReactorLoadBalancer接口的实现类换成RandomLoadBalancer，应为默认是轮询。这样之后就大功告成了。