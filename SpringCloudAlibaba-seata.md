---
title:seata
---



### 1. Seata 的背景和诞生原因

- **背景**：
  - 随着微服务架构的普及，分布式系统中的事务处理变得越来越复杂。
  - 单一应用内部的事务管理已经不能满足跨服务间的事务一致性需求。
  - 传统的 XA 两阶段提交协议在网络延迟、性能损耗等方面存在局限性。
- **诞生原因**：
  - 为了解决分布式系统中的事务一致性问题。
  - 提供一种轻量级、高性能的分布式事务解决方案。
  - 支持多种事务模型，满足不同场景的需求。

### 2. Seata 概述

官网：`https://seata.apache.org/zh-cn`

github：`https://github.com/apache/incubator-seata`

#### 2.1 Seata 是什么

- **简介**：
  - Seata 是一款开源的分布式事务解决方案。
  - 它旨在简化分布式环境下事务管理的复杂度。
  - Seata 提供了一个简单、高性能的框架来实现分布式事务。
  - 适用于微服务架构下的事务一致性管理。
- **特点**：
  - 支持多种事务模式，包括 AT 模式、TCC 模式、SAGA 模式、Base 模式等。
  - 可以与多种编程语言和框架集成。
  - 高性能、低延迟，适合大规模分布式系统。
  - 社区活跃，有丰富的文档和支持。

#### 2.2 发展历史

- **起源**：
  - Seata 最初由阿里巴巴集团内部研发。
  - 于 2019 年开源发布。
  - 开源之前，Seata 已经在阿里巴巴内部得到了广泛的应用和验证。
- **版本演进**：
  - **1.x 版本**：最初版本，提供了基础的分布式事务功能。
  - **2.x 版本**：增加了更多特性和优化，提高了稳定性和性能。
  - **最新版本**：持续更新中，不断引入新特性并改进现有功能。
- **社区与支持**：
  - Seata 有一个活跃的社区，支持多种语言和框架。
  - 提供了详尽的文档、教程和示例代码。
  - 社区贡献者遍布全球，支持多语言文档和技术交流。

#### 2.3 术语解释

- **TC (Transaction Coordinator)**：
  - 事务协调器，负责管理和协调全局事务的状态。
  - 包括全局事务的开始、提交、回滚等操作。
  - TC 是 Seata 服务端的核心组件，负责接收来自 TM 的指令，并将指令下发给 RM。
  - 实现了 Seata 服务端的主要逻辑，如事务状态管理、超时处理、心跳检测等。
- **TM (Transaction Manager)**：
  - 事务管理器，负责发起全局事务。
  - TM 是客户端的一部分，它通常位于应用程序中。
  - 当应用程序需要启动一个全局事务时，TM 会向 TC 发送请求，由 TC 来管理整个全局事务的过程。
  - TM 负责与 TC 进行交互，通知 TC 开始、提交或回滚事务。
- **RM (Resource Manager)**：
  - 资源管理器，负责管理分支事务。
  - RM 也是客户端的一部分，通常位于每个服务节点上。
  - RM 负责执行本地事务，并向 TC 报告状态。
  - RM 在事务过程中扮演着重要的角色，它需要与 TC 保持通信，以确保事务的一致性。
- **Global Transaction (GT)**：
  - 全局事务，指的是跨越多个服务边界的大事务。
  - 由 Global Transaction Coordinator (GTC) 管理。
  - GT 是 Seata 中的最高层次事务，它可以包含多个分支事务。
- **Branch Transaction (BT)**：
  - 分支事务，指的是全局事务中的单个服务事务。
  - 由 Global Transaction Participant (GTP) 或 RM 管理。
  - BT 是 GT 的组成部分之一，每个服务节点上的事务都可以看作是 BT。

