---
title: 服务链路追踪-Micrometer
---

## 分布式链路追踪技术

分布式链路追踪技术（Distributed Tracing）是一种在分布式系统中跟踪和监控请求流程的方法。这种技术特别适用于现代微服务架构，其中单个用户请求可能会触发多个服务之间的复杂交互。分布式链路追踪的主要目的是帮助开发者理解和诊断这些复杂交互中的性能问题和故障。

### 解决的问题

分布式链路追踪技术主要解决了以下几类问题：

1. **性能瓶颈定位**：在微服务架构中，一个请求可能需要跨越多个服务边界，传统的监控工具往往只能提供单个服务的性能数据。分布式链路追踪能够展示请求在整个系统中的流动路径，从而帮助识别哪些服务或操作导致了延迟。
2. **故障诊断**：当一个请求失败时，确定故障发生的具体位置可能非常困难，尤其是在服务之间存在大量依赖的情况下。链路追踪可以帮助开发者追溯请求的完整路径，并找出导致失败的服务或操作。
3. **服务间交互可视化**：通过可视化工具，开发者可以清楚地看到请求是如何在不同服务之间传递的，这对于理解系统架构和调试是非常有用的。
4. **服务健康状况监测**：除了单次请求的追踪外，链路追踪还可以用来监测长时间内服务的健康状况，例如查看某个服务是否频繁超时或返回错误。

### 原理

##### 基本概念

1. **TraceID（追踪）**：一个 Trace 表示从用户发起请求到收到响应的完整过程。每个 Trace 都有一个全局唯一的 ID。
2. **SpanID（跨度）**：一个 Span 表示 Trace 中的一个子任务，比如一个服务调用另一个服务或者执行数据库查询。每个 Span 也有一个唯一的 ID，并且包含开始时间、结束时间、操作名、标签等信息。
3. **Span 的父子关系**：当一个服务调用另一个服务时，会创建一个paent id 连接父 Span 和一个或多个子 Span。这表示了服务之间的依赖关系。
4. **采样（Sampling）**：由于追踪所有请求可能过于昂贵，因此通常采用采样的方式来决定哪些请求会被追踪。
5. **追踪上下文（Tracing Context）**：在请求传递过程中，需要将追踪信息从客户端传递到服务端，再从服务端传递到下一个服务。这个信息包括 Trace ID、Span ID 和采样决策等。

##### 分布式链路追踪的工作流程

1. **初始化追踪**：客户端发起请求时，会生成一个 Trace ID 和一个初始 Span ID，这标志着一个 Trace 的开始。
2. **传播追踪上下文**：当请求到达第一个服务时，该服务会读取请求头中的追踪上下文信息，并创建一个新的 Span（通常是根 Span）。然后，这个服务会在后续的服务调用中传播这些追踪信息。
3. **创建子 Span**：当服务需要调用另一个服务时，它会创建一个新的 Span，并将其作为当前 Span 的子 Span。新的 Span 会继承父 Span 的 Trace ID，并且包含自己的 Span ID。
4. **记录事件和元数据**：每个 Span 可以记录事件（比如 RPC 调用的开始和结束时间）、错误状态、业务元数据等。
5. **追踪数据上报**：每个服务在处理完请求后，会将 Span 数据上报给追踪存储系统（如 Zipkin 或 Jaeger）。这些系统通常提供了一个界面来查看追踪数据和分析问题。
6. **分析和可视化**：通过可视化工具，可以查看整个请求的执行路径、响应时间和潜在的问题点。

![](.\image\lianlu.drawio.png)

## Micrometer的概述   (数据采集)

#### 1.Spring Cloud Sleuth

Spring Cloud Sleuth 是一个用于 Spring Cloud 应用程序的分布式链路追踪库。它提供了一种简单的方法来追踪和监控微服务架构中的请求流。Sleuth 可以自动为 HTTP 请求、消息和数据库调用生成追踪上下文，并将追踪数据发送到支持的后端（如 Zipkin 或 Jaeger）。

#### 2. 什么是Micrometer

Micrometer 则是一个通用的度量收集库，它专注于收集和报告应用程序中的度量数据。Micrometer 提供了简单而强大的 API 来记录度量，如计数器、定时器、分布统计等，并且可以与多种监控后端集成，如 Prometheus、Graphite、Wavefront 等。

#### 3. Micrometer 能做什么？

1. **指标收集**：Micrometer 提供了一套丰富的 API 来注册和报告各种类型的度量指标，例如计数器（Counter）、直方图（Histogram）、定时器（Timer）等。
2. **监控集成**：支持多种监控后端，包括 Prometheus、Graphite、StatsD、Wavefront 等，使得度量数据可以轻松地与现有的监控系统集成。
3. **自动度量**：对于一些常见的框架和技术栈（如 Spring Boot），Micrometer 提供了自动度量的能力，无需显式编写代码即可收集某些度量数据。
4. **可扩展性**：允许用户通过实现自定义的 MeterRegistry 来扩展 Micrometer，以便支持新的监控后端或度量类型。

#### 4. Micrometer的背景

