---
title: 服务的注册与发现 and 分布式配置管理-consul
---
## 什么是consul

### Consul 的背景

​	Consul 是由 HashiCorp 开发的一款开源服务网格工具，旨在解决现代微服务架构中的服务发现和服务间通信问题。HashiCorp 成立于 2012 年，是一家专注于基础设施自动化领域的公司，其产品还包括 Terraform、Vault、Nomad 等。Consul 于 2014 年首次发布，随着微服务架构的兴起，它迅速成为了服务发现领域的重要工具之一。

### cap原则

CAP原则，也称为CAP定理，是分布式系统设计中的一个重要概念。它指出在一个分布式系统中，一致性（Consistency）、可用性（Availability）和分区容错性（Partition tolerance）这三个属性不可能同时达到最优，最多只能同时满足其中的两个。下面我们详细解释这三个属性：

1. **一致性（Consistency）**:
   - **定义**: 在分布式系统中，所有节点在同一时刻的数据都是一致的。当一个节点更新了数据之后，所有的节点都应该立即获得这一更新。
   - **例子**: 如果一个系统是一致的，那么当一个客户端提交了一个更新后，所有后续的读操作都会返回最新的数据值，无论这些读操作发生在哪个节点上。
2. **可用性（Availability）**:
   - **定义**: 在集群中一部分节点故障后，集群整体仍能响应客户端的读写请求。即每个请求不管成功与否都能得到响应。
   - **例子**: 如果一个系统是可用的，那么任何非故障节点在任何时间内都应当能够接受读写请求，并给出合理的响应（即使这个响应是暂时失败或要求重试）。
3. **分区容错性（Partition tolerance）**:
   - **定义**: 即使网络分区发生（部分节点因网络故障而无法相互通信），系统仍然能够正常运作。换句话说，系统应该能够在网络分割的情况下继续提供服务。
   - **例子**: 如果系统具有分区容错性，即使部分节点因为网络问题而无法互相通信，整个系统仍然能够继续运行并处理外部请求。

根据CAP原则，分布式系统的设计需要在这三个属性之间做出权衡。具体来说，分布式系统只能选择以下两种组合之一：

- **CP组合** (Consistency and Partition tolerance):
  - 在出现网络分区的情况下，系统保证数据的一致性，但可能会拒绝服务请求（即降低可用性）。
  - 例子: Zookeeper 就是一个遵循CP原则的例子。当网络分区发生时，Zookeeper 会选择暂停服务以保证数据的一致性。
- **AP组合** (Availability and Partition tolerance):
  - 在出现网络分区的情况下，系统保证可用性，但可能会暂时牺牲一致性。
  - 例子: Nacos 和 Eureka 都是遵循AP原则的例子。它们在遇到网络分区时会继续接受读写请求，但可能返回旧的数据版本。

总结起来，CAP原则强调了分布式系统设计中的权衡问题。在设计系统时，必须明确地选择哪两个属性作为优先级，而放弃第三个属性。这是因为分区容错性被认为是不可避免的，特别是在互联网规模的应用中。因此，大多数实际的分布式系统会在一致性和可用性之间做出权衡。

### 为什么不经常使用eureka而使用consul或者nacos

### Eureka 的局限性

1. **一致性问题**：
   - Eureka 遵循的是 AP（Availability + Partition tolerance）原则，意味着在出现网络分区的情况下，它优先保证系统的可用性而非数据的一致性。对于一些场景来说，这种设计可能会导致数据不一致的问题。
2. **功能单一**：
   - Eureka 主要关注服务发现，虽然它可以很好地完成这项工作，但在功能上较为单一，缺乏其他高级功能，比如配置管理、服务治理等。
3. **维护和支持**：
   - Netflix 已经停止了对 Eureka 的积极维护和发展，这意味着不会有新的功能添加或重要的 bug 修复。这对希望保持技术栈最新状态的企业来说是一个不利因素。
4. **社区和生态系统**：
   - Eureka 的社区相对于其他选项来说可能不够活跃，这可能会影响长期的支持和新功能的开发。

### Nacos 的优势

1. **综合功能**：
   - Nacos 不仅提供了服务发现功能，还包含了配置管理和服务治理的能力。这对于需要一个一站式解决方案的团队来说非常有用。
2. **动态配置管理**：
   - Nacos 提供了强大的动态配置管理能力，允许服务在运行时获取最新的配置更新，而无需重启服务。
3. **活跃的社区和支持**：
   - Nacos 作为一个相对较新的项目，拥有活跃的开发者社区和持续的版本更新，这意味着它可以更好地应对未来的需求变化。
4. **与 Spring Cloud 的集成**：
   - Nacos 与 Spring Cloud 的集成更加紧密和自然，这有助于简化开发过程并提高开发效率。

### Consul 的优势

1. **强一致性**：
   - Consul 遵循 CP（Consistency + Partition tolerance）原则，确保数据的一致性，这对于需要强一致性的应用场景尤为重要。
2. **丰富的功能集**：
   - Consul 不仅仅是一个服务发现工具，还提供了健康检查、秘密管理、KV 存储等功能，适用于多种不同的用例。
