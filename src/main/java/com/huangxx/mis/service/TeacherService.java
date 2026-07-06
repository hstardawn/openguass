package com.huangxx.mis.service;

import com.huangxx.mis.common.SessionUser;
import com.huangxx.mis.repository.OperationLogRepository;
import com.huangxx.mis.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final OperationLogRepository operationLogRepository;

    public TeacherService(TeacherRepository teacherRepository, OperationLogRepository operationLogRepository) {
        this.teacherRepository = teacherRepository;
        this.operationLogRepository = operationLogRepository;
    }

    public List<Map<String, Object>> tasks(String teacherId) {
        return teacherRepository.tasks(teacherId);
    }

    public Map<String, Object> task(String taskId, String teacherId) {
        return teacherRepository.task(taskId, teacherId);
    }

    public List<Map<String, Object>> taskStudents(String taskId, String teacherId) {
        return teacherRepository.taskStudents(taskId, teacherId);
    }

    public Map<String, Object> selectionForScore(String selectionId, String teacherId) {
        return teacherRepository.selectionForScore(selectionId, teacherId);
    }

    public Map<String, Object> score(String scoreId, String teacherId) {
        return teacherRepository.score(scoreId, teacherId);
    }

    @Transactional
    public void addScore(SessionUser user, String selectionId, BigDecimal usualScore, BigDecimal examScore) {
        if (!teacherRepository.selectionCanReceiveScore(selectionId, user.refId())) {
            throw new IllegalArgumentException("只有已结束教学任务中的有效选课记录才能录入成绩");
        }
        String scoreId = "SC-JAVA-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        teacherRepository.addScore(scoreId, selectionId, usualScore, examScore, user.refId());
        operationLogRepository.log(user, "成绩录入", selectionId, "教师录入学生成绩", "成功");
    }

    @Transactional
    public void editScore(SessionUser user, String scoreId, BigDecimal usualScore, BigDecimal examScore, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("成绩修改必须填写修改原因");
        }
        if (!teacherRepository.scoreCanBeEdited(scoreId, user.refId())) {
            throw new IllegalArgumentException("只有已结束教学任务的成绩才能修改");
        }
        teacherRepository.editScore(scoreId, usualScore, examScore, user.refId(), reason);
        operationLogRepository.log(user, "成绩修改", scoreId, reason, "成功");
    }

    @Transactional
    public void publish(SessionUser user, String taskId) {
        if (!teacherRepository.ownsTask(taskId, user.refId())) {
            throw new IllegalArgumentException("教师只能发布自己的教学任务成绩");
        }
        if (!teacherRepository.taskEnded(taskId, user.refId())) {
            throw new IllegalArgumentException("教学任务结束后才能发布成绩");
        }
        teacherRepository.publishTaskScores(taskId, user.refId());
        operationLogRepository.log(user, "成绩发布", taskId, "教师发布教学任务成绩", "成功");
    }

    public List<Map<String, Object>> courseStat(String taskId, String teacherId) {
        return teacherRepository.courseStat(taskId, teacherId);
    }

    public List<Map<String, Object>> appeals(String teacherId) {
        return teacherRepository.appeals(teacherId);
    }

    @Transactional
    public void handleAppeal(SessionUser user, String appealId, String status, String result) {
        teacherRepository.handleAppeal(appealId, user.refId(), status, result);
        operationLogRepository.log(user, "申诉处理", appealId, "处理申诉：" + status + "，" + result, "成功");
    }
}
