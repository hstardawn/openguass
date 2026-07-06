package com.huangxx.mis.repository;

import com.huangxx.mis.common.Role;
import com.huangxx.mis.common.SessionUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LoginRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoginRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<SessionUser> findActiveUser(String loginName, String password) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
                SELECT hxx_user_id11, hxx_login_name11, hxx_role11, hxx_ref_id11
                  FROM Huangxx_SystemUser11
                 WHERE hxx_login_name11 = ?
                   AND hxx_password11 = ?
                   AND hxx_user_status11 = '正常'
                """, loginName, password);
        if (rows.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Object> row = rows.get(0);
        Role role = Role.fromDbValue(String.valueOf(row.get("hxx_role11")));
        String refId = row.get("hxx_ref_id11") == null ? null : String.valueOf(row.get("hxx_ref_id11"));
        String displayName = loadDisplayName(role, refId, loginName);
        return Optional.of(new SessionUser(
                String.valueOf(row.get("hxx_user_id11")),
                String.valueOf(row.get("hxx_login_name11")),
                role,
                refId,
                displayName
        ));
    }

    public void markLogin(String userId) {
        jdbcTemplate.update("UPDATE Huangxx_SystemUser11 SET hxx_last_login_time11 = current_timestamp WHERE hxx_user_id11 = ?", userId);
    }

    public boolean passwordMatches(String userId, String password) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*) FROM Huangxx_SystemUser11
                 WHERE hxx_user_id11 = ?
                   AND hxx_password11 = ?
                   AND hxx_user_status11 = '正常'
                """, Integer.class, userId, password);
        return count != null && count > 0;
    }

    public void changePassword(String userId, String newPassword) {
        jdbcTemplate.update("UPDATE Huangxx_SystemUser11 SET hxx_password11 = ? WHERE hxx_user_id11 = ?", newPassword, userId);
    }

    private String loadDisplayName(Role role, String refId, String loginName) {
        if (refId == null || refId.isBlank()) {
            return loginName;
        }
        if (role == Role.TEACHER) {
            return jdbcTemplate.queryForObject("SELECT hxx_teacher_name11 FROM Huangxx_Teacher11 WHERE hxx_teacher_id11 = ?", String.class, refId);
        }
        if (role == Role.STUDENT) {
            return jdbcTemplate.queryForObject("SELECT hxx_student_name11 FROM Huangxx_Student11 WHERE hxx_student_id11 = ?", String.class, refId);
        }
        return loginName;
    }
}