3. **多数据中心支持**：
   - Consul 提供了对多数据中心的支持，这对于需要在全球范围内部署服务的应用来说非常重要。
4. **成熟的技术和广泛的采用**：
   - Consul 作为 HashiCorp 的旗舰产品之一，有着成熟的社区支持和广泛的采用率，这意味着有更多的资源和文档可供参考。

## consul的下载

1. 去官网下载`consul.io`
2. 找到install
3. 选择自己的操作系统和想要的版本download

## consul的特性

1. **服务发现**:
   - **自动服务注册**：Consul 允许服务自动注册自己，这样其他服务可以轻易地发现和调用这些服务。
   - **服务查询**：Consul 提供了多种方式来查询已注册的服务，包括 DNS 和 HTTP API。
2. **健康检查**:
   - **服务健康检查**：Consul 可以定期执行健康检查来确认服务实例是否可用。
   - **支持多种检查类型**：包括 TCP 检查、HTTP 检查、TTL 检查等，可以根据服务的具体需求定制检查策略。
   - **自动服务隔离**：如果服务实例不可用，Consul 会自动将其标记为不可用，并从服务列表中移除，避免其他服务尝试与其通信。
3. **键值存储 (Key/Value Storage)**:
   - **配置管理**：Consul 提供了一个简单的键值存储系统，可以用来存储服务配置信息。
   - **动态配置**：服务可以动态地从 Consul 中获取配置信息，支持配置的实时更新。
   - **监听和通知**：当配置发生变化时，Consul 可以发送通知给监听的客户端。
4. **多数据中心支持**:
   - **跨数据中心同步**：Consul 支持多数据中心部署，并能在数据中心之间同步服务注册表和键值存储数据。
   - **灵活的拓扑结构**：Consul 可以适应各种网络拓扑结构，包括私有云、公有云和混合云环境。
5. **分布式一致性**:
   - **Raft 一致性算法**：Consul 使用 Raft 算法来确保数据的一致性和可用性。
   - **强一致性**：在数据写入时，Consul 提供强一致性保证，确保所有节点看到相同的数据版本。
6. **安全性**:
   - **服务间加密通信**：Consul 支持服务间的 mTLS (Mutual TLS) 加密通信，增强服务间通信的安全性。
   - **基于角色的访问控制 (RBAC)**：Consul 提供了 RBAC 来管理对 Consul API 和服务的访问权限。
7. **可扩展性**:
   - **插件化架构**：Consul 具有插件化设计，允许用户开发和部署自定义插件来扩展 Consul 的功能。
   - **代理集成**：Consul 支持内置代理以及与第三方代理（如 Envoy）的集成。
8. **易于使用**:
   - **Web UI**：Consul 提供了一个直观的 Web 用户界面，方便用户管理和监控服务。
   - **简单的安装和部署**：Consul 是一个二进制可执行文件，安装和部署都非常简单。
9. **API 和 CLI**:
   - **丰富的 API**：Consul 提供了一系列 RESTful API 来自动化管理任务。
   - **命令行工具**：Consul 还提供了一个命令行工具，方便手动操作和脚本编写。

## 运行consul

1. 下载完成后进入目录中，会有一个consul.exe文件
2. 在当前目录上cmd 输入命令：`consul --version` 来检查版本
3. 输入命令：`consul agent -dev` 用开发者模式启动
4. 去访问管理界面：`http://localhost:8500`

## 将生产者服务入住注册中心

1. 导依赖

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

2. 写yml

   ```java
   spring:
     # 定义 Spring 应用程序的基础配置
     application:
       # 当前应用程序的名称
       name: cloud-provider-payment
     cloud:
       consul:
         # Consul 服务器的配置
         host: localhost
         # Consul 服务器的端口号
         port: 8500
   ```

3. 在主启动类上加注解：`@EnableDiscoveryClient`来开启服务发现

   <img src=".\image\Snipaste_2024-07-31_17-59-34.png" style="zoom:75%;" />

## 将消费者服务入住注册中心

1. 导依赖

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

2. 改yml

   ```java
   server:
     port: 8002
   spring:
     application:
       name: cloud-consumer-order
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           service-name: ${spring.application.name}
   ```

3. 启动类

   在主启动类上加注解：`@EnableDiscoveryClient`来开启服务发现

4. 获取服务

   暂时用RestTemplate

   - 手动添加组件RestTemplate

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

   - 调用

     ```java
     public static final String url = "http://cloud-provider-payment";
     @Resource
     private RestTemplate restTemplate;
     
     @GetMapping("/consumer/pay/add")
     public ResultData addOrder(PayDTO payDTO){
         return restTemplate.postForObject(url + "/pay/add", payDTO, ResultData.class);
     }
     @GetMapping("/consumer/pay/delete/{id}")
     public ResultData deleteOrder(@PathVariable("id") Long id){
         return restTemplate.getForObject(url + "/pay/delete/" + id, ResultData.class);
     }
     @GetMapping("/consumer/pay/update")
     public ResultData updateOrder(PayDTO payDTO){
         return restTemplate.postForObject(url + "/pay/update", payDTO, ResultData.class);
     }
     @GetMapping("/consumer/pay/get/{id}")
     public ResultData getOrder(@PathVariable("id") Long id){
         return restTemplate.getForObject(url + "/pay/get/" + id, ResultData.class);
     }
     @GetMapping("/consumer/pay/getall")
     public ResultData listOrder(){
         return restTemplate.getForObject(url + "/pay/getall", ResultData.class);
     }
     @GetMapping("/consumer/getConsulInfo")
     public ResultData getConsulInfo(){
         return restTemplate.getForObject(url + "/getConsulInfo", ResultData.class);
     ```

