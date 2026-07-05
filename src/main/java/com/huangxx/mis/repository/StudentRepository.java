package com.huangxx.mis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> profile(String studentId) {
        return jdbcTemplate.queryForMap("""
                SELECT s.hxx_student_id11 AS 学号,
                       s.hxx_student_name11 AS 姓名,
                       s.hxx_gender11 AS 性别,
                       s.hxx_age11 AS 年龄,
                       cls.hxx_class_name11 AS 班级,
                       major.hxx_major_name11 AS 专业,
                       r.hxx_region_name11 AS 生源地,
                       s.hxx_total_credit11 AS 已修学分,
                       s.hxx_gpa11 AS GPA,
                       s.hxx_phone11 AS 联系电话,
                       s.hxx_status11 AS 学籍状态,
                       s.hxx_enroll_date11 AS 入学日期
                  FROM Huangxx_Student11 s
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = s.hxx_class_id11
                  JOIN Huangxx_Major11 major ON major.hxx_major_id11 = cls.hxx_major_id11
                  JOIN Huangxx_Region11 r ON r.hxx_region_id11 = s.hxx_region_id11
                 WHERE s.hxx_student_id11 = ?
                """, studentId);
    }

    public List<Map<String, Object>> selections(String studentId) {
        return jdbcTemplate.queryForList("""
                SELECT c.hxx_course_name11 AS 课程名称,
                       tea.hxx_teacher_name11 AS 任课教师, term.hxx_school_year11 || ' ' || term.hxx_semester11 AS 学期,
                       sel.hxx_selection_status11 AS 选课状态,
                       CASE WHEN task.hxx_score_publish_flag11 = 'Y' THEN '已发布' ELSE '未发布' END AS 成绩发布状态
                  FROM Huangxx_CourseSelection11 sel
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                  JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11
                  JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11
                 WHERE sel.hxx_student_id11 = ?
                 ORDER BY term.hxx_school_year11 DESC, c.hxx_course_name11
                """, studentId);
    }

    public List<Map<String, Object>> publishedScores(String studentId) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_score_id11 AS scoreId,
                       hxx_course_name11 AS 课程名称,
                       hxx_teacher_name11 AS 任课教师,
                       hxx_school_year11 || ' ' || hxx_semester11 AS 学期,
                       hxx_usual_score11 AS 平时成绩,
                       hxx_exam_score11 AS 期末成绩,
                       hxx_final_score11 AS 总评成绩,
                       hxx_grade_level11 AS 等级,
                       hxx_credit11 AS 学分
                  FROM Huangxx_ViewStudentScore11
                 WHERE hxx_student_id11 = ?
                   AND hxx_publish_flag11 = 'Y'
                   AND hxx_score_status11 <> '作废'
                 ORDER BY hxx_school_year11 DESC, hxx_course_name11
                """, studentId);
    }

    public Map<String, Object> gpa(String studentId) {
        return jdbcTemplate.queryForMap("""
                SELECT hxx_student_id11 AS 学号,
                       hxx_student_name11 AS 姓名,
                       hxx_class_name11 AS 班级,
                       hxx_total_credit11 AS 已修学分,
                       hxx_gpa11 AS GPA,
                       hxx_pass_course_count11 AS 通过课程,
                       hxx_fail_course_count11 AS 未通过课程
                  FROM Huangxx_ViewStudentGpaCredit11
                 WHERE hxx_student_id11 = ?
                """, studentId);
    }

    public List<Map<String, Object>> appeals(String studentId) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_student_name11 AS 学生姓名,
                       hxx_class_name11 AS 班级,
                       hxx_course_name11 AS 课程名称,
                       hxx_teacher_name11 AS 任课教师,
                       hxx_final_score11 AS 当前成绩,
                       hxx_appeal_reason11 AS 申诉理由,
                       hxx_appeal_status11 AS 申诉状态,
                       COALESCE(hxx_handle_result11, '待处理') AS 处理结果,
                       hxx_create_time11 AS 申请时间,
                       hxx_handle_time11 AS 处理时间
                  FROM Huangxx_ViewScoreAppeal11
                 WHERE hxx_student_id11 = ?
                 ORDER BY hxx_create_time11 DESC
                """, studentId);
    }

    public Map<String, Object> scoreForAppeal(String scoreId, String studentId) {
        return jdbcTemplate.queryForMap("""
                SELECT *
                  FROM Huangxx_ViewStudentScore11
                 WHERE hxx_score_id11 = ?
                   AND hxx_student_id11 = ?
                   AND hxx_publish_flag11 = 'Y'
                """, scoreId, studentId);
    }

    public boolean hasPendingAppeal(String scoreId, String studentId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*) FROM Huangxx_ScoreAppeal11
                 WHERE hxx_score_id11 = ?
                   AND hxx_student_id11 = ?
                   AND hxx_appeal_status11 = '待处理'
                """, Integer.class, scoreId, studentId);
        return count != null && count > 0;
    }

    public void addAppeal(String appealId, String scoreId, String studentId, String reason) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_ScoreAppeal11
                (hxx_appeal_id11, hxx_score_id11, hxx_student_id11, hxx_appeal_reason11)
                VALUES (?, ?, ?, ?)
                """, appealId, scoreId, studentId, reason);
    }
}
