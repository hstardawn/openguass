package com.huangxx.mis.config;

import com.huangxx.mis.common.Role;

public final class AuthRules {

    private AuthRules() {
    }

    public static boolean isPublicRoute(String path) {
        return path.equals("/")
                || path.equals("/login")
                || path.equals("/logout")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.equals("/favicon.ico")
                || path.startsWith("/error");
    }

    public static boolean canAccess(Role role, String path) {
        if (isPublicRoute(path)) {
            return true;
        }
        if (role == null) {
            return false;
        }
        return switch (role) {
            case ADMIN -> path.startsWith("/admin/") || path.equals("/admin");
            case TEACHER -> path.startsWith("/teacher/") || path.equals("/teacher");
            case STUDENT -> path.startsWith("/student/") || path.equals("/student");
        };
    }
}
