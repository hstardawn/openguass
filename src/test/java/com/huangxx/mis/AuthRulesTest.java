package com.huangxx.mis;

import com.huangxx.mis.common.Role;
import com.huangxx.mis.config.AuthRules;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthRulesTest {

    @Test
    void allowsMatchingRolePrefix() {
        assertThat(AuthRules.canAccess(Role.ADMIN, "/admin/students")).isTrue();
        assertThat(AuthRules.canAccess(Role.TEACHER, "/teacher/tasks")).isTrue();
        assertThat(AuthRules.canAccess(Role.STUDENT, "/student/scores")).isTrue();
    }

    @Test
    void blocksCrossRoleAccess() {
        assertThat(AuthRules.canAccess(Role.STUDENT, "/teacher/tasks")).isFalse();
        assertThat(AuthRules.canAccess(Role.TEACHER, "/admin/index")).isFalse();
        assertThat(AuthRules.canAccess(Role.ADMIN, "/student/profile")).isFalse();
    }

    @Test
    void allowsPublicAndStaticRoutesForAnyRole() {
        assertThat(AuthRules.canAccess(Role.STUDENT, "/login")).isTrue();
        assertThat(AuthRules.canAccess(Role.TEACHER, "/css/style.css")).isTrue();
        assertThat(AuthRules.canAccess(Role.ADMIN, "/js/main.js")).isTrue();
    }

    @Test
    void allowsLogoutToReachControllerForAnySignedInRole() {
        assertThat(AuthRules.canAccess(Role.ADMIN, "/logout")).isTrue();
        assertThat(AuthRules.canAccess(Role.TEACHER, "/logout")).isTrue();
        assertThat(AuthRules.canAccess(Role.STUDENT, "/logout")).isTrue();
    }
}
