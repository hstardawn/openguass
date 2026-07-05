# HuangxxMIS11 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the full Spring Boot + Thymeleaf + openGauss course-design system for HuangxxMIS11.

**Architecture:** A single Spring Boot MVC app serves role-based Thymeleaf pages. `JdbcTemplate` repositories talk to the personalized openGauss schema, while triggers/views/procedures prove the database design requirements.

**Tech Stack:** Java 17, Spring Boot 3.3.x, Maven, Thymeleaf, Spring JDBC, PostgreSQL JDBC driver for openGauss compatibility, JUnit 5.

---

### File Structure

- `pom.xml`: Maven build and dependencies.
- `src/main/resources/application.yml`: database connection template.
- `src/main/java/com/huangxx/mis/HuangxxMisApplication.java`: Spring Boot entry point.
- `src/main/java/com/huangxx/mis/common/*`: constants, session user, flash helpers, score helper.
- `src/main/java/com/huangxx/mis/config/*`: interceptor and MVC config.
- `src/main/java/com/huangxx/mis/repository/*`: SQL access grouped by workflow.
- `src/main/java/com/huangxx/mis/service/*`: login, admin, teacher, student services.
- `src/main/java/com/huangxx/mis/controller/*`: MVC route controllers.
- `src/main/java/com/huangxx/mis/dto/*`: form DTOs.
- `src/main/java/com/huangxx/mis/vo/*`: view records.
- `src/main/java/com/huangxx/mis/entity/*`: minimal entity records for required layer structure.
- `src/main/resources/templates/**`: Thymeleaf pages.
- `src/main/resources/static/**`: CSS and JS.
- `sql/01_create_database.sql` through `sql/08_test_and_verify.sql`: executable SQL sequence.
- `docs/run_guide.md`, `docs/database_init.md`, `docs/demo_script.md`: delivery docs.
- `src/test/java/com/huangxx/mis/*`: SQL content and helper tests.

### Task 1: Test and Scaffold

- [ ] Create Maven project files and tests first.
- [ ] Write tests for route permission and score helper behavior.
- [ ] Run `mvn test` and confirm expected compile failure before helper classes exist.
- [ ] Add minimal application, helper, and config classes.
- [ ] Run `mvn test` and confirm helper tests pass.

### Task 2: SQL Files

- [ ] Write SQL content tests that check required files, `11` naming, no `01` names, 15 tables, views, triggers, procedures, and verification calls.
- [ ] Run `mvn test` and confirm SQL tests fail before SQL files exist.
- [ ] Add `sql/01_create_database.sql` through `sql/08_test_and_verify.sql`.
- [ ] Run `mvn test` and confirm SQL tests pass.

### Task 3: Backend Workflows

- [ ] Implement repositories for login, admin lists/statistics, teacher tasks/scores/appeals, student profile/scores/appeals, and logs.
- [ ] Implement services with transaction boundaries and ownership checks.
- [ ] Implement controllers for public, admin, teacher, and student routes.
- [ ] Run `mvn test` and `mvn -DskipTests package`.

### Task 4: Thymeleaf Pages

- [ ] Add shared fragments, login page, admin pages, teacher pages, student pages, and static assets.
- [ ] Make all listed routes return real templates.
- [ ] Run `mvn -DskipTests package` to verify template parsing and compilation.

### Task 5: Delivery Docs

- [ ] Write `README.md`.
- [ ] Write `docs/run_guide.md`.
- [ ] Write `docs/database_init.md`.
- [ ] Write `docs/demo_script.md`.
- [ ] Run final verification: `mvn test`, `mvn -DskipTests package`, and static file checks.

### Self-Review

- The plan covers SQL, Java code, frontend pages, docs, and verification.
- There are no placeholder sections.
- Naming is fixed to `Huangxx/hxx/11` throughout generated artifacts.
