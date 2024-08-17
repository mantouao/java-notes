---
title:Nacos
---

## Nacos是什么

### 什么是Nacos

Nacos 是一个 Alibaba 开源的、易于构建云原生应用的动态服务发现、配置管理和服务管理平台。

使用 Spring Cloud Alibaba Nacos Discovery，可基于 Spring Cloud 的编程模型快速接入 Nacos 服务注册功能。

简单来说：nacos =  服务中心 + 配置中心

官网：`https://nacos.io/`

中文文档：`https://spring-cloud-alibaba-group.github.io/github-pages/2021/zh-cn/index.html`

### 为什么叫Nacos

**Nacos的名称来源于其核心功能：‌动态服务发现、‌配置管理和服务管理。‌**

Nacos的名称由几个部分组成，‌其中“nacos”是Dynamic **Na**ming and **Co**nfiguration **S**ervice的首字母简称。‌这个名称直接反映了Nacos的主要功能和作用。‌

###  Nacos 的作用

- **服务发现**：Nacos 提供了服务实例的自动注册与发现功能，允许微服务应用在运行时自动发现并调用其他服务。
- **健康检查**：Nacos 能够监控服务实例的状态，并能够及时通知客户端服务实例的健康状况变化。
- **动态配置服务**：Nacos 允许以中心化、外部化和动态化的方式管理所有环境的配置，当配置发生改变时，能够实时推送到应用端。
- **动态 DNS 服务**：支持基于 DNS 的服务发现机制。
- **服务及其依赖关系管理**：通过服务元数据管理，了解服务间的依赖关系。
- **非侵入式设计**：Nacos 的客户端SDK是可选的，也可以通过HTTP API进行交互。

### Nacos和其他注册中心的对比

| 特性/产品          | Nacos              | ZooKeeper      | Consul     | Eureka     |
| ------------------ | ------------------ | -------------- | ---------- | ---------- |
| **发布者**         | 阿里巴巴           | Apache         | HashiCorp  | Netflix    |
| **主要用途**       | 服务发现与配置管理 | 分布式协调服务 | 服务网格   | 服务发现   |
| **CAP 定理**       | AP/CP              | AP             | CP         | AP         |
| **数据一致性模型** | 强一致             | 最终一致       | 强一致     | 最终一致   |
| **配置管理**       | 支持               | 不支持         | 支持       | 不支持     |
| **健康检查**       | 内置支持           | 不直接支持     | 支持       | 支持       |
| **API 支持**       | RESTful            | 客户端APIs     | RESTful    | RESTful    |
| **客户端 SDK**     | 多语言支持         | 多语言支持     | 多语言支持 | 多语言支持 |
| **服务注册**       | 动态               | 动态           | 动态       | 动态       |
| **服务发现**       | 动态               | 动态           | 动态       | 动态       |
| **服务权重**       | 支持               | 不支持         | 支持       | 不支持     |
| **服务分组**       | 支持               | 不支持         | 支持       | 支持       |

## Nacos的安装运行

下载地址：`https://nacos.io/download/nacos-server/`

加压后

- Linux/Unix/Mac：`sh startup.sh -m standalone`
- Windows：`startup.cmd -m standalone`
- 启动命令(standalone代表着单机模式运行，非集群模式)

地址： `http://localhost:8848/nacos/index.html`

停止服务

`Ctrl + C / shutdown`

## Nacos怎么使用

#### 生产者：

1. 开启nacos服务

   在windows下nacos/bin路径下 cmd 输入startup.cmd -m standalone

2. 创建项目

   父工程中加

   ```xml
   <dependencyManagement>
           <dependencies>
               <dependency>
                   <groupId>org.springframework.cloud</groupId>
                   <artifactId>spring-cloud-dependencies</artifactId>
                   <version>${spring.cloud.version}</version>
                   <type>pom</type>
                   <scope>import</scope>
               </dependency>
               <dependency>
                   <groupId>com.alibaba.cloud</groupId>
                   <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                   <version>${spring.cloud.alibaba.version}</version>
                   <type>pom</type>
                   <scope>import</scope>
               </dependency>
           </dependencies>
       </dependencyManagement>
   ```

