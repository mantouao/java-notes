# 数据类型

| 数据类型类别      | 数据类型       | 描述                                                                                 | 存储大小            |
| ----------- | ---------- | ---------------------------------------------------------------------------------- | --------------- |
| **整数类型**    | TINYINT    | 1字节。范围：-128 到 127 或 0 到 255（无符号）。                                                  | 1 字节            |
|             | SMALLINT   | 2字节。范围：-32768 到 32767 或 0 到 65535（无符号）。                                            | 2 字节            |
|             | MEDIUMINT  | 3字节。范围：-8388608 到 8388607 或 0 到 16777215（无符号）。                                     | 3 字节            |
|             | INT        | 4字节。范围：-2147483648 到 2147483647 或 0 到 4294967295（无符号）。                             | 4 字节            |
|             | BIGINT     | 8字节。范围：-9223372036854775808 到 9223372036854775807 或 0 到 18446744073709551615（无符号）。 | 8 字节            |
| **浮点数类型**   | FLOAT      | 4字节。单精度浮点数。                                                                        | 4 字节            |
|             | DOUBLE     | 8字节。双精度浮点数。                                                                        | 8 字节            |
| **定点数类型**   | DECIMAL    | 用于存储精确的小数。                                                                         | 依赖于定义           |
| **日期和时间类型** | DATE       | 存储日期值，格式为 'YYYY-MM-DD'。                                                            | 3 字节            |
|             | TIME       | 存储时间值，格式为 'HH:MM<br><br>'。                                                         | 3 字节            |
|             | DATETIME   | 存储日期和时间值，格式为 'YYYY-MM-DD HH:MM<br><br>'。                                           | 8 字节            |
|             | TIMESTAMP  | 存储时间戳，格式为 'YYYY-MM-DD HH:MM<br><br>'。                                              | 4 字节            |
|             | YEAR       | 存储年份值，格式为 'YYYY'。                                                                  | 1 字节            |
| **字符串类型**   | CHAR       | 定长字符串。范围：0 到 255 字符。                                                               | 0-255 字节        |
|             | VARCHAR    | 变长字符串。范围：0 到 65535 字符。                                                             | 0-65535 字节      |
|             | TINYTEXT   | 变长字符串。最大长度为 255 字符。                                                                | 0-255 字节        |
|             | TEXT       | 变长字符串。最大长度为 65535 字符。                                                              | 0-65535 字节      |
|             | MEDIUMTEXT | 变长字符串。最大长度为 16777215 字符。                                                           | 0-16777215 字节   |
|             | LONGTEXT   | 变长字符串。最大长度为 4294967295 字符。                                                         | 0-4294967295 字节 |
| **二进制类型**   | BINARY     | 定长二进制数据。范围：0 到 255 字节。                                                             | 0-255 字节        |
|             | VARBINARY  | 变长二进制数据。范围：0 到 65535 字节。                                                           | 0-65535 字节      |
|             | TINYBLOB   | 变长二进制数据。最大长度为 255 字节。                                                              | 0-255 字节        |
|             | BLOB       | 变长二进制数据。最大长度为 65535 字节。                                                            | 0-65535 字节      |
|             | MEDIUMBLOB | 变长二进制数据。最大长度为 16777215 字节。                                                         | 0-16777215 字节   |
|             | LONGBLOB   | 变长二进制数据。最大长度为 4294967295 字节。                                                       | 0-4294967295 字节 |
| **枚举和集合类型** | ENUM       | 枚举类型。字符串对象，其值必须是预定义的值之一。                                                           | 1-2 字节          |
|             | SET        | 集合类型。字符串对象，可以有零个或多个值，每个值必须是预定义的值之一。                                                | 1-8 字节          |



# SQL语句

### 数据定义语言 (DDL)

DDL 用于定义数据库结构或模式。常见的 DDL 语句包括 `CREATE`、`ALTER` 和 `DROP`。



```sql
--看数据库
SHOW DATABASE
--创建数据库
CREATE DATABASE 数据库名;
--使用数据库
USE 数据库名;
--当前是哪个数据库
SELECT DATABASE();
--删除数据库
DROP DATABASE 数据库名;
-----------------------------------
--看表
SHOW TABLE;
--CREATE: 创建数据库对象（例如表、视图、索引）。
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    BirthDate DATE,
    Position VARCHAR(50)
);
--查看表的字段
DESC 表名;
--看建表语句
SHOW CREATE TABLE 表名;
--修改表
ALTER TABLE 表名 ADD(添加)/MODIFY(修改类型)/CHANGE(修改字段名和类型)/DROP(删除)/RENAME TO(重命名)...;
--删除表
DROP TABLE 表名;


```

