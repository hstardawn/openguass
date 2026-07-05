# 系统运行说明

## 1. 环境要求

- JDK：建议 Java 17，Java 21 也可运行 Spring Boot 3.3
- Maven：建议 3.8+
- 数据库：openGauss / GaussDB
- 浏览器：Chrome、Edge、Safari 均可

## 2. openGauss 连接方式

项目使用 PostgreSQL JDBC 驱动连接 openGauss：

```yaml
spring:
    datasource:
      url: jdbc:postgresql://localhost:15432/huangxx_mis11
      username: gaussdb
      password: Huangxx@123
```

如果 openGauss 端口不是 `5432`，请改成实际端口。

## 3. SQL 执行顺序

```bash
gsql -d postgres -f sql/01_create_database.sql
gsql -d HuangxxMIS11 -f sql/02_create_tables.sql
gsql -d HuangxxMIS11 -f sql/03_insert_test_data.sql
gsql -d HuangxxMIS11 -f sql/04_indexes.sql
gsql -d HuangxxMIS11 -f sql/05_views.sql
gsql -d HuangxxMIS11 -f sql/06_triggers.sql
gsql -d HuangxxMIS11 -f sql/07_procedures.sql
gsql -d HuangxxMIS11 -f sql/08_test_and_verify.sql
```

## 4. 启动命令

```bash
mvn clean spring-boot:run
```

或先打包再运行：

```bash
mvn clean package
java -jar target/huangxx-mis11-1.0.0.jar
```

## 5. 浏览器访问

```text
http://localhost:8080/login
```

## 6. 测试账号

| 角色 | 登录名 | 密码 | 说明 |
|---|---|---|---|
| 管理员 | `admin` | `123456` | 查看管理、统计、日志页面 |
| 教师 | `t001` | `123456` | 数据库系统、计算机网络任课教师 |
| 教师 | `t003` | `123456` | Java Web 任课教师 |
| 学生 | `s2023001` | `123456` | 已有已发布成绩和待处理申诉 |
| 学生 | `s2023011` | `123456` | 可用于演示未发布课程和新增成绩 |

## 7. 页面入口

- 管理员首页：`/admin/index`
- 教师首页：`/teacher/index`
- 学生首页：`/student/index`

未登录访问内部页面会自动跳转到登录页。