Spring Cloud Sleuth 的最后一个次要版本是 3.1。您可以查看[3.1.x](https://github.com/spring-cloud/spring-cloud-sleuth/tree/3.1.x)分支以获取最新提交。该项目的核心已移至[Micrometer Tracing](https://micrometer.io/docs/tracing)项目，并且仪表将移至[Micrometer](https://micrometer.io/)和所有相关项目（不再所有仪表都将在单个存储库中完成）。

这是官网的描述，而且sleuth只支持springboot2.x不支持jdk17和springboot3.x, 所以说sleuth已经不再更新，项目已经转移到Micrometer

## ZipKin（图形展示）

Zipkin 是一个开源的分布式追踪系统，主要用于收集和分析微服务架构中的服务间调用数据。通过 Zipkin，开发者可以监控服务之间的依赖关系以及请求在各个服务间的传播路径，这对于调试延迟问题、优化性能以及监控整体系统的健康状况非常重要。

#### Zipkin 能做什么？

1. **请求追踪**：Zipkin 可以追踪每个请求在微服务架构中的传播路径，记录每个服务调用的时间戳和其他元数据。
2. **故障诊断**：当系统出现延迟或故障时，Zipkin 提供的追踪数据可以帮助快速定位问题所在的服务。
3. **性能优化**：通过分析服务间的调用时间和频率，可以发现瓶颈并进行相应的优化。
4. **服务依赖可视化**：Zipkin 可以显示服务之间的依赖关系，有助于理解整个系统的架构布局。

#### Zipkin的下载和启动

下载地址`https://zipkin.io/pages/quickstart.html`

有三种下载方式，如果用jar包就下java就行

启动：java -jar xxx(jar包名称)

Zipkin服务默认访问地址`http://localhost:9411/`

## 整合使用

1. 导入依赖

   全局父工程(6个)

   ```xml
   <micrometer-tracing.version>1.2.0</micrometer-tracing.version>
   <micrometer-observation.version>1.12.0</micrometer-observation.version>
   <feign-micrometer.version>12.5</feign-micrometer.version>
   <zipkin-reporter-brave.version>2.17.0</zipkin-reporter-brave.version>
   ```

   ```xml
   <!-- 导入链路追踪版本中心-->
   <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-tracing-bom</artifactId>
       <version>${micrometer-tracing.version}</version>
       <type>pom</type>
       <scope>import</scope>
   </dependency>
   <!-- 指标追踪-->
   <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-tracing</artifactId>
       <version>${micrometer-tracing.version}</version>
   </dependency>
   <!-- 集成brave，Brave 是一个流行的 Java 分布式追踪库，而 Micrometer Tracing 是 Micrometer 生态系统的一部分，用于处理应用程序中的追踪数据。-->
   <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-tracing-bridge-brave</artifactId>
       <version>${micrometer-tracing.version}</version>
   </dependency>
   <!--这个 JAR 包是 Micrometer 观测 (Observation) 模块的一部分，它提供了用于构建观测（Observation）上下文的工具和 API。观测上下文是一种轻量级的上下文管理机制，用于跟踪和管理应用程序中请求的生命周期。-->
   <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-observation</artifactId>
       <version>${micrometer-observation.version}</version>
   </dependency>
   <!--feign客户端的Micrometer模块-->
   <dependency>
       <groupId>io.github.openfeign</groupId>
       <artifactId>feign-micrometer</artifactId>
       <version>${feign-micrometer.version}</version>
   </dependency>
   <!--把brave的数据报告到zipkin-->
   <dependency>
       <groupId>io.zipkin.reporter2</groupId>
       <artifactId>zipkin-reporter-brave</artifactId>
       <version>${zipkin-reporter-brave.version}</version>
   </dependency>
   ```

   具体的子服务（5个）

   ```xml
   <!-- 指标追踪-->
   <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-tracing</artifactId>
   </dependency>
   <!-- 集成brave，Brave 是一个流行的 Java 分布式追踪库，而 Micrometer Tracing 是 Micrometer 生态系统的一部分，用于处理应用程序中的追踪数据。-->
   <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-tracing-bridge-brave</artifactId>
   </dependency>
   <!--这个 JAR 包是 Micrometer 观测 (Observation) 模块的一部分，它提供了用于构建观测（Observation）上下文的工具和 API。观测上下文是一种轻量级的上下文管理机制，用于跟踪和管理应用程序中请求的生命周期。-->
   <dependency>
       <groupId>io.micrometer</groupId>
       <artifactId>micrometer-observation</artifactId>
   </dependency>
   <!--feign客户端的Micrometer模块-->
   <dependency>
       <groupId>io.github.openfeign</groupId>
       <artifactId>feign-micrometer</artifactId>
   </dependency>
   <!--把brave的数据报告到zipkin-->
   <dependency>
       <groupId>io.zipkin.reporter2</groupId>
       <artifactId>zipkin-reporter-brave</artifactId>
   </dependency>
   ```

2. 写yml

   ```yml
   management:
     zipkin:
       tracing:
         endpoint: http://localhost:9411/api/v2/spans
     tracing:
       sampling:
         probability: 1.0 #采样率默认为0.1(0.1就是10次只能有一次被记录下来)，值越大收集越及时。
   ```

   

3. controller

   生产者：

   ```java
   @RestController
   public class PayMicrometerController {
       @GetMapping("/pay/micrometer/{id}")
       public String myMicrometerTest(@PathVariable("id") Integer id) {
           return "hello! micrometer" + id + "\t" + UUID.randomUUID();
       }
   }
   ```

   feign接口

   ```java
   @GetMapping("/pay/micrometer/{id}")
   public String myMicrometerTest(@PathVariable("id") Integer id);
   ```

   消费者：

   ```java
   @RestController
   public class OrderMicrometerController {
       @Resource
       private FeignAPI feignAPI;
       @GetMapping("/feign/micrometer/{id}")
       public String feignMicrometer(@PathVariable("id") Integer id) {
           return feignAPI.myMicrometerTest(id);
       }
   }
   ```

**效果**

打开`http://localhost:9411/`

点击运行查询

![](.\image\Snipaste_2024-08-09_20-47-32.png)

选择服务

![](.\image\Snipaste_2024-08-09_20-48-45.png)

看到我们写的接口

![](.\image\Snipaste_2024-08-09_20-49-24.png)

show后就看到具体的调用过程了

还有更多相关的功能
