# Frontend Admin Polish Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Upgrade the Spring Boot + Thymeleaf academic management UI into a clean backend system without exposing database or technical fields.

**Architecture:** Add explicit table view models in Java, have controllers pass column definitions and rows, and render only configured visible columns. Keep Thymeleaf server-side rendering, existing services, and database structure intact while improving shared templates and CSS.

**Tech Stack:** Spring Boot, Thymeleaf, Java records, CSS.

---

### Task 1: Explicit Table View Model

**Files:**
- Create: `src/main/java/com/huangxx/mis/view/TableColumn.java`
- Create: `src/main/java/com/huangxx/mis/view/TablePage.java`
- Modify: `src/main/java/com/huangxx/mis/service/AdminService.java`
- Modify: `src/main/java/com/huangxx/mis/controller/AdminController.java`

- [ ] Add records for table columns and page metadata.
- [ ] Return `TablePage` from admin table service instead of raw rows.
- [ ] Configure visible columns per admin list/stat page.

### Task 2: Safe Table Rendering

**Files:**
- Modify: `src/main/resources/templates/fragments/layout.html`
- Modify: `src/main/resources/templates/admin/table.html`
- Modify: `src/main/resources/templates/admin/rank.html`

- [ ] Replace `row.entrySet()` rendering with explicit `columns`.
- [ ] Render operation buttons from hidden edit/delete paths only.
- [ ] Add status/number/text cell styling by column type.

### Task 3: Forms And Login Polish

**Files:**
- Modify: `src/main/resources/templates/login.html`
- Modify: `src/main/resources/templates/admin/student-form.html`
- Modify: `src/main/resources/templates/admin/teacher-form.html`
- Modify: `src/main/resources/templates/admin/course-form.html`
- Modify: `src/main/resources/templates/admin/task-form.html`
- Modify: `src/main/resources/templates/admin/class-form.html`
- Modify: `src/main/resources/templates/admin/major-form.html`

- [ ] Remove default demo account display.
- [ ] Group forms by business sections.
- [ ] Add required markers, placeholders, and consistent action bars.

### Task 4: Student And Teacher Pages

**Files:**
- Modify: `src/main/resources/templates/student/*.html`
- Modify: `src/main/resources/templates/teacher/*.html`

- [ ] Keep hidden identifiers only in form actions/hidden inputs.
- [ ] Use clean cards and explicit table headers.
- [ ] Add empty states and consistent status tags.

### Task 5: CSS And Verification

**Files:**
- Modify: `src/main/resources/static/css/style.css`

- [ ] Consolidate CSS variables, layout, forms, tables, tags, alerts, login page.
- [ ] Run Maven tests.
- [ ] Start on port 8081 and verify login, admin list, course add, student pages, teacher pages.
