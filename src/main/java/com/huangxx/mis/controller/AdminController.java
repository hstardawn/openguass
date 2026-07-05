package com.huangxx.mis.controller;

import com.huangxx.mis.common.ControllerSupport;
import com.huangxx.mis.service.AdminService;
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
        model.addAttribute("title", title(key));
        model.addAttribute("description", description(key));
        model.addAttribute("rows", adminService.table(key));
        model.addAttribute("pageKey", key);
        return "admin/table";
    }

    @GetMapping("/admin/stat/rank")
    public String rank(Model model,
                       HttpSession session,
                       @RequestParam(defaultValue = "class") String mode,
                       @RequestParam(required = false) String classId,
                       @RequestParam(required = false) String majorId,
                       @RequestParam(required = false) Integer gradeYear) {
        ControllerSupport.putUser(model, session);
        boolean majorMode = "major".equals(mode);
        model.addAttribute("title", "成绩排名");
        model.addAttribute("mode", majorMode ? "major" : "class");
        model.addAttribute("classId", classId);
        model.addAttribute("majorId", majorId);
        model.addAttribute("gradeYear", gradeYear);
        model.addAttribute("classes", adminService.classOptions());
        model.addAttribute("majors", adminService.majorOptions());
        model.addAttribute("gradeYears", adminService.gradeYears());
        model.addAttribute("rows", majorMode
                ? adminService.majorScoreRanks(majorId, gradeYear)
                : adminService.classScoreRanks(classId));
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
            return ControllerSupport.redirectWithError(attributes, "/admin/students/import", "导入失败：" + ex.getMessage());
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
            return ControllerSupport.redirectWithError(attributes, "/admin/students", "保存学生失败：" + ex.getMessage());
        }
    }

    @PostMapping("/admin/students/delete/{id}")
    public String deleteStudent(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteStudent(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/students", "学生已删除");
        } catch (DataAccessException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/students", "删除学生失败：" + ex.getMessage());
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
            return ControllerSupport.redirectWithError(attributes, "/admin/teachers", "保存教师失败：" + ex.getMessage());
        }
    }

    @PostMapping("/admin/teachers/delete/{id}")
    public String deleteTeacher(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteTeacher(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/teachers", "教师已删除");
        } catch (DataAccessException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/teachers", "删除教师失败：" + ex.getMessage());
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
            return ControllerSupport.redirectWithError(attributes, "/admin/courses", "保存课程失败：" + ex.getMessage());
        }
    }

    @PostMapping("/admin/courses/delete/{id}")
    public String deleteCourse(@PathVariable String id, RedirectAttributes attributes) {
        try {
            adminService.deleteCourse(id);
            return ControllerSupport.redirectWithSuccess(attributes, "/admin/courses", "课程已删除");
        } catch (DataAccessException ex) {
            return ControllerSupport.redirectWithError(attributes, "/admin/courses", "删除课程失败：" + ex.getMessage());
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
            return ControllerSupport.redirectWithError(attributes, "/admin/tasks", "保存教学任务失败：" + ex.getMessage());
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