###### 数据操作语言 (DML)

DML 用于查询和修改数据。常见的 DML 语句包括 `INSERT`、`UPDATE` 和 `DELETE`。



```sql
-- 添加
INSERT INTO 表名 (字段1，字段2...) VALUES (值1，值2...) [(值1，值2...)]
--修改
UPDATE 表名 SET 字段1 = 值1， 字段2 = 值2 [WHERE 条件]
--删除
DELETE FROM 表名 [WHERE 条件]


```

### 数据库查询语言(DQL)

```sql
5.SELECT  字段列表 [AS] 别名
1.FROM 表名
2.WHERE 过滤条件（>, >=, <, <=, =, <> / !=, like, between...and, in, and, or）
3.GROUP BY 分组字段
4.HAVING 分组过滤条件
6.ORDER BY 排序列  asc(默认，升序) desc(降序)
7.LIMIT 1 / 0 ，1

--聚合函数
COUNT：计算行数。
SELECT COUNT(*) FROM Employees;
SUM：计算数值列的总和。
SELECT SUM(Salary) FROM Employees;
AVG：计算数值列的平均值。
SELECT AVG(Salary) FROM Employees;
MIN：返回数值列的最小值。
SELECT MIN(Salary) FROM Employees;
MAX：返回数值列的最大值。
SELECT MAX(Salary) FROM Employees;
-- sql函数
字符串函数：
--CONCAT：连接两个或多个字符串。
SELECT CONCAT(FirstName, ' ', LastName) AS FullName FROM Employees;
--SUBSTRING：提取字符串的一部分。
SELECT SUBSTRING(FirstName, 1, 3) AS ShortName FROM Employees;
--UPPER：将字符串转换为大写。
SELECT UPPER(FirstName) AS UpperName FROM Employees;
--LOWER：将字符串转换为小写。
SELECT LOWER(FirstName) AS LowerName FROM Employees;

数值函数：
--ROUND：对数值进行四舍五入。
SELECT ROUND(Salary, 2) AS RoundedSalary FROM Employees;
--CEILING：返回大于等于指定数值的最小整数（向上取整）。
SELECT CEILING(Salary) AS CeilingSalary FROM Employees;
--FLOOR：返回小于等于指定数值的最大整数。（向下取整）
SELECT FLOOR(Salary) AS FloorSalary FROM Employees;

日期函数：
--CURDATE：返回当前日期。
SELECT CURDATE() AS Today;
--DATE_ADD：向日期添加指定的时间间隔。
SELECT DATE_ADD(CURDATE(), INTERVAL 7 DAY) AS NextWeek;
--DATEDIFF：计算两个日期之间的差异。
SELECT DATEDIFF(CURDATE(), '2021-01-01') AS DaysPassed;

条件函数：
--IF：返回基于条件的不同值。
SELECT FirstName, IF(Salary > 5000, 'High', 'Low') AS SalaryLevel FROM Employees;



```

### 数据库控制语句(DCL)

```sql
-- 用户管理
--查询用户
USE mysql
SELECT * FROM user
-- 创建用户
CREATE USER '用户名'@'主机名' IDENTIFIED BY '密码'
-- create user '你好'@'localhost' identified by '112233'
-- create user '你好'@'%' identified by '112233'
--修改用户密码
ALTER USER '用户名'@'主机名' IDENTIFIED WITH mysql_native_password BY '密码'
-- alter user '你好'@'%' identified with mysql_native_password by '1234'
-- 删除用户
DROP USER '用户名'@'主机名'
-- drop user '你好'@'%'
-------------------------------------
-- 控制管理
--查询权限
--权限列表 
(all, select, insert, update, delete, drop, alter, create)
SHOW GRANTS for '你好'@'%'
-- 授权
GRANT 权限列表 ON 数据库名.表名 TO '用户名'@'主机名'
-- grant all on *.* to '你好'@'%'
--撤回权限
REVOKE 权限列表 ON 数据库名.表名 TO '用户名'@'主机名'
--revoke all on *.* to '你好'@'%'


```

### 约束

**非空约束：NOT NULL**

```sql
CREATE TABLE Employees (
    EmployeeID INT NOT NULL,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    HireDate DATE NOT NULL
);

```

