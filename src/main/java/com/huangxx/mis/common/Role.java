package com.huangxx.mis.common;

public enum Role {
    ADMIN("管理员", "/admin/index"),
    TEACHER("教师", "/teacher/index"),
    STUDENT("学生", "/student/index");

    private final String dbValue;
    private final String homePath;

    Role(String dbValue, String homePath) {
        this.dbValue = dbValue;
        this.homePath = homePath;
    }

    public String dbValue() {
        return dbValue;
    }

    public String homePath() {
        return homePath;
    }

    public static Role fromDbValue(String value) {
        for (Role role : values()) {
            if (role.dbValue.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知角色: " + value);
    }
}
