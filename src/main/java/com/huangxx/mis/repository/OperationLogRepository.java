package com.huangxx.mis.repository;

import com.huangxx.mis.common.SessionUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class OperationLogRepository {

    private final JdbcTemplate jdbcTemplate;

    public OperationLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void log(SessionUser user, String type, String object, String description, String result) {
        String id = "LOG-JAVA-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        jdbcTemplate.update("""
                INSERT INTO Huangxx_OperationLog11
                (hxx_log_id11, hxx_user_id11, hxx_login_name11, hxx_role11,
                 hxx_operation_type11, hxx_operation_object11, hxx_operation_desc11, hxx_operation_result11)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                id,
                user == null ? null : user.userId(),
                user == null ? null : user.loginName(),
                user == null ? null : user.role().dbValue(),
                type,
                object,
                description,
                result
        );
    }
}