**唯一约束：UNIQE**

```sql
CREATE TABLE Employees (
    EmployeeID INT NOT NULL,
    Email VARCHAR(100) UNIQUE,
    FirstName VARCHAR(50),
    LastName VARCHAR(50)
);

```

**主键约束：PRIMARY KEY (自增：AUTO_INCREMENT)**

```sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50),
    LastName VARCHAR(50)
);

```

**默认约束：DEFULT**

```sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    Status VARCHAR(10) DEFAULT 'Active'
);

```

**检查约束：CHECK**

```sql
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    Age INT CHECK (Age >= 18)
);

```

**外键约束：FOREIGN key** 

```sql
CREATE TABLE Departments (
    DepartmentID INT PRIMARY KEY AUTO_INCREMENT,
    DepartmentName VARCHAR(50)
);

CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    DepartmentID INT,
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID)
);

```

### 事务的定义

事务（Transaction）是一个逻辑上的工作单元，由一组操作组成，这些操作要么全部执行成功，要么全部执行失败。事务保证了数据库的完整性和一致性。

#### 检查是否自动提交

MySQL默认是自动提交模式。在自动提交模式下，每个独立的SQL语句被当作一个事务自动提交。你可以通过以下命令检查当前的自动提交模式：

```sql
SELECT @@autocommit;

```

返回结果为1表示自动提交模式开启，返回0表示关闭。

#### 设置不自动提交

你可以通过以下命令将自动提交模式关闭：

```sql
SET autocommit = 0;

```

### 事务的操作

- **BEGIN**: 开始一个事务。
- **COMMIT**: 提交事务，使所有对数据库的修改成为永久的。
- **ROLLBACK**: 回滚事务，撤销自上次提交以来所有对数据库的修改。

#### 示例

```sql
BEGIN;
-- 事务操作
INSERT INTO accounts (user_id, balance) VALUES (1, 1000);
UPDATE accounts SET balance = balance - 100 WHERE user_id = 1;
UPDATE accounts SET balance = balance + 100 WHERE user_id = 2;
-- 提交事务
COMMIT;
-- 若出现错误，可以使用 ROLLBACK 撤销操作
ROLLBACK;

```

### 事务的四大特性 (ACID)

1. **原子性 (Atomicity)**: 事务是一个不可分割的工作单位，事务中的操作要么全部成功，要么全部失败。
2. **一致性 (Consistency)**: 事务执行前后，数据库都保持一致的状态。
3. **隔离性 (Isolation)**: 并发执行的事务相互隔离，避免相互影响。
4. **持久性 (Durability)**: 事务一旦提交，其结果是永久性的，即使系统崩溃也不会丢失。

### 并发事务问题及隔离级别

| 隔离级别                    | 脏读  | 不可重复读 | 幻读  | 解决的问题       |
| ----------------------- | --- | ----- | --- | ----------- |
| 读未提交 (Read Uncommitted) | 是   | 是     | 是   | 无           |
| 读已提交 (Read Committed)   | 否   | 是     | 是   | 脏读          |
| 可重复读 (Repeatable Read)  | 否   | 否     | 是   | 脏读，不可重复读    |
| 串行化 (Serializable)      | 否   | 否     | 否   | 脏读，不可重复读，幻读 |

#### Redo Log

Redo Log（重做日志）用于记录事务的修改操作，保证持久性。事务提交后，先写入Redo Log，再更新数据库。

#### Undo Log

Undo Log（撤销日志）用于记录事务的撤销操作，保证原子性和一致性。事务回滚时，根据Undo Log撤销操作。

### MVCC (Multi-Version Concurrency Control)

#### 基本概念

MVCC，即多版本并发控制，是一种用于提高数据库并发性能的方法。通过维护数据的多个版本，避免读写冲突，提高数据库的读性能。

- **解决的问题**: 提高并发性能，避免读写冲突。
- **为什么用MVCC**: 提高读性能，减少锁争用。

#### 隐藏字段

MVCC 在每行记录中添加两个隐藏字段：

- **trx_id**: 记录创建或最近一次更新该行的事务ID。
- **roll_pointer**: 指向该行的前一个版本的指针。

可以通过查询`information_schema`库中的`innodb_trx`表来查看这些隐藏字段。

#### 版本链

每行记录通过`roll_pointer`字段形成一个版本链，新版本指向旧版本，形成一个链表。这样，读取操作可以找到合适的版本进行读取。

