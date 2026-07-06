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
                SELECT sel.hxx_selection_id11 AS selectionId,
                       c.hxx_course_name11 AS 课程名称,
                       tea.hxx_teacher_name11 AS 任课教师, term.hxx_school_year11 || ' ' || term.hxx_semester11 AS 学期,
                       task.hxx_teaching_place11 AS 上课地点,
                       sel.hxx_selection_status11 AS 选课状态,
                       CASE WHEN task.hxx_score_publish_flag11 = 'Y' THEN '已发布' ELSE '未发布' END AS 成绩发布状态
                  FROM Huangxx_CourseSelection11 sel
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                 JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11
                 JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11
                 WHERE sel.hxx_student_id11 = ?
                   AND sel.hxx_selection_status11 <> '退选'
                 ORDER BY term.hxx_school_year11 DESC, c.hxx_course_name11
                """, studentId);
    }

    public List<Map<String, Object>> availableCourses(String studentId) {
        return jdbcTemplate.queryForList("""
                SELECT task.hxx_task_id11 AS taskId,
                       sel.hxx_selection_id11 AS selectionId,
                       c.hxx_course_name11 AS 课程名称,
                       c.hxx_credit11 AS 学分,
                       tea.hxx_teacher_name11 AS 任课教师,
                       cls.hxx_class_name11 AS 班级,
                       term.hxx_school_year11 || ' ' || term.hxx_semester11 AS 学期,
                       task.hxx_teaching_place11 AS 上课地点,
                       (SELECT count(*)
                          FROM Huangxx_CourseSelection11 active_sel
                         WHERE active_sel.hxx_task_id11 = task.hxx_task_id11
                           AND active_sel.hxx_selection_status11 <> '退选') || '/' || task.hxx_max_count11 AS 人数,
                       task.hxx_task_status11 AS 开课状态,
                       CASE
                           WHEN task.hxx_task_status11 <> '开课中' THEN '暂未开放'
                           WHEN sel.hxx_selection_status11 IS NULL THEN '可选'
                           WHEN sel.hxx_selection_status11 = '退选' THEN '可重新选择'
                           ELSE '已选'
                       END AS 选课状态
                  FROM Huangxx_TeachingTask11 task
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                  JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = task.hxx_class_id11
                  JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11
                  JOIN Huangxx_Student11 stu ON stu.hxx_class_id11 = task.hxx_class_id11
                  LEFT JOIN Huangxx_CourseSelection11 sel
                    ON sel.hxx_task_id11 = task.hxx_task_id11
                   AND sel.hxx_student_id11 = stu.hxx_student_id11
                 WHERE stu.hxx_student_id11 = ?
                   AND task.hxx_task_status11 IN ('未开始', '开课中')
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

    public boolean taskExistsForStudentClass(String taskId, String studentId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*)
                  FROM Huangxx_TeachingTask11 task
                  JOIN Huangxx_Student11 stu ON stu.hxx_class_id11 = task.hxx_class_id11
                 WHERE task.hxx_task_id11 = ?
                   AND stu.hxx_student_id11 = ?
                   AND task.hxx_task_status11 = '开课中'
                """, Integer.class, taskId, studentId);
        return count != null && count > 0;
    }

    public boolean hasActiveSelection(String taskId, String studentId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*) FROM Huangxx_CourseSelection11
                 WHERE hxx_task_id11 = ?
                   AND hxx_student_id11 = ?
                   AND hxx_selection_status11 <> '退选'
                """, Integer.class, taskId, studentId);
        return count != null && count > 0;
    }

    public boolean selectionHasScore(String selectionId, String studentId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*)
                  FROM Huangxx_CourseSelection11 sel
                  JOIN Huangxx_Score11 sc ON sc.hxx_selection_id11 = sel.hxx_selection_id11
                 WHERE sel.hxx_selection_id11 = ?
                   AND sel.hxx_student_id11 = ?
                   AND sc.hxx_score_status11 <> '作废'
                """, Integer.class, selectionId, studentId);
        return count != null && count > 0;
    }

    public void selectCourse(String selectionId, String studentId, String taskId) {
        int updated = jdbcTemplate.update("""
                UPDATE Huangxx_CourseSelection11
                   SET hxx_selection_status11 = '已选',
                       hxx_select_time11 = current_timestamp
                 WHERE hxx_student_id11 = ?
                   AND hxx_task_id11 = ?
                   AND hxx_selection_status11 = '退选'
                """, studentId, taskId);
        if (updated == 0) {
            jdbcTemplate.update("""
                    INSERT INTO Huangxx_CourseSelection11
                    (hxx_selection_id11, hxx_student_id11, hxx_task_id11, hxx_selection_status11)
                    VALUES (?, ?, ?, '已选')
                    """, selectionId, studentId, taskId);
        }
    }

    public void dropSelection(String selectionId, String studentId) {
        int updated = jdbcTemplate.update("""
                UPDATE Huangxx_CourseSelection11
                   SET hxx_selection_status11 = '退选'
                 WHERE hxx_selection_id11 = ?
                   AND hxx_student_id11 = ?
                   AND hxx_selection_status11 = '已选'
                """, selectionId, studentId);
        if (updated == 0) {
            throw new IllegalArgumentException("选课记录不存在，或当前状态不允许退选");
        }
    }

    public void addAppeal(String appealId, String scoreId, String studentId, String reason) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_ScoreAppeal11
                (hxx_appeal_id11, hxx_score_id11, hxx_student_id11, hxx_appeal_reason11)
                VALUES (?, ?, ?, ?)
                """, appealId, scoreId, studentId, reason);
    }
}
