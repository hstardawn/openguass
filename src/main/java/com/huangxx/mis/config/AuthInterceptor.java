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
            if (isApi(path)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"请先登录系统\"}");
                return false;
            }
            response.sendRedirect("/login");
            return false;
        }

        if (!AuthRules.canAccess(user.role(), path)) {
            if (isApi(path)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"当前账号无权访问该功能\"}");
                return false;
            }
            response.sendRedirect(user.role().homePath());
            return false;
        }

        request.setAttribute("loginUser", user);
        return true;
    }

    private boolean isApi(String path) {
        return path.startsWith("/api/");
    }
}
