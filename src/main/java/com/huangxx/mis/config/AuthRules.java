package com.huangxx.mis.config;

import com.huangxx.mis.common.Role;

public final class AuthRules {

    private AuthRules() {
    }

    public static boolean isPublicRoute(String path) {
        return path.equals("/")
                || path.equals("/login")
                || path.equals("/logout")
                || path.equals("/api/auth/login")
                || path.equals("/api/auth/forgot")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/app/")
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
            case ADMIN -> path.startsWith("/admin/") || path.equals("/admin")
                    || path.startsWith("/api/admin/") || path.equals("/api/auth/me") || path.equals("/api/auth/logout")
                    || path.equals("/api/auth/password");
            case TEACHER -> path.startsWith("/teacher/") || path.equals("/teacher")
                    || path.startsWith("/api/teacher/") || path.equals("/api/auth/me") || path.equals("/api/auth/logout")
                    || path.equals("/api/auth/password");
            case STUDENT -> path.startsWith("/student/") || path.equals("/student")
                    || path.startsWith("/api/student/") || path.equals("/api/auth/me") || path.equals("/api/auth/logout")
                    || path.equals("/api/auth/password");
        };
    }
}
