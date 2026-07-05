package com.huangxx.mis.service;

import com.huangxx.mis.common.SessionUser;
import com.huangxx.mis.repository.OperationLogRepository;
import com.huangxx.mis.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final OperationLogRepository operationLogRepository;

    public StudentService(StudentRepository studentRepository, OperationLogRepository operationLogRepository) {
        this.studentRepository = studentRepository;
        this.operationLogRepository = operationLogRepository;
    }

    public Map<String, Object> profile(String studentId) {
        return studentRepository.profile(studentId);
    }

    public List<Map<String, Object>> selections(String studentId) {
        return studentRepository.selections(studentId);
    }

    public List<Map<String, Object>> scores(String studentId) {
        return studentRepository.publishedScores(studentId);
    }

    public Map<String, Object> gpa(String studentId) {
        return studentRepository.gpa(studentId);
    }

    public List<Map<String, Object>> appeals(String studentId) {
        return studentRepository.appeals(studentId);
    }

    public Map<String, Object> scoreForAppeal(String scoreId, String studentId) {
        return studentRepository.scoreForAppeal(scoreId, studentId);
    }

    @Transactional
    public void addAppeal(SessionUser user, String scoreId, String reason) {
        if (studentRepository.hasPendingAppeal(scoreId, user.refId())) {
            throw new IllegalArgumentException("同一成绩已有待处理申诉，不能重复提交");
        }
        String appealId = "AP-JAVA-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        studentRepository.addAppeal(appealId, scoreId, user.refId(), reason);
        operationLogRepository.log(user, "提交申诉", scoreId, reason, "成功");
    }
}
