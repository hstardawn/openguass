package com.huangxx.mis.controller;

import com.huangxx.mis.common.ControllerSupport;
import com.huangxx.mis.service.AdminService;
import com.huangxx.mis.view.TablePage;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/index")
    public String index(Model model, HttpSession session) {
        ControllerSupport.putUser(model, session);
        model.addAttribute("dashboard", adminService.dashboard());
        return "admin/index";
    }

    @GetMapping({
            "/admin/students", "/admin/teachers", "/admin/courses", "/admin/majors",
            "/admin/classes", "/admin/tasks", "/admin/stat/course", "/admin/stat/class-credit",
            "/admin/stat/region", "/admin/audit/score", "/admin/logs"
    })
    public String table(Model model, HttpSession session, jakarta.servlet.http.HttpServletRequest request) {
        ControllerSupport.putUser(model, session);
        String key = keyFromPath(request.getRequestURI());
        Map<String, String> filters = request.getParameterMap().entrySet().stream()
                .filter(entry -> entry.getValue().length > 0)
                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0]));
        filters = normalizeFilters(filters);
        TablePage tablePage = adminService.table(key, filters);
        model.addAttribute("tablePage", tablePage);
        model.addAttribute("title", tablePage.title());
        model.addAttribute("description", tablePage.description());
        model.addAttribute("rows", tablePage.rows());
        model.addAttribute("columns", tablePage.columns());
        model.addAttribute("pageKey", key);
        model.addAttribute("filters", filters);
        model.addAttribute("options", adminService.options());
        return "admin/table";
    }

    @GetMapping("/admin/stat/rank")
    public String rank(Model model,
                       HttpSession session,
                       @RequestParam(defaultValue = "class") String mode,
                       @RequestParam(required = false) String classId,
                       @RequestParam(required = false) String classKey,
                       @RequestParam(required = false) String majorId,
                       @RequestParam(required = false) String majorKey,
                       @RequestParam(required = false) Integer gradeYear) {
        ControllerSupport.putUser(model, session);
        boolean majorMode = "major".equals(mode);
        String selectedClass = classId != null && !classId.isBlank() ? classId : classKey;
        String selectedMajor = majorId != null && !majorId.isBlank() ? majorId : majorKey;
        model.addAttribute("title", "成绩排名");
        model.addAttribute("mode", majorMode ? "major" : "class");
        model.addAttribute("classId", selectedClass);
        model.addAttribute("majorId", selectedMajor);
        model.addAttribute("gradeYear", gradeYear);
        model.addAttribute("classes", adminService.classOptions());
        model.addAttribute("majors", adminService.majorOptions());
        model.addAttribute("gradeYears", adminService.gradeYears());
        model.addAttribute("columns", adminService.rankColumns(majorMode ? "major" : "class"));
        model.addAttribute("rows", majorMode
                ? adminService.majorScoreRanks(selectedMajor, gradeYear)
                : adminService.classScoreRanks(selectedClass));
        return "admin/rank";
    }

    @GetMapping("/admin/students/add")
    public String addStudent(Model model, HttpSession session) {
        prepareForm(model, session, "新增学生", "students", false, Map.of());
        return "admin/student-form";
    }

    @GetMapping("/admin/students/import")
    public String importStudents(Model model, HttpSession session) {
        ControllerSupport.putUser(model, session);
        model.addAttribute("title", "一键导入学生");
        return "admin/student-import";
    }

    @PostMapping("/admin/students/import")
    public String doImportStudents(@RequestParam String importText, RedirectAttributes attributes) {
        try {
            int count = adminService.importStudents(importText);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/students", "成功导入 " + count + " 名学生");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/students/import", ControllerSupport.friendlyError("导入", ex));
        }
    }

    @GetMapping("/admin/students/edit/{id}")
    public String editStudent(@PathVariable String id, Model model, HttpSession session) {
        prepareForm(model, session, "编辑学生", "students", true, adminService.editData("students", id));
        return "admin/student-form";
    }

    @PostMapping({"/admin/students/add", "/admin/students/edit"})
    public String saveStudent(@RequestParam Map<String, String> form,
                              RedirectAttributes attributes,
                              jakarta.servlet.http.HttpServletRequest request) {
        try {
            adminService.saveStudent(form, request.getRequestURI().contains("/edit"));
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/students", "学生信息已保存");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/students", ControllerSupport.friendlyError("保存学生", ex));
        }
    }

    @PostMapping("/admin/students/delete/{id}")
    public String deleteStudent(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteStudent(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/students", "学生已删除");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/students", ControllerSupport.friendlyError("删除学生", ex));
        }
    }

    @GetMapping("/admin/teachers/add")
    public String addTeacher(Model model, HttpSession session) {
        prepareForm(model, session, "新增教师", "teachers", false, Map.of());
        return "admin/teacher-form";
    }

    @GetMapping("/admin/teachers/edit/{id}")
    public String editTeacher(@PathVariable String id, Model model, HttpSession session) {
        prepareForm(model, session, "编辑教师", "teachers", true, adminService.editData("teachers", id));
        return "admin/teacher-form";
    }

    @PostMapping({"/admin/teachers/add", "/admin/teachers/edit"})
    public String saveTeacher(@RequestParam Map<String, String> form,
                              RedirectAttributes attributes,
                              jakarta.servlet.http.HttpServletRequest request) {
        try {
            adminService.saveTeacher(form, request.getRequestURI().contains("/edit"));
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/teachers", "教师信息已保存");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/teachers", ControllerSupport.friendlyError("保存教师", ex));
        }
    }

    @PostMapping("/admin/teachers/delete/{id}")
    public String deleteTeacher(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteTeacher(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/teachers", "教师已删除");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/teachers", ControllerSupport.friendlyError("删除教师", ex));
        }
    }

    @GetMapping("/admin/courses/add")
    public String addCourse(Model model, HttpSession session) {
        prepareForm(model, session, "新增课程", "courses", false, Map.of());
        return "admin/course-form";
    }

    @GetMapping("/admin/courses/edit/{id}")
    public String editCourse(@PathVariable String id, Model model, HttpSession session) {
        prepareForm(model, session, "编辑课程", "courses", true, adminService.editData("courses", id));
        return "admin/course-form";
    }

    @PostMapping({"/admin/courses/add", "/admin/courses/edit"})
    public String saveCourse(@RequestParam Map<String, String> form,
                             RedirectAttributes attributes,
                             jakarta.servlet.http.HttpServletRequest request) {
        try {
            adminService.saveCourse(form, request.getRequestURI().contains("/edit"));
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/courses", "课程信息已保存");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/courses", ControllerSupport.friendlyError("保存课程", ex));
        }
    }

    @PostMapping("/admin/courses/delete/{id}")
    public String deleteCourse(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteCourse(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/courses", "课程已删除");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/courses", ControllerSupport.friendlyError("删除课程", ex));
        }
    }

    @GetMapping("/admin/majors/add")
    public String addMajor(Model model, HttpSession session) {
        prepareForm(model, session, "新增专业", "majors", false, Map.of());
        return "admin/major-form";
    }

    @GetMapping("/admin/majors/edit/{id}")
    public String editMajor(@PathVariable String id, Model model, HttpSession session) {
        prepareForm(model, session, "编辑专业", "majors", true, adminService.editData("majors", id));
        return "admin/major-form";
    }

    @PostMapping({"/admin/majors/add", "/admin/majors/edit"})
    public String saveMajor(@RequestParam Map<String, String> form,
                            RedirectAttributes attributes,
                            jakarta.servlet.http.HttpServletRequest request) {
        try {
            adminService.saveMajor(form, request.getRequestURI().contains("/edit"));
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/majors", "专业信息已保存");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/majors", ControllerSupport.friendlyError("保存专业", ex));
        }
    }

    @PostMapping("/admin/majors/delete/{id}")
    public String deleteMajor(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteMajor(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/majors", "专业已删除");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/majors", ControllerSupport.friendlyError("删除专业", ex));
        }
    }

    @GetMapping("/admin/classes/add")
    public String addClass(Model model, HttpSession session) {
        prepareForm(model, session, "新增班级", "classes", false, Map.of());
        return "admin/class-form";
    }

    @GetMapping("/admin/classes/edit/{id}")
    public String editClass(@PathVariable String id, Model model, HttpSession session) {
        prepareForm(model, session, "编辑班级", "classes", true, adminService.editData("classes", id));
        return "admin/class-form";
    }

    @PostMapping({"/admin/classes/add", "/admin/classes/edit"})
    public String saveClass(@RequestParam Map<String, String> form,
                            RedirectAttributes attributes,
                            jakarta.servlet.http.HttpServletRequest request) {
        try {
            adminService.saveClass(form, request.getRequestURI().contains("/edit"));
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/classes", "班级信息已保存");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/classes", ControllerSupport.friendlyError("保存班级", ex));
        }
    }

    @PostMapping("/admin/classes/delete/{id}")
    public String deleteClass(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteClass(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/classes", "班级已删除");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/classes", ControllerSupport.friendlyError("删除班级", ex));
        }
    }

    @GetMapping("/admin/tasks/add")
    public String addTask(Model model, HttpSession session) {
        prepareForm(model, session, "新增教学任务", "tasks", false, Map.of());
        return "admin/task-form";
    }

    @GetMapping("/admin/tasks/edit/{id}")
    public String editTask(@PathVariable String id, Model model, HttpSession session) {
        prepareForm(model, session, "编辑教学任务", "tasks", true, adminService.editData("tasks", id));
        return "admin/task-form";
    }

    @PostMapping({"/admin/tasks/add", "/admin/tasks/edit"})
    public String saveTask(@RequestParam Map<String, String> form,
                           RedirectAttributes attributes,
                           jakarta.servlet.http.HttpServletRequest request) {
        try {
            adminService.saveTask(form, request.getRequestURI().contains("/edit"));
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/tasks", "教学任务已保存");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/tasks", ControllerSupport.friendlyError("保存教学任务", ex));
        }
    }

    @PostMapping("/admin/tasks/delete/{id}")
    public String deleteTask(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteTask(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/tasks", "教学任务已删除");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/tasks", ControllerSupport.friendlyError("删除教学任务", ex));
        }
    }

    private void prepareForm(Model model, HttpSession session, String title, String type, boolean edit, Map<String, Object> row) {
        ControllerSupport.putUser(model, session);
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("edit", edit);
        model.addAttribute("row", row);
        model.addAttribute("options", adminService.options());
    }

    private String keyFromPath(String path) {
        if (path.contains("/stat/course")) return "stat-course";
        if (path.contains("/stat/class-credit")) return "stat-class-credit";
        if (path.contains("/stat/region")) return "stat-region";
        if (path.contains("/stat/rank")) return "stat-rank";
        if (path.contains("/audit/score")) return "audit-score";
        if (path.contains("/logs")) return "logs";
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private Map<String, String> normalizeFilters(Map<String, String> filters) {
        java.util.Map<String, String> normalized = new java.util.HashMap<>(filters);
        alias(normalized, "classKey", "classId");
        alias(normalized, "regionKey", "regionId");
        alias(normalized, "teacherKey", "teacherId");
        alias(normalized, "courseKey", "courseId");
        alias(normalized, "majorKey", "majorId");
        return normalized;
    }

    private void alias(Map<String, String> filters, String from, String to) {
        if (filters.containsKey(from) && !filters.containsKey(to)) {
            filters.put(to, filters.get(from));
        }
    }

    private String title(String key) {
        return switch (key) {
            case "students" -> "学生管理";
            case "teachers" -> "教师管理";
            case "courses" -> "课程管理";
            case "majors" -> "专业管理";
            case "classes" -> "班级管理";
            case "tasks" -> "教学任务管理";
            case "stat-course" -> "课程成绩统计";
            case "stat-class-credit" -> "班级学分统计";
            case "stat-region" -> "生源地统计";
            case "stat-rank" -> "成绩排名";
            case "audit-score" -> "成绩审计日志";
            case "logs" -> "操作日志";
            default -> "管理页面";
        };
    }

    private String description(String key) {
        return switch (key) {
            case "stat-rank" -> "按教学任务、班级学期、专业年级分别计算排名，避免跨专业跨年级混排。";
            case "audit-score" -> "展示成绩修改的业务上下文和修改轨迹，隐藏审计主键、成绩记录编号等技术字段。";
            case "logs" -> "展示系统业务操作记录，优先解析申诉、成绩、教学任务等业务对象名称。";
            case "stat-course" -> "按教学任务汇总课程成绩分布、通过率和优秀率。";
            case "stat-class-credit" -> "查看学生学分、GPA 和课程通过情况。";
            case "stat-region" -> "统计各生源地学生分布。";
            default -> "维护教务基础数据和业务信息。";
        };
    }
}