TC就是作为全部事务的管理者，订单服务是事务的入口，又有本身的服务，所以他既是TM,又是RM，库存是RM，当用户下单的时候，订单业务首先会去调用库存业务，库存业务告诉全局自己要开始库存减少的事务，并生成反向sql来再出错快速回滚数据，当库存减少后订单业务继续创建，创建完成后告诉tc完成了，tc去统一提交事务，否则就执行回滚。这个例子中，订单事务是个大事务，里面包含着库存这个小事务，和栈的思路很像，小事务的完成才能推动大事务的完成

#### 2.4 下载和运行

- **下载**：
  - 访问 Seata 的 GitHub 仓库或官方网站下载最新版本。
  - 提供了可执行的二进制包以及源码。
  - 建议下载官方发布的稳定版本，以获得更好的稳定性和支持。
- **安装**：
  - 解压下载的包到指定目录。
  - 配置环境变量（可选），方便后续的操作。
- **运行**：
  - 启动 Seata 服务端，通常是通过执行 `seata-server.bat` 脚本。
  - 根据需要配置服务端的参数，例如注册中心、存储方式等`\seata\conf\application.yml`。
  - 启动客户端应用，确保正确配置了 Seata 相关的配置项。
  - 启动`localhost:7091`测试 Seata 是否正常工作，可以通过发送事务请求来测试。

#### 2.5 参数配置

- **服务端配置**：

  - 位于 `\seata\conf\application.yml`文件。

  - 包括注册中心类型、存储方式、安全配置等。

  - 示例配置：

    ```yml
    #  Copyright 1999-2019 Seata.io Group.
    #
    #  Licensed under the Apache License, Version 2.0 (the "License");
    #  you may not use this file except in compliance with the License.
    #  You may obtain a copy of the License at
    #
    #  http://www.apache.org/licenses/LICENSE-2.0
    #
    #  Unless required by applicable law or agreed to in writing, software
    #  distributed under the License is distributed on an "AS IS" BASIS,
    #  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    #  See the License for the specific language governing permissions and
    #  limitations under the License.
    
    server:
      port: 7091
    
    spring:
      application:
        name: seata-server
    
    logging:
      config: classpath:logback-spring.xml
      file:
        path: ${log.home:${user.home}/logs/seata}
      extend:
        logstash-appender:
          destination: 127.0.0.1:4560
        kafka-appender:
          bootstrap-servers: 127.0.0.1:9092
          topic: logback_to_logstash
    
    console:
      user:
        username: seata
        password: seata
    seata:
      config:
        # support: nacos, consul, apollo, zk, etcd3
        type: nacos
        nacos:
          server-addr: 127.0.0.1:8848
          namespace:
          group: SEATA_GROUP
          username: nacos
          password: nacos
      registry:
        # support: nacos, eureka, redis, zk, consul, etcd3, sofa
        type: nacos
        nacos:
          application: seata-server
          server-addr: 127.0.0.1:8848
          group: SEATA_GROUP
          namespace:
          cluster: default
          username: nacos
          password: nacos
      store:
        # support: file 、 db 、 redis 、 raft
        mode: db
        db:
          datasource: druid
          db-type: mysql
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/seata?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&rewiteBatchedtatements=true
          username: root
          password: root
          min-conn: 10
          max-conn: 100
          global-table: global_table
          branch-table: branch_table
          lock-table: lock_table
          distributed-lock-table: distributed_lock
          query-limit: 1000
          max-wait: 5000
      #  server:
      #    service-port: 8091 #If not configured, the default is '${server.port} + 1000'
      security:
        secretKey: SeataSecretKey0c382ef121d778043159209298fd40bf3850a017
        tokenValidityInMilliseconds: 1800000
        ignore:
          urls: /,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.jpeg,/**/*.ico,/api/v1/auth/login,/metadata/v1/**
    
    ```

