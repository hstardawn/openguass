# HuangxxMIS11 Design

## Goal
Build a runnable Java Web course-design project named "基于 openGauss 的高校教务成绩管理系统设计与实现" for 黄星晓, group 11. The deliverable includes openGauss SQL, Spring Boot + Thymeleaf source code, test data, run instructions, database initialization notes, and a demo script. It does not include the final course-design report正文.

## Requirements Source
The implementation follows the course assignment Word documents, the supplementary frontend/backend requirements, and the enhanced schema markdown. When sources differ, the explicit personalized requirements win:

- Database: `HuangxxMIS11`
- Table names: `Huangxx_*11`
- Field names: `hxx_*11`
- Roles: 管理员、教师、学生
- Required stack: Java, Spring Boot, Thymeleaf, Maven, openGauss-compatible JDBC

## Architecture
Use a single Spring Boot MVC application. Controllers return Thymeleaf pages and use session data to identify the logged-in user. A Spring MVC interceptor blocks unauthenticated access and prevents cross-role page access. `JdbcTemplate` repositories query the openGauss schema directly, keeping the project easy to run and inspect during答辩.

The database contains the main business integrity: primary keys, foreign keys, unique constraints, check constraints, indexes, views, trigger functions, triggers, and stored procedures. Java performs user workflow validation and calls SQL that lets the database calculate final score, grade, pass flag, GPA, credit totals, audit logs, operation logs, and statistics.

## Database Model
Create 15 tables:

- `Huangxx_Region11`
- `Huangxx_Major11`
- `Huangxx_Class11`
- `Huangxx_Student11`
- `Huangxx_Teacher11`
- `Huangxx_Course11`
- `Huangxx_Term11`
- `Huangxx_TeachingTask11`
- `Huangxx_CourseSelection11`
- `Huangxx_Score11`
- `Huangxx_ScoreAudit11`
- `Huangxx_CreditLog11`
- `Huangxx_ScoreAppeal11`
- `Huangxx_SystemUser11`
- `Huangxx_OperationLog11`

The score table is enhanced beyond the markdown base design. It stores usual score, final exam score, computed final score, grade level, pass flag, grade point, exam status, score source, publish flag, lock flag, invalid flag, input teacher, modifier metadata, and timestamps.

## Database Automation
Implement trigger-backed behavior:

- Score insert/update calculates final score, grade, pass flag, and grade point.
- Score insert/update checks the input teacher owns the teaching task.
- Score insert/update/delete recalculates student credit and GPA.
- Score update writes a score audit record when score values change.
- Course selection insert/update checks duplicate active selection, task capacity, and enrollment status.
- Course selection changes refresh task current count.
- Appeal processing writes operation logs.

Implement views:

- Student score detail
- Teacher task list
- Class course list
- Course score statistics
- Score ranking
- Student GPA/credit summary
- Region student statistics
- Score appeal processing

Implement stored procedures:

- Student transcript
- Course score statistics
- Class credit statistics
- Region student statistics
- Teacher task statistics

## Java Modules
Use package `com.huangxx.mis`.

- `config`: MVC/session auth interceptor.
- `common`: constants, login session model, flash helper.
- `repository`: `JdbcTemplate` access for users, admin lists, teacher workflows, student workflows, statistics, logs.
- `service`: workflow validation and transaction boundaries.
- `controller`: login, admin, teacher, student routes.
- `entity`, `dto`, `vo`: small data carriers for form input and page output.

## Frontend
Use Thymeleaf templates with one shared layout style and simple pages:

- `login.html`
- `admin/*`
- `teacher/*`
- `student/*`
- reusable fragments for header/sidebar/messages
- `static/css/style.css`
- `static/js/main.js`

The UI is utilitarian for repeated演示操作: clear nav, compact tables, aligned forms, visible success/error messages, and database-backed data only.

## Verification
Local verification includes:

- Maven test/compile.
- Tests that required SQL files exist and contain personalized `11` names, core tables, views, triggers, procedures, and no accidental `01` object suffixes.
- Tests for role-route permission helper behavior.
- Tests for score calculation helper behavior.

Full database execution requires a local openGauss/GaussDB instance and is documented in `docs/database_init.md`.
