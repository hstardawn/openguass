# 数据库初始化说明

## 1. 创建数据库

执行：

```bash
gsql -d postgres -f sql/01_create_database.sql
```

该脚本创建运行用数据库 `huangxx_mis11`。课程报告中仍可按个性化要求写 `HuangxxMIS11`；openGauss 未加引号的数据库名会转小写，Java 连接使用稳定的小写库名。

## 2. 建表

```bash
gsql -d huangxx_mis11 -f sql/02_create_tables.sql
```

建表脚本包含 15 张表、主键、外键、唯一约束和检查约束。

## 3. 插入测试数据

```bash
gsql -d huangxx_mis11 -f sql/03_insert_test_data.sql
```

测试数据包含地区、专业、班级、20 名学生、6 名教师、8 门课程、3 个学期、10 条教学任务、50 条选课、40 条以上成绩、5 条申诉、系统用户和操作日志。

## 4. 创建索引

```bash
gsql -d huangxx_mis11 -f sql/04_indexes.sql
```

索引用于学生班级、生源地、教学任务、选课、成绩、申诉和日志查询。

## 5. 创建视图

```bash
gsql -d huangxx_mis11 -f sql/05_views.sql
```

视图包括：

- `Huangxx_ViewStudentScore11`
- `Huangxx_ViewTeacherTask11`
- `Huangxx_ViewClassCourse11`
- `Huangxx_ViewCourseStat11`
- `Huangxx_ViewScoreRank11`
- `Huangxx_ViewStudentGpaCredit11`
- `Huangxx_ViewRegionStudentStat11`
- `Huangxx_ViewScoreAppeal11`

## 6. 创建触发器

```bash
gsql -d huangxx_mis11 -f sql/06_triggers.sql
```

触发器实现：

- 成绩自动计算最终成绩、等级、通过标志和绩点。
- 教师只能录入本人教学任务成绩。
- 成绩变化后自动重算学生已修学分和 GPA。
- 成绩修改后写入审计日志。
- 选课前检查教学任务状态和容量。
- 选课变化后维护当前人数。
- 申诉处理后写入操作日志。

## 7. 创建存储过程

```bash
gsql -d huangxx_mis11 -f sql/07_procedures.sql
```

存储过程包括：

- `Huangxx_ProcStudentTranscript11`
- `Huangxx_ProcCourseScoreStat11`
- `Huangxx_ProcClassCreditStat11`
- `Huangxx_ProcRegionStudentStat11`
- `Huangxx_ProcTeacherTaskStat11`
- `Huangxx_ProcStudentYearlyScoreStat11`
- `Huangxx_ProcMajorGpaStat11`
- `Huangxx_ProcAppealStatusStat11`
- `Huangxx_ProcOperationLogStat11`

## 8. 执行验证 SQL

```bash
gsql -d huangxx_mis11 -f sql/08_test_and_verify.sql
```

该脚本验证基础数据、视图、成绩触发器、审计触发器、申诉日志触发器和存储过程调用。