- **客户端配置**：

  - 通常在 Spring Boot 应用的配置文件中配置。

  - 包括事务组、注册中心配置、数据源代理模式等。

  - 示例配置：

    ```yml
    seata:
      registry:
        type: nacos
        nacos:
          server-addr: 127.0.0.1:8848
          namespace: ""
          group: SEATA_GROUP
          application: seata-server
      tx-service-group: default_tx_group
      service:
        vgroup-mapping:
          default_tx_group: default
      data-source-proxy-mode: AT
    logging:
      level:
        io:
          seata: info
    
    ```

#### 2.6 事务模式

- **AT (Automatic Transaction)**：
  - 最常用的事务模式，适用于大多数场景。
  - 通过在本地事务中插入必要的记录来保证事务的一致性。
  - 无需额外编码即可实现，降低了开发难度。
  - 适用于大多数基于 SQL 数据库的场景。
- **TCC (Try-Confirm-Cancel)**：
  - 明确指定事务行为的模式。
  - 需要开发者实现 Try、Confirm 和 Cancel 三个方法。
  - 适用于事务逻辑复杂的场景，例如需要外部系统交互的情况。
  - TCC 模式需要开发者明确控制事务的行为，因此更加灵活但也更为复杂。
- **SAGA**：
  - 一种长活事务模式。
  - 通过一系列的补偿动作来保证最终一致性。
  - 适用于事务持续时间较长的情况，例如订单流程中的支付、发货等环节。
  - SAGA 模式允许事务跨越多个服务，每个服务负责自己的部分，并在失败时执行补偿操作。
- **Base (Branchless Asynchronous Session Execution)**：
  - 一种特殊的事务模式，用于解决传统 AT 模式的性能瓶颈。
  - Base 模式通过异步执行的方式，避免了在本地事务中插入额外的记录。
  - 适用于读多写少的场景，可以显著提高性能。
  - Base 模式通过异步的方式完成全局事务的提交和回滚，减少了对数据库的压力。

###  3. Seata 的使用

#### 3.1 AT模式的工作流程

1. 开始全局事务

当应用程序需要执行一个涉及多个服务的业务操作时，它会通过 TM 向 TC 发起一个全局事务。

2. 分支注册

每个参与该全局事务的服务都需要向 TC 注册自己的分支事务。此时 RM 会记录下这个分支事务的 ID 和状态。

3. 执行业务逻辑

在事务中执行正常的业务逻辑。对于数据库操作来说，这通常意味着执行 SQL 语句。

4. 产生补偿信息

在提交 SQL 之前，SEATA 会在同一个数据库连接中执行一些额外的操作来生成“补偿”SQL（即回滚操作）。这样做的目的是为了保证如果全局事务需要回滚，可以通过这些补偿 SQL 快速地恢复数据到事务开始前的状态。

5. 提交或回滚全局事务

当所有业务逻辑都正常执行完毕后，TM 会决定是提交还是回滚全局事务。

- 如果决定提交，TC 会通知所有 RM 提交各自的分支事务。
- 如果决定回滚，TC 会通知所有 RM 回滚各自的分支事务。

示例

假设我们有一个电商系统，包含两个微服务：订单服务和库存服务。当用户下单时，需要先扣减库存再创建订单。这是一个典型的需要分布式事务的场景。

1. 用户点击购买商品，订单服务作为发起方，通过 TM 启动一个全局事务。
2. 订单服务通知 TC 开始全局事务，并调用库存服务扣减库存。
3. 库存服务接收到请求后，向 TC 注册一个分支事务，并尝试执行扣减库存的 SQL。
4. 在执行扣减库存的 SQL 前，SEATA 会生成一个增加库存的补偿 SQL。
5. 扣减库存成功后，订单服务继续执行创建订单的逻辑。
6. 创建订单成功后，订单服务通过 TM 决定提交全局事务。
7. TM 通知 TC 提交全局事务。
8. TC 通知库存服务和订单服务提交各自分支事务。
9. 最终，库存被正确扣减，订单也被正确创建。

#### 3.2 AT 模式的二阶段提交机制

**第一阶段：准备阶段（Prepare Phase）**