3. 子项目pom

   ```xml
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

4. yml

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
   ```

5. 启动类

   ```java
   @SpringBootApplication
   @EnableDiscoveryClient
   public class Main7001 {
       public static void main(String[] args) {
           SpringApplication.run(Main7001.class, args);
       }
   }
   ```


#### 消费者

和正常的消费者一样，写过一个在写这个就十分轻松

1. 建模块

2. 导入依赖

   依旧导入discovery依赖等相关依赖

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
   
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-loadbalancer</artifactId>
   </dependency>
   
   <dependency>
       <groupId>com.alibaba.cloud</groupId>
       <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
   </dependency>
   ```

3. 写yml

   ```yml
   server:
     port: 7002
   spring:
     application:
       name: cloud-consumer-order-nacos
     cloud:
       nacos:
         discovery:
           server-addr: localhost:8848
   service-url:
     nacos-user-service: http://cloud-provider-payment-nacos
   ```

   这个yml就指明了端口号，注册中心是哪里，还有一个留着调用的服务地址

4. 启动类

   ```java
   @SpringBootApplication
   @EnableDiscoveryClient
   @EnableFeignClients
   public class Main7002 {
       public static void main(String[] args) {
           SpringApplication.run(Main7002.class, args);
       }
   }
   ```

5. controller测试

   ```java
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

   这有openfeign和restTemplate两种，openfeign就需要消费者生产者导入openfeign依赖，然后写feign接口，然后消费者调用就好

#### Nacos作为配置中心

首先最好能对配置中心是什么要有个概念，不能傻乎乎的硬学，不然肯定学的迷迷糊糊

​	配置中心就是为了让微服务都能读取到了配置，就可以用配置中心写一个统一的配置让各个服务区读取并实现统一的动态配置，怎么用呢？逻辑思路如下：首先在nacos或者其他配置中心编写统一的配置，然后在我的目的服务上写一个bootstrap.yml去指定我要去哪个配置中心读文件，在用application,yml文件中配置avtice多环境配置，来选取文件，这样就可以读到文件了。为什么用bootstrap呢？其实就是它的优先级高，他的配置不会被覆盖。

按照这个思路，我们的步骤就十分清晰了

#### 1. 写统一的配置

问题1：在哪写配置？

![](.\image\Snipaste_2024-08-15_15-14-57.png)

问题2：怎么写?

![](.\image\Snipaste_2024-08-15_15-15-56.png)

按照官方的描述，有三个部分，data id, group, 内容，我们学过consul就知道，consul的匹配规则是在/config文件夹下用服务名+ 分割+ 环境名来匹配

nacos是用这三个来匹配，DataId 默认使用 `spring.application.name` 配置跟文件扩展名结合(配置格式默认使用 properties)如果是其他名字则可以用。可以在spring.cloud.nacos.config.prefix改但是格式一直是名字.文件后缀名，GROUP 不配置默认使用 DEFAULT_GROUP

例如我写了binbin.yml

![](.\image\Snipaste_2024-08-15_15-42-35.png)

#### 2. 导入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

这两个是必要依赖，其他根据服务功能加

#### 3.bootstrap.yml,application.yml

```yml
spring:
  application:
    name:  nacos-config-client
  cloud:
    nacos:
      config:
        prefix: binbin
        server-addr: localhost:8848
        file-extension: yml
```

写了基本的配置用prefix和file-extension匹配了我要匹配的配置文件

```yml
server:
  port: 3377
spring:
  application:
    name:  nacos-config-client
  profiles:
    active: dev
```

我的环境要用的是dev就写dev 匹配 prefix-dev.file-extension

#### 4.启动测试

修改好springboot的启动类后，写个controller测试

```java
@RestController
public class ConfigReadController {
    @GetMapping("/config/getConfigInfo")
    public String getConfigInfo(@Value("${binbin.info}") String configValue) {
        return "configValue: " + configValue + "\tport:3377";
    }
}
```

另外nacos自带动态刷新配置，就是我在全局配置改动后，立刻动态刷新