#### Read View

Read View 是MVCC实现的一部分，用于确定当前事务可以看到哪些版本的数据。在事务启动时生成一个Read View，包含所有当前活跃事务的列表，决定了哪些版本的数据对当前事务可见。

#### MVCC控制级别

- **RC级别 (Read Committed)**: 只要数据被提交，其他事务就可以读取。这意味着在同一个事务中，同一个查询可能会看到不同的结果。
- **RR级别 (Repeatable Read)**: 事务开始时创建一个Read View，事务结束前，该事务只能看到事务开始时已经提交的版本，避免了不可重复读的问题。

### MVCC 控制并发的流程

#### 插入、更新和删除操作

- **插入（Insert）**: 插入操作为数据行创建一个新版本，`trx_id`设为当前事务ID，`roll_pointer`为NULL，因为这是第一个版本。
- **更新（Update）**: 更新操作创建一个新版本，`trx_id`设为当前事务ID，`roll_pointer`指向前一个版本。
- **删除（Delete）**: 删除操作创建一个新版本，标记该版本已删除，`trx_id`设为当前事务ID，`roll_pointer`指向前一个版本。

#### 读取操作

- **快照读（Snapshot Read）**: 在可重复读（Repeatable Read，RR）和读已提交（Read Committed，RC）隔离级别下，使用快照读。快照读根据Read View和版本链来确定事务可见的数据版本。
- **当前读（Current Read）**: 在读未提交（Read Uncommitted，RU）隔离级别下，读取最新版本的数据。

### MVCC 工作原理示例

假设有如下表结构：

`CREATE TABLE accounts (   id INT PRIMARY KEY,   balance DECIMAL(10,2) );`

插入初始数据：

`INSERT INTO accounts (id, balance) VALUES (1, 1000), (2, 1500);`

#### 事务示例

##### 场景1：事务A和事务B并发更新同一行数据

1. **事务A开始**：
   
   `BEGIN;`

2. **事务B开始**：
   
   `BEGIN;`

3. **事务A更新数据**：
   
   `UPDATE accounts SET balance = 1100 WHERE id = 1;`
   
   - 创建新版本，`trx_id`为事务A的ID，`roll_pointer`指向旧版本。
   - 版本链：
     - 版本2: balance = 1100, `trx_id` = A
     - 版本1: balance = 1000, `trx_id` = NULL

4. **事务B更新数据**：
   
   `UPDATE accounts SET balance = 1200 WHERE id = 1;`
   
   - 创建新版本，`trx_id`为事务B的ID，`roll_pointer`指向事务A的版本。
   - 版本链：
     - 版本3: balance = 1200, `trx_id` = B
     - 版本2: balance = 1100, `trx_id` = A
     - 版本1: balance = 1000, `trx_id` = NULL

5. **事务A读取数据**：
   
   `SELECT balance FROM accounts WHERE id = 1;`
   
   - 根据Read View和版本链，事务A只能看到事务A开始之前提交的版本。
   - 事务A的Read View: `trx_id` = A，看到版本1（balance = 1000）。

6. **事务A提交**：
   
   `COMMIT;`

7. **事务B读取数据**：
   
   `SELECT balance FROM accounts WHERE id = 1;`
   
   - 根据Read View和版本链，事务B只能看到事务B开始之前提交的版本。
   - 事务B的Read View: `trx_id` = B，看到版本2（balance = 1100）。

8. **事务B提交**：
   
   `COMMIT;`

#### 场景2：事务C和事务D并发读取和更新同一行数据

1. **事务C开始**：
   
   `BEGIN;`

2. **事务D开始**：
   
   `BEGIN;`

3. **事务D读取数据**：
   
   `SELECT balance FROM accounts WHERE id = 1;`
   
   - 根据Read View和版本链，事务D看到最新提交的版本（balance = 1200）。

4. **事务C读取数据**：
   
   `SELECT balance FROM accounts WHERE id = 1;`
   
   - 根据Read View和版本链，事务C看到最新提交的版本（balance = 1200）。

5. **事务D更新数据**：
   
   `UPDATE accounts SET balance = 1300 WHERE id = 1;`
   
   - 创建新版本，`trx_id`为事务D的ID，`roll_pointer`指向旧版本。
   - 版本链：
     - 版本4: balance = 1300, `trx_id` = D
     - 版本3: balance = 1200, `trx_id` = B
     - 版本2: balance = 1100, `trx_id` = A
     - 版本1: balance = 1000, `trx_id` = NULL

