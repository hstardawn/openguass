# HuangxxMIS11 高校教务成绩管理系统

本项目是数据库系统课程设计阶段性交付物，题目为“基于 openGauss 的高校教务成绩管理系统设计与实现”。系统使用黄星晓个人命名规则：

- 数据库名：`HuangxxMIS11`
- 表名：`Huangxx_表名11`
- 字段名：`hxx_字段名11`
- 小组号后缀：`11`

本阶段只包含可运行系统、SQL、测试数据、运行说明和演示脚本，不包含课程设计报告正文。

## 技术栈

- Java 17
- Spring Boot 3.3.5
- Thymeleaf
- Spring JDBC / JdbcTemplate
- Maven
- openGauss / GaussDB，使用 PostgreSQL JDBC 驱动连接

## 项目结构

```text
pom.xml
src/main/java/com/huangxx/mis
src/main/resources/templates
src/main/resources/static
sql/01_create_database.sql
sql/02_create_tables.sql
sql/03_insert_test_data.sql
sql/04_indexes.sql
sql/05_views.sql
sql/06_triggers.sql
sql/07_procedures.sql
sql/08_test_and_verify.sql
docs/run_guide.md
docs/database_init.md
docs/demo_script.md
```

## 数据库初始化

按顺序执行：

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

如使用远程 openGauss，请在命令中增加 `-h`、`-p`、`-U` 参数。

## 修改数据库连接

编辑 `src/main/resources/application.yml`：

```yaml
spring:
    datasource:
      url: jdbc:postgresql://localhost:15432/huangxx_mis11
      username: gaussdb
      password: Huangxx@123
```

## 启动系统

```bash
mvn spring-boot:run
```

浏览器访问：

```text
http://localhost:8080/login
```

## 测试账号

| 角色 | 登录名 | 密码 |
|---|---|---|
| 管理员 | `admin` | `123456` |
| 教师 | `t001` | `123456` |
| 教师 | `t002` | `123456` |
| 学生 | `s2023001` | `123456` |
| 学生 | `s2023011` | `123456` |

## 主要功能

- 管理员：学生、教师、课程、专业、班级、教学任务、课程统计、班级学分、生源地统计、成绩排名、成绩审计日志、操作日志。
- 管理员学生管理支持“一键导入”：在 `/admin/students/import` 粘贴 CSV 文本，系统逐行写入 `Huangxx_Student11`。
- 教师：查看本人教学任务、查看选课学生、录入成绩、修改成绩、发布成绩、课程成绩统计、处理成绩申诉。
- 学生：查看个人信息、已选课程、已发布成绩、GPA 和已修学分、提交成绩申诉、查看申诉状态。

## 常见问题

- `mvn: command not found`：本机未安装 Maven。安装 Maven 后再执行 `mvn spring-boot:run`。
- 登录失败：确认已执行全部 SQL，并且 `Huangxx_SystemUser11` 中存在测试账号。
- 数据库连接失败：检查 `application.yml` 中 host、port、数据库名、用户名、密码。OrbStack 容器默认示例为 `localhost:15432/huangxx_mis11`，账号使用非初始用户 `gaussdb`，不要用禁止远程连接的 `omm`。
- 学生看不到成绩：成绩必须发布后才会显示，教师可在 `/teacher/tasks` 点击发布。

## 验收演示建议

按 `docs/demo_script.md` 完成截图链路：管理员查看基础数据和统计，教师录入/修改/发布成绩，学生查看成绩与 GPA 并提交申诉，教师处理申诉，最后查看审计日志和操作日志。
