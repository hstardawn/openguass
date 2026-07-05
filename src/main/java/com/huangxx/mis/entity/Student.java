package com.huangxx.mis.entity;

import java.math.BigDecimal;

public record Student(String studentId, String studentName, String classId, BigDecimal totalCredit, BigDecimal gpa) {
}