1. **TM 发起全局事务**：
   - 应用程序中的事务管理器（TM）通过 Seata 客户端发起一个全局事务。
   - TM 向事务协调器（TC）发送开始全局事务的消息。
2. **本地事务开始**：
   - 应用程序开始执行本地事务（例如数据库操作）。
   - 在本地事务中，Seata 客户端会在事务开始时捕获当前数据的 beforeimage（事务前的快照）。
3. **生成 beforeimage 和 afterimage**：
   - **beforeimage**：在事务开始时捕获的数据快照，即事务前的数据状态。
   - **afterimage**：在事务结束时捕获的数据快照，即事务后的数据状态。
   - 这两个快照用于生成 undo 日志，即 undo.log 文件。
4. **生成 undo.log**：
   - Seata 客户端在本地事务提交之前，会根据 beforeimage 和 afterimage 生成 undo 日志。
   - undo.log 文件包含了如何撤销事务更改的信息。
   - undo.log 文件保存在本地磁盘上，确保即使应用程序重启也能恢复数据。
5. **本地事务提交**：
   - 本地事务执行完成后，会提交事务。
   - 但是，在提交本地事务的同时，Seata 客户端会向 TC 发送一个分支事务提交请求。
   - 这个请求包含了事务的标识信息，以及 undo.log 文件的位置等元数据。
6. **TC 接收提交请求**：
   - TC 收到提交请求后，将分支事务标记为准备状态（Prepared）。
   - 这意味着该分支事务已经准备好进行全局提交或回滚。

**第二阶段：提交或回滚阶段（Commit or Rollback Phase）**

1. **TM 请求提交全局事务**：
   - 如果所有分支事务都已经准备成功，TM 将向 TC 发送提交全局事务的请求。
2. **TC 执行全局提交**：
   - TC 接收到提交请求后，遍历所有参与事务的分支事务，并向它们发送提交指令。
   - 这一步是实际的提交过程，分支事务将被标记为提交状态（Committed）。
3. **清理 undo.log**：
   - 在全局事务提交成功后，undo.log 文件会被清理掉，因为不再需要它们。

**回滚阶段（Rollback Phase）**

1. **TM 请求回滚全局事务**：
   - 如果在第一阶段中有任何一个分支事务提交失败，或者 TM 决定回滚全局事务，则 TM 向 TC 发送回滚请求。
2. **TC 执行全局回滚**：
   - TC 收到回滚请求后，遍历所有参与事务的分支事务，并向它们发送回滚指令。
   - 分支事务将读取 undo.log 文件，并根据其中的信息撤销事务的更改。
3. **执行 undo 操作**：
   - 分支事务根据 undo.log 文件中的信息撤销数据更改。
   - 这一步是实际的回滚过程，分支事务将被标记为回滚状态（Rolled Back）。
4. **清理 undo.log**：
   - 在全局事务回滚成功后，undo.log 文件同样会被清理掉，因为它们已经完成了使命。

**总结**

AT 模式通过在本地事务中插入必要的记录来保证事务的一致性。在准备阶段，Seata 会捕获 beforeimage 和 afterimage，并生成 undo.log 文件。在提交或回滚阶段，Seata 会根据 undo.log 文件来恢复数据一致性。这种方式简化了分布式事务的实现，并且降低了开发难度。AT 模式适用于大多数基于 SQL 数据库的场景，是 Seata 中最为推荐的事务模式之一。

#### AT模式的使用

1. 打开nacos