6. **事务D提交**：
   
   `COMMIT;`

7. **事务C读取数据**：
   
   `SELECT balance FROM accounts WHERE id = 1;`
   
   - 根据Read View和版本链，事务C看到最新提交的版本（balance = 1200）。

8. **事务C更新数据**：
   
   `UPDATE accounts SET balance = 1400 WHERE id = 1;`
   
   - 创建新版本，`trx_id`为事务C的ID，`roll_pointer`指向旧版本。
   - 版本链：
     - 版本5: balance = 1400, `trx_id` = C
     - 版本4: balance = 1300, `trx_id` = D
     - 版本3: balance = 1200, `trx_id` = B
     - 版本2: balance = 1100, `trx_id` = A
     - 版本1: balance = 1000, `trx_id` = NULL

9. **事务C提交**：
   
   `COMMIT;`

### redo log 和 undo log 与 MVCC 的联动

1. **更新数据**：
   
   - 事务对数据进行修改时，旧版本会记录在undo log中，新的版本会写入数据页，同时更新`trx_id`和`roll_pointer`。
   - redo log记录修改操作，以便在崩溃恢复时重做这些修改。

2. **读取数据**：
   
   - 读取操作根据事务的Read View和版本链，利用undo log找到合适的历史版本，实现快照读。

# 锁

### 全局锁

#### 是什么？

全局锁是对整个MySQL实例加锁，阻塞所有写操作，只允许读操作。获取全局锁后，其他会话无法修改任何表。

#### 怎么用？

通过 `FLUSH TABLES WITH READ LOCK` 命令来获取全局锁。这对于需要确保备份数据的一致性非常有用。

#### 使用场景和示例：

- **场景：** 备份整个数据库。

- **示例：**
  
  `-- 获取全局锁 FLUSH TABLES WITH READ LOCK;  -- 执行备份操作  -- 释放锁 UNLOCK TABLES;`

### 表级锁

#### 表锁

##### 是什么？

表级锁是对整张表加锁，可以是读锁（共享锁）或写锁（排他锁），影响整张表的读写操作。

##### 怎么用？

通过 `LOCK TABLES ... WRITE` 或 `LOCK TABLES ... READ` 命令获取写锁或读锁。

##### 使用场景和示例：

- **场景：** 需要对整个表进行大量数据的操作或维护。

- **示例：**
  
  `-- 获取写锁 LOCK TABLES my_table WRITE;  -- 执行表的写操作  -- 释放锁 UNLOCK TABLES;`

#### 元数据锁

##### 是什么？

元数据锁是保护表结构的锁，用于防止并发DDL操作造成的异常情况。

##### 怎么用？

MySQL 自动管理，不需要显式获取或释放。

##### 使用场景和示例：

- **场景：** 在执行 `ALTER TABLE` 或 `DROP TABLE` 等操作时，需要保护表结构的一致性。
- **示例：** MySQL 在执行DDL操作时自动添加元数据锁。

#### 意向锁

##### 是什么？

意向锁是表锁的辅助锁，用于指示事务将在更细粒度的层次上设置锁定。

##### 怎么用？

MySQL 自动管理，不需要显式获取或释放。

##### 使用场景和示例：

- **场景：** 多个事务需要操作不同表时，通过意向锁提前通知其他事务可能涉及的表。
- **示例：** MySQL 在事务开始时自动设置适当的意向锁。

### 行级锁

#### 行锁

##### 是什么？

行级锁是对表中的行数据进行锁定，允许其他事务并发地访问表的其他行。

##### 怎么用？

MySQL 根据具体的SQL语句自动判断是否需要行级锁。可以通过 `SELECT ... FOR UPDATE` 或 `SELECT ... LOCK IN SHARE MODE` 显式获取行锁。

##### 使用场景和示例：

- **场景：** 需要确保某些行数据的独占性操作。

- **示例：**
  
  `-- 获取行锁 START TRANSACTION; SELECT * FROM my_table WHERE id = 1 FOR UPDATE;  -- 执行更新操作  COMMIT;`

#### 间隙锁

##### 是什么？

间隙锁是行锁的一种补充，用于锁定一个范围，而不是具体的行，防止其他事务在锁范围内插入新数据。

##### 怎么用？

MySQL 自动管理，通常在使用范围条件时会自动添加间隙锁。

