package com.huangxx.mis.controller;

import com.huangxx.mis.common.AppConstants;
import com.huangxx.mis.common.SessionUser;
import com.huangxx.mis.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "HuangxxMIS11 登录");
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String loginName,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes attributes) {
        Optional<SessionUser> user = loginService.login(loginName, password);
        if (user.isEmpty()) {
            attributes.addFlashAttribute(AppConstants.FLASH_ERROR, "登录失败：账号、密码错误或用户被禁用");
            return "redirect:/login";
        }
        session.setAttribute(AppConstants.SESSION_USER, user.get());
        return "redirect:" + user.get().role().homePath();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes attributes) {
        session.invalidate();
        attributes.addFlashAttribute(AppConstants.FLASH_SUCCESS, "已退出系统");
        return "redirect:/login";
    }
}