## consul的分布式配置

### 全局配置的意义

​	在集群环境下为整个consul集群提供统一的参数配置，不用一个一个的去修改配置。

### 怎么配置

1. 导入依赖

   ```java
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-consul-config</artifactId>
   </dependency>
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-bootstrap</artifactId>
   </dependency>
   ```

   consul的全局配置是用bootstarp.yml 来代替 application.yml

   - **加载顺序**:
     - `bootstrap.yml` 的配置优先级高于 `application.yml`，并且它在 Spring Boot 启动时首先被加载。
     - `bootstrap.yml` 中的配置不会被外部配置中心覆盖，而 `application.yml` 中的配置会被覆盖。
   - **用途**:
     - `bootstrap.yml` 主要用于配置 Spring Cloud 组件，如 Spring Cloud Config Client 和 Consul 配置中心。
     - `application.yml` 主要用于配置应用本身的设置。

2. 写bootstarp.yml

   ```java
   spring:
     # 定义 Spring 应用程序的基础配置
     application:
       # 当前应用程序的名称
       name: cloud-provider-payment
     cloud:
       consul:
         # Consul 服务器的配置
         host: localhost
         # Consul 服务器的端口号
         port: 8500
         discovery:
           # 服务发现配置
           # 注册到 Consul 的服务名称，这里使用了 ${spring.application.name} 的占位符来引用上面定义的应用名称
           service-name: ${spring.application.name}
         config:
           # Consul 配置中心的配置
           # 分隔符用于区分环境配置文件，例如 application-dev.properties
           profile-separator: '-'
           # 配置文件的格式，这里指定为 YAML 格式
           format: yaml
           watch:
             # 配置文件变更监听相关设置
             # 当 Consul 中的配置文件发生变化时，客户端会等待一定时间（默认单位为秒）来确保没有连续的变化
             # 这里设置为 3 秒
             wait-time: 3
   ```

   本地yml可以用多环境配置来激活全局环境

   ```java
   spring:
     application:
       name: cloud-provider-payment
     profiles:
   	# 激活开发环境
       active: dev 
   ```

3. consul 服务器 key/value 来填写配置

   官网定义的配置文件的格式是这样的

   ```java
   config/testApp,dev/
   config/testApp/
   config/application,dev/
   config/application/		
   ```

   也就是统一要建一个config文件夹，然后再config里就可以建自己环境的文件夹了，例如再springboot里是application-dev，这里是application,dev，把 - 变成了，了，然后才建data文件，再里面写配置。

   注意建文件夹要后面加/ 例如建config文件夹 就是`config/`

   建好大概是这样的

   ![](.\image\Snipaste_2024-07-31_18-35-19.png)

​		然后建data文件 ，后面不加/

​		![](.\image\Snipaste_2024-07-31_18-37-38.png)

取信息就是直接再配置文件中取

```java
@GetMapping("/getConsulInfo")
public ResultData<String> getConsulInfo(@Value("${binbin.info}") String info, @Value("8001") String port){
    return ResultData.success(info + "port : " + port);
}
```

可能就是他会创建一个application-dev.yml之类的文件。也许是这么比较好理解。

### 全局配置的自动刷新

操作十分简单，就是在主启动类上@RefreshScope

```java
spring:
  cloud:
    consul:
      config:
        watch:
          wait-time: 3
```

等待刷新时间默认是55s，可以修改。

### consul配置文件的持久化

我们的consul的配置文件随着服务关闭一起消失，这肯定是不行。

**步骤** ：

1. 首先在consul的安装目录，也就是有consul.exe的那个目录创建一个存放数据的文件夹，还有一个.bat的脚本文件

2. ```shell
   @echo.服务启动......  
   @echo off  
   @sc create Consul binpath= "D:\consul_1.19.1_windows_amd64\consul.exe agent -server -ui -bind=127.0.0.1 -client=0.0.0.0 -bootstrap-expect  1  -data-dir D:\consul_1.19.1_windows_amd64\data"
   @net start Consul
   @sc config Consul start= AUTO  
   @echo.Consul start is OK......success
   @pause
   ```

​		目录需要改成自己的。

​		这个脚本的大致作用就是把consul注册成一个服务在，系统启动的时候自动运行，并操作/data文件夹，这样就可以实现数据的持久化并且不用每次都手动启动。

3. 鼠标右键用管理员命令行运行，如果报错，仔细检查是否路径没有写对，然后在管理员命令行用`sc delete [服务名]`来删除服务