2. 在seata的application.yml里面配置注册中心，配置中心，存储信息

   ```yml
   #  Copyright 1999-2019 Seata.io Group.
   #
   #  Licensed under the Apache License, Version 2.0 (the "License");
   #  you may not use this file except in compliance with the License.
   #  You may obtain a copy of the License at
   #
   #  http://www.apache.org/licenses/LICENSE-2.0
   #
   #  Unless required by applicable law or agreed to in writing, software
   #  distributed under the License is distributed on an "AS IS" BASIS,
   #  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   #  See the License for the specific language governing permissions and
   #  limitations under the License.
   
   server:
     port: 7091
   
   spring:
     application:
       name: seata-server
   
   logging:
     config: classpath:logback-spring.xml
     file:
       path: ${log.home:${user.home}/logs/seata}
     extend:
       logstash-appender:
         destination: 127.0.0.1:4560
       kafka-appender:
         bootstrap-servers: 127.0.0.1:9092
         topic: logback_to_logstash
   
   console:
     user:
       username: seata
       password: seata
   seata:
     config:
       # support: nacos, consul, apollo, zk, etcd3
       type: nacos
       nacos:
         server-addr: 127.0.0.1:8848
         namespace:
         group: SEATA_GROUP
         username: nacos
         password: nacos
     registry:
       # support: nacos, eureka, redis, zk, consul, etcd3, sofa
       type: nacos
       nacos:
         application: seata-server
         server-addr: 127.0.0.1:8848
         group: SEATA_GROUP
         namespace:
         cluster: default
         username: nacos
         password: nacos
     store:
       # support: file 、 db 、 redis 、 raft
       mode: db
       db:
         datasource: druid
         db-type: mysql
         driver-class-name: com.mysql.cj.jdbc.Driver
         url: jdbc:mysql://localhost:3306/seata?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&rewiteBatchedtatements=true
         username: root
         password: root
         min-conn: 10
         max-conn: 100
         global-table: global_table
         branch-table: branch_table
         lock-table: lock_table
         distributed-lock-table: distributed_lock
         query-limit: 1000
         max-wait: 5000
     #  server:
     #    service-port: 8091 #If not configured, the default is '${server.port} + 1000'
     security:
       secretKey: SeataSecretKey0c382ef121d778043159209298fd40bf3850a017
       tokenValidityInMilliseconds: 1800000
       ignore:
         urls: /,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.jpeg,/**/*.ico,/api/v1/auth/login,/metadata/v1/**
   
   ```

3. 建立seata的db存储，mysql

   ```sql
   CREATE TABLE IF NOT EXISTS `global_table`
   (
       `xid`                       VARCHAR(128) NOT NULL,
       `transaction_id`            BIGINT,
       `status`                    TINYINT      NOT NULL,
       `application_id`            VARCHAR(32),
       `transaction_service_group` VARCHAR(32),
       `transaction_name`          VARCHAR(128),
       `timeout`                   INT,
       `begin_time`                BIGINT,
       `application_data`          VARCHAR(2000),
       `gmt_create`                DATETIME,
       `gmt_modified`              DATETIME,
       PRIMARY KEY (`xid`),
       KEY `idx_status_gmt_modified` (`status` , `gmt_modified`),
       KEY `idx_transaction_id` (`transaction_id`)
   ) ENGINE = InnoDB
     DEFAULT CHARSET = utf8mb4;
   
   -- the table to store BranchSession data
   CREATE TABLE IF NOT EXISTS `branch_table`
   (
       `branch_id`         BIGINT       NOT NULL,
       `xid`               VARCHAR(128) NOT NULL,
       `transaction_id`    BIGINT,
       `resource_group_id` VARCHAR(32),
       `resource_id`       VARCHAR(256),
       `branch_type`       VARCHAR(8),
       `status`            TINYINT,
       `client_id`         VARCHAR(64),
       `application_data`  VARCHAR(2000),
       `gmt_create`        DATETIME(6),
       `gmt_modified`      DATETIME(6),
       PRIMARY KEY (`branch_id`),
       KEY `idx_xid` (`xid`)
   ) ENGINE = InnoDB
     DEFAULT CHARSET = utf8mb4;
   
   -- the table to store lock data
   CREATE TABLE IF NOT EXISTS `lock_table`
   (
       `row_key`        VARCHAR(128) NOT NULL,
       `xid`            VARCHAR(128),
       `transaction_id` BIGINT,
       `branch_id`      BIGINT       NOT NULL,
       `resource_id`    VARCHAR(256),
       `table_name`     VARCHAR(32),
       `pk`             VARCHAR(36),
       `status`         TINYINT      NOT NULL DEFAULT '0' COMMENT '0:locked ,1:rollbacking',
       `gmt_create`     DATETIME,
       `gmt_modified`   DATETIME,
       PRIMARY KEY (`row_key`),
       KEY `idx_status` (`status`),
       KEY `idx_branch_id` (`branch_id`),
       KEY `idx_xid` (`xid`)
   ) ENGINE = InnoDB
     DEFAULT CHARSET = utf8mb4;
   
   CREATE TABLE IF NOT EXISTS `distributed_lock`
   (
       `lock_key`       CHAR(20) NOT NULL,
       `lock_value`     VARCHAR(20) NOT NULL,
       `expire`         BIGINT,
       primary key (`lock_key`)
   ) ENGINE = InnoDB
     DEFAULT CHARSET = utf8mb4;
   
   INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('AsyncCommitting', ' ', 0);
   INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryCommitting', ' ', 0);
   INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryRollbacking', ' ', 0);
   INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('TxTimeoutCheck', ' ', 0);
   
   
   CREATE TABLE IF NOT EXISTS `vgroup_table`
   (
       `vGroup`    VARCHAR(255),
       `namespace` VARCHAR(255),
       `cluster`   VARCHAR(255),
       primary key (`vGroup`)
   ) ENGINE = InnoDB
     DEFAULT CHARSET = utf8mb4;
   ```