##### 使用场景和示例：

- **场景：** 在处理范围查询时，需要防止其他事务在范围内插入新数据。

- **示例：**
  
  `START TRANSACTION; SELECT * FROM my_table WHERE id >= 10 AND id <= 20 FOR UPDATE;  -- 执行相关操作  COMMIT;`

#### 临键锁

##### 是什么？

临键锁是行级锁的一种特殊形式，用于锁定不存在的行，防止其他事务在相同条件下插入相同的数据。

##### 怎么用？

MySQL 自动管理，在插入数据时如果有唯一索引或主键冲突时会自动添加临键锁。

##### 使用场景和示例：

- **场景：** 插入新数据前需要确保唯一性。

- **示例：**
  
  `START TRANSACTION; INSERT INTO my_table (id, name) VALUES (1, 'John') ON DUPLICATE KEY UPDATE name = VALUES(name);  -- 执行相关操作  COMMIT;`



# InnoDB 索引

#### 聚簇索引（Clustered Index）

- **定义**：聚簇索引决定了数据在磁盘上的存储顺序。每个表有且只有一个聚簇索引，通常是主键。

- **特点**：叶子节点包含了整行数据。

- **创建**：在创建表时定义主键，自动创建聚簇索引。
  
  `CREATE TABLE example (     id INT PRIMARY KEY,     name VARCHAR(50) );`

#### 二级索引（Secondary Index）

- **定义**：非聚簇索引，用于加速除主键以外的查询。

- **特点**：叶子节点存储索引列和主键值。

- **创建**：
  
  `CREATE INDEX idx_name ON example(name);`

#### 唯一索引（Unique Index）

- **定义**：保证索引列中的值唯一。

- **特点**：相同值无法重复插入。

- **创建**：
  
  `CREATE UNIQUE INDEX idx_unique_name ON example(name);`

#### 联合索引（Composite Index）

- **定义**：包含多个列的索引，用于加速多个列的组合查询。

- **特点**：根据多个列的组合值进行索引。

- **创建**：
  
  `CREATE INDEX idx_composite ON example(name, id);`

#### 覆盖索引（Covering Index）

- **定义**：查询可以只通过索引满足，不需要回表。

- **特点**：查询的所有列都在索引中。

- **示例**：
  
  `SELECT name FROM example WHERE name = 'John';`

#### 前缀索引（Prefix Index）

- **定义**：索引列的前缀部分，用于字符串列。

- **特点**：节省空间，但可能增加查询成本。

- **创建**：
  
  `CREATE INDEX idx_prefix ON example(name(10));`

#### 删除索引

- **删除**：
  
  `DROP INDEX idx_name ON example;`

#### 查询索引

- **查询索引**：
  
  `SHOW INDEX FROM example;`

### InnoDB 和 MyISAM 的区别

#### 存储结构

- **InnoDB**：支持事务，行级锁定，支持外键。数据存储在主索引（聚簇索引）中，所有二级索引引用主键。
- **MyISAM**：不支持事务，表级锁定，不支持外键。数据和索引分开存储，数据存储在文件中，索引存储在B树中。

#### 索引结构

- **InnoDB**：B+树结构，主索引和二级索引。
- **MyISAM**：B+树结构，但不支持聚簇索引。

#### 事务和锁定

- **InnoDB**：支持ACID事务，行级锁。
- **MyISAM**：不支持事务，表级锁。

### 索引结构

#### B树

- **特点**：多路自平衡树，节点包含多个子节点和关键字。
- **应用**：不常用于现代数据库索引。

#### B+树

- **特点**：所有关键字存储在叶子节点，内部节点仅用于索引。叶子节点通过链表相连。
- **应用**：广泛用于数据库索引，适合范围查询。

#### 哈希索引

- **特点**：基于哈希表，适合等值查询，不适合范围查询。
- **应用**：MySQL中的Memory存储引擎。

### 最左匹配原则

- **定义**：联合索引从最左边列开始匹配查询条件，依次匹配。

- **示例**：
  
  `CREATE INDEX idx_composite ON example(a, b, c);`
  
  - `WHERE a = 1 AND b = 2 AND c = 3`：完全匹配。
  - `WHERE a = 1 AND b = 2`：匹配前两列。
  - `WHERE b = 2 AND c = 3`：不匹配，索引失效。

### 索引失效场景

#### 1. 范围查询后的列不再使用索引

