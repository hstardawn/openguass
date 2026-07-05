package com.huangxx.mis.common;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public final class ControllerSupport {

    private ControllerSupport() {
    }

    public static SessionUser currentUser(HttpSession session) {
        return (SessionUser) session.getAttribute(AppConstants.SESSION_USER);
    }

    public static void putUser(Model model, HttpSession session) {
        SessionUser user = currentUser(session);
        if (user != null) {
            model.addAttribute("loginUser", user);
        }
    }

    public static String redirectWithSuccess(RedirectAttributes attributes, String path, String message) {
        attributes.addFlashAttribute(AppConstants.FLASH_SUCCESS, message);
        return "redirect:" + path;
    }

    public static String redirectWithError(RedirectAttributes attributes, String path, String message) {
        attributes.addFlashAttribute(AppConstants.FLASH_ERROR, message);
        return "redirect:" + path;
    }
}