4. 启动seata，并在nacos上检查seata是否成功注册

5. 业务说明：有三个服务，订单，库存，账户，关系如下：用户下单生成订单，在生成订单后要去扣减库存和账户信息。

6. 简历数据库，信息表和undolog表

   ![](.\image\Snipaste_2024-08-16_20-23-07.png)

7. order服务

   - pom

   - 

   - ```xml
     <dependencies>
         <dependency>
             <groupId>org.springframework.cloud</groupId>
             <artifactId>spring-cloud-starter-openfeign</artifactId>
         </dependency>
         <!--nacos服务注册发现-->
         <dependency>
             <groupId>com.alibaba.cloud</groupId>
             <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
         </dependency>
         <!--seata-->
         <dependency>
             <groupId>com.alibaba.cloud</groupId>
             <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
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
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-devtools</artifactId>
             <scope>runtime</scope>
             <optional>true</optional>
         </dependency>
         <dependency>
             <groupId>org.projectlombok</groupId>
             <artifactId>lombok</artifactId>
             <optional>true</optional>
         </dependency>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-test</artifactId>
             <scope>test</scope>
         </dependency>
     
         <dependency>
             <groupId>org.springframework.cloud</groupId>
             <artifactId>spring-cloud-starter-loadbalancer</artifactId>
         </dependency>
         <dependency>
             <groupId>com.alibaba.cloud</groupId>
             <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
         </dependency>
         <dependency>
             <groupId>mysql</groupId>
             <artifactId>mysql-connector-java</artifactId>
         </dependency>
         <dependency>
             <groupId>org.mybatis.spring.boot</groupId>
             <artifactId>mybatis-spring-boot-starter</artifactId>
         </dependency>
         <dependency>
             <groupId>com.alibaba</groupId>
             <artifactId>druid-spring-boot-starter</artifactId>
         </dependency>
         <dependency>
             <groupId>com.atguigu.cloud</groupId>
             <artifactId>cloud-commons</artifactId>
             <version>1.0-SNAPSHOT</version>
         </dependency>
     </dependencies>
     ```

   - yml

     ```yml
     server:
       port: 7777
     
     spring:
       application:
         name: seata-order
       cloud:
         nacos:
           discovery:
             server-addr: localhost:8848
       datasource:
         type: com.alibaba.druid.pool.DruidDataSource
         driver-class-name: com.mysql.cj.jdbc.Driver
         url: jdbc:mysql://localhost:3306/seata_order?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
         username: root
         password: root
     mybatis:
       mapper-locations: classpath:mapper/*.xml
       type-aliases-package: com.atguigu.cloud.pojo
       configuration:
         map-underscore-to-camel-case: true
     
     seata:
       registry:
         type: nacos
         nacos:
           server-addr: 127.0.0.1:8848
           namespace: ""
           group: SEATA_GROUP
           application: seata-server
       tx-service-group: default_tx_group
       service:
         vgroup-mapping:
           default_tx_group: default
       data-source-proxy-mode: AT
     logging:
       level:
         io:
           seata: info
     ```

   - service and controlle

     ```java
     @Service
     @Slf4j
     public class OrderServiceImpl implements OrderService {
         @Resource
         private OrderMapper orderMapper;
         @Resource
         private StorageFeign storageFeign;
         @Resource
         private AccountFeign accountFeign;
         @GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class)
         @Override
         public void create(Order order) {
             String xid = RootContext.getXID();
             log.info("[create] 当前XID: {}", xid);
             order.setStatus(0);
             int result = orderMapper.create(order);
             log.info("[result] = " + result);
             if (result > 0) {
                 Order orderDTO = orderMapper.selectOrder(order);
                 log.info("[orderDTO] = " + orderDTO);
                 // 1. 扣减库存
                 log.info("扣减库存----------------》");
                 storageFeign.decrease(orderDTO.getProductId(), orderDTO.getCount());
                 log.info("扣减库存完成----------------》");
                 // 2. 扣减账户
                 log.info("扣减账户----------------》");
                 accountFeign.decrease(orderDTO.getUserId(), orderDTO.getMoney());
                 log.info("扣减账户完成----------------》");
                 // 3. 修改订单状态
                 orderDTO.setStatus(1);
                 int updateResult = orderMapper.updateOrderStatus(orderDTO);
             }
     
         }
     }
     ```

     ```java
     @RestController
     public class OrderController {
         @Resource
         private OrderService orderService;
         @GetMapping("/order/create")
         public ResultData createOrder(Order order){
             orderService.create(order);
             return ResultData.success(order);
         }
     }
     ```

     feign

     ```java
     @FeignClient(value = "cloud-provider-payment-nacos", fallback =  NacosFeignFallback.class)
     public interface NacosFeign {
         @GetMapping(value = "/pay/nacos/info")
         public ResultData<String> echo();
     }
     ```

     ```java
     @FeignClient("seata-storage")
     public interface StorageFeign {
         @PostMapping("/storage/decrease")
         ResultData decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
     }
     ```

8. storage服务

   pom,yml同上只改动数据库方面就行

   - service

     ```java
     @Service
     @Slf4j
     public class StorageServiceImpl implements StorageService {
         @Resource
         private StorageMapper storageMapper;
     
         @Override
         public void decrease(Long productId, Integer count) {
             log.info("------->storage-service中扣减库存开始");
             storageMapper.decrease(productId, count);
             log.info("------->storage-service中扣减库存结束");
         }
     }
     ```

9. account服务

   - service

     ```java
     @Service
     @Slf4j
     public class AccountServiceImpl implements AccountService {
         @Resource
         private AccountMapper accountMapper;
     
     
         @Override
         public void decrease(Long userId, double money) {
             log.info("------->account-service中扣减账户开始");
             accountMapper.decrease(userId,money);
     //        int i = 10 / 0;
             runtime();
             log.info("------->account-service中扣减账户结束");
         }
         private void runtime(){
             try {
                 Thread.sleep(61000);
             } catch (InterruptedException e) {
                 throw new RuntimeException(e);
             }
         }
     }
     ```

其实关键还是`@GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class)`

这么麻烦的分布式事务用一个注解就完成了，被注解标注的服务就是TM

