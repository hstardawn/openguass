package com.huangxx.mis.config;

import com.huangxx.mis.common.AppConstants;
import com.huangxx.mis.common.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        if (AuthRules.isPublicRoute(path)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        SessionUser user = session == null ? null : (SessionUser) session.getAttribute(AppConstants.SESSION_USER);
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        if (!AuthRules.canAccess(user.role(), path)) {
            response.sendRedirect(user.role().homePath());
            return false;
        }

        request.setAttribute("loginUser", user);
        return true;
    }
}
