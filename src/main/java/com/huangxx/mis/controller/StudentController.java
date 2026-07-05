package com.huangxx.mis.controller;

import com.huangxx.mis.common.ControllerSupport;
import com.huangxx.mis.common.SessionUser;
import com.huangxx.mis.service.StudentService;
import com.huangxx.mis.view.TableColumn;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student/index")
    public String index(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("profile", studentService.profile(user.refId()));
        model.addAttribute("gpa", studentService.gpa(user.refId()));
        return "student/index";
    }

    @GetMapping("/student/profile")
    public String profile(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("profile", studentService.profile(user.refId()));
        return "student/profile";
    }

    @GetMapping("/student/selections")
    public String selections(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("title", "已选课程");
        model.addAttribute("rows", studentService.selections(user.refId()));
        model.addAttribute("columns", java.util.List.of(
                TableColumn.text("课程名称", "课程名称"),
                TableColumn.text("任课教师", "任课教师"),
                TableColumn.text("学期", "学期"),
                TableColumn.status("选课状态", "选课状态"),
                TableColumn.status("成绩发布状态", "成绩发布状态")
        ));
        return "student/selections";
    }

    @GetMapping("/student/scores")
    public String scores(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("title", "已发布成绩");
        model.addAttribute("rows", studentService.scores(user.refId()));
        return "student/scores";
    }

    @GetMapping("/student/gpa")
    public String gpa(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("gpa", studentService.gpa(user.refId()));
        return "student/gpa";
    }

    @GetMapping("/student/appeals")
    public String appeals(Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("rows", studentService.appeals(user.refId()));
        model.addAttribute("columns", java.util.List.of(
                TableColumn.text("学生姓名", "学生姓名"),
                TableColumn.text("班级", "班级"),
                TableColumn.text("课程名称", "课程名称"),
                TableColumn.text("任课教师", "任课教师"),
                TableColumn.number("当前成绩", "当前成绩"),
                TableColumn.longText("申诉理由", "申诉理由"),
                TableColumn.status("申诉状态", "申诉状态"),
                TableColumn.longText("处理结果", "处理结果"),
                TableColumn.dateTime("申请时间", "申请时间"),
                TableColumn.dateTime("处理时间", "处理时间")
        ));
        return "student/appeals";
    }

    @GetMapping("/student/appeals/add/{scoreId}")
    public String addAppeal(@PathVariable String scoreId, Model model, HttpSession session) {
        SessionUser user = ControllerSupport.currentUser(session);
        ControllerSupport.putUser(model, session);
        model.addAttribute("score", studentService.scoreForAppeal(scoreId, user.refId()));
        return "student/appeal-form";
    }

    @PostMapping("/student/appeals/add")
    public String saveAppeal(@RequestParam String scoreId,
                             @RequestParam String reason,
                             HttpSession session,
                             RedirectAttributes attributes) {
        SessionUser user = ControllerSupport.currentUser(session);
        try {
            studentService.addAppeal(user, scoreId, reason);
            return ControllerSupport.redirectWithSuccess(attributes, "/student/appeals", "申诉已提交");
        } catch (DataAccessException | IllegalArgumentException ex) {
            return ControllerSupport.redirectWithError(attributes, "/student/appeals", ControllerSupport.friendlyError("提交申诉", ex));
        }
    }
}