- 当查询条件中有范围查询（如 `<`, `>`, `BETWEEN`, `LIKE`），范围查询后的列索引失效。
  
  `WHERE a = 1 AND b > 2 AND c = 3 -- c 列索引失效`

#### 2. 对索引列进行函数操作

- 在 WHERE 子句中对索引列使用函数会导致索引失效。
  
  `WHERE UPPER(name) = 'JOHN' -- name 列索引失效`

#### 3. 对索引列进行隐式类型转换

- MySQL 可能会进行隐式类型转换，从而导致索引失效。
  
  `WHERE name = 123 -- name 列索引失效（假设 name 是字符串类型）`

#### 4. 使用不等于操作符（<> 或 !=）

- 不等于操作符通常会导致索引失效。
  
  `WHERE name != 'John' -- name 列索引失效`

#### 5. 使用 IS NULL 或 IS NOT NULL

- 索引列上的 IS NULL 或 IS NOT NULL 查询可能导致索引失效。
  
  `WHERE name IS NULL -- name 列索引失效`

#### 6. 使用 OR 条件

- 如果 OR 条件中有未使用索引的列，则可能导致索引失效。
  
  `WHERE name = 'John' OR age = 25 -- 可能导致索引失效`

#### 7. 字符串不带引号

- 对字符串类型的索引列查询时，未用引号包裹会导致索引失效。
  
  `WHERE name = 123 -- name 列索引失效（假设 name 是字符串类型）`

### EXPLAIN 分析查询

使用 EXPLAIN 可以查看 MySQL 如何执行查询，输出的每一列含义如下：

#### 输出列说明

1. **id**：查询的序列号，包含一组数字，表示查询中执行 select 子句或操作表的顺序。
2. **select_type**：查询类型，显示查询是简单查询、联合查询或子查询等。
   - `SIMPLE`：简单查询，不包含子查询或 UNION。
   - `PRIMARY`：最外层的查询。
   - `UNION`：UNION 中的第二个或后面的查询。
   - `SUBQUERY`：子查询中的第一个 SELECT。
3. **table**：显示查询操作的表。
4. **partitions**：匹配的分区信息。
5. **type**：访问类型，显示 MySQL 选择使用的索引类型。性能由好到坏依次是：
   - `system`：表只有一行（等于系统表）。
   - `const`：表最多有一个匹配行，主键或唯一索引。
   - `eq_ref`：唯一性索引扫描，对于每个索引键，表中有一行匹配。
   - `ref`：非唯一性索引扫描，返回匹配某个单独值的所有行。
   - `range`：索引范围扫描。
   - `index`：全索引扫描。
   - `ALL`：全表扫描。
6. **possible_keys**：查询可能使用的索引。
7. **key**：查询实际使用的索引。
8. **key_len**：索引的长度（字节数）。
9. **ref**：列和索引的比较。
10. **rows**：MySQL 估算找到所需行的数量。
11. **filtered**：显示查询条件的过滤效果。
12. **Extra**：额外信息，显示 MySQL 在查询过程中做的额外操作。
    - `Using where`：需要再进一步通过条件过滤。
    - `Using index`：查询使用覆盖索引（索引包含查询的所有列）。
    - `Using filesort`：MySQL 需要额外排序。
    - `Using temporary`：MySQL 需要创建临时表存储结果。

### 页、区、段的概念

#### 页（Page）

- **定义**：最小的存储单元，通常为 16KB。
- **作用**：存储表的数据和索引，读取和写入以页为单位。
- **类型**：数据页、索引页、undo 页、系统页等。

#### 区（Extent）

- **定义**：由多个连续的页组成，通常为 1MB 大小（64 个页）。
- **作用**：提高 I/O 性能，减少磁盘碎片。

#### 段（Segment）

- **定义**：由多个区组成，用于管理表的数据和索引。
- **作用**：管理和组织数据存储结构。

### MySQL 如何减少磁盘 I/O

1. **缓冲池**：InnoDB 的缓冲池缓存数据页和索引页，减少对磁盘的直接读写。
   
   - **配置**：`innodb_buffer_pool_size` 参数设置缓冲池大小。

2. **预读机制**：通过预读区（Extent），提前加载数据，提高 I/O 性能。

3. **双写缓冲区**：InnoDB 双写缓冲区减少磁盘写入失败风险，保障数据完整性。

4. **日志文件**：使用日志文件（如 redo log 和 undo log）记录数据变更，减少频繁的磁盘写入。
