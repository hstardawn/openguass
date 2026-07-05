package com.huangxx.mis.controller;

import com.huangxx.mis.common.ControllerSupport;
import com.huangxx.mis.common.SessionUser;
import com.huangxx.mis.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/teacher/index")
    public String index(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("tasks", teacherService.tasks(user.refId()));
        return "teacher/index";
    }

    @GetMapping("/teacher/tasks")
    public String tasks(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("title", "我的教学任务");
        model.addAttribute("rows", teacherService.tasks(user.refId()));
        return "teacher/tasks";
    }

    @GetMapping("/teacher/tasks/{taskId}/students")
    public String taskStudents(@PathVariable String taskId, Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("task", teacherService.task(taskId, user.refId()));
        model.addAttribute("rows", teacherService.taskStudents(taskId, user.refId()));
        return "teacher/task-students";
    }

    @GetMapping("/teacher/score/add/{selectionId}")
    public String addScore(@PathVariable String selectionId, Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("selection", teacherService.selectionForScore(selectionId, user.refId()));
        return "teacher/score-form";
    }

    @PostMapping("/teacher/score/add")
    public String saveScore(@RequestParam String selectionId,
                            @RequestParam BigDecimal usualScore,
                            @RequestParam BigDecimal examScore,
                            HttpSession session,
                            RedirectAttributes attributes) {
        SessionUser user = ControllerSupport.currentUser(session);
        try {
            teacherService.addScore(user, selectionId, usualScore, examScore);
            return ControllerSupport.redirectWithSuccess(attributes, "/teacher/tasks", "成绩录入成功，最终成绩、等级和 GPA 已由数据库自动维护");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/teacher/tasks", "成绩录入失败：" + ex.getMessage());
        }
    }

    @GetMapping("/teacher/score/edit/{scoreId}")
    public String editScore(@PathVariable String scoreId, Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("score", teacherService.score(scoreId, user.refId()));
        return "teacher/score-edit";
    }

    @PostMapping("/teacher/score/edit")
    public String updateScore(@RequestParam String scoreId,
                              @RequestParam BigDecimal usualScore,
                              @RequestParam BigDecimal examScore,
                              @RequestParam String reason,
                              HttpSession session,
                              RedirectAttributes attributes) {
        SessionUser user = ControllerSupport.currentUser(session);
        try {
            teacherService.editScore(user, scoreId, usualScore, examScore, reason);
            return ControllerSupport.redirectWithSuccess(attributes, "/teacher/tasks", "成绩修改成功，审计记录已写入");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/teacher/tasks", "成绩修改失败：" + ex.getMessage());
        }
    }

    @PostMapping("/teacher/score/publish/{taskId}")
    public String publish(@PathVariable String taskId, HttpSession session, RedirectAttributes attributes) {
        SessionUser user = ControllerSupport.currentUser(session);
        try {
            teacherService.publish(user, taskId);
            return ControllerSupport.redirectWithSuccess(attributes, "/teacher/tasks", "成绩已发布，学生端现在可见");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/teacher/tasks", "成绩发布失败：" + ex.getMessage());
        }
    }

    @GetMapping("/teacher/stat/course/{taskId}")
    public String courseStat(@PathVariable String taskId, Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("title", "课程成绩统计");
        model.addAttribute("rows", teacherService.courseStat(taskId, user.refId()));
        return "teacher/stat-course";
    }

    @GetMapping("/teacher/appeals")
    public String appeals(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("rows", teacherService.appeals(user.refId()));
        return "teacher/appeals";
    }

    @PostMapping("/teacher/appeals/handle")
    public String handleAppeal(@RequestParam String appealId,
                               @RequestParam String status,
                               @RequestParam String result,
                               HttpSession session,
                               RedirectAttributes attributes) {
        SessionUser user = ControllerSupport.currentUser(session);
        try {
            teacherService.handleAppeal(user, appealId, status, result);
            return ControllerSupport.redirectWithSuccess(attributes, "/teacher/appeals", "申诉已处理，操作日志已写入");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/teacher/appeals", "申诉处理失败：" + ex.getMessage());
        }
    }
}
