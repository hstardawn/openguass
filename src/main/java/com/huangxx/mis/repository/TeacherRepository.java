package com.huangxx.mis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class TeacherRepository {

    private final JdbcTemplate jdbcTemplate;

    public TeacherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> tasks(String teacherId) {
        return jdbcTemplate.queryForList("""
                SELECT task.hxx_task_id11 AS taskId,
                       task.hxx_course_name11 AS 课程名称,
                       task.hxx_class_name11 AS 班级,
                       task.hxx_school_year11 || ' ' || task.hxx_semester11 AS 学期,
                       task.hxx_teaching_place11 AS 上课地点,
                       (SELECT count(*)
                          FROM Huangxx_CourseSelection11 sel
                         WHERE sel.hxx_task_id11 = task.hxx_task_id11
                           AND sel.hxx_selection_status11 <> '退选') || '/' || task.hxx_max_count11 AS 选课人数,
                       task.hxx_task_status11 AS 任务状态,
                       CASE WHEN task.hxx_score_publish_flag11 = 'Y' THEN '已发布' ELSE '未发布' END AS 成绩发布状态
                  FROM Huangxx_ViewTeacherTask11 task
                 WHERE task.hxx_teacher_id11 = ?
                 ORDER BY task.hxx_school_year11 DESC, task.hxx_semester11, task.hxx_course_name11
                """, teacherId);
    }

    public Map<String, Object> task(String taskId, String teacherId) {
        return jdbcTemplate.queryForMap("""
                SELECT task.hxx_course_name11 AS 课程名称,
                       task.hxx_class_name11 AS 班级,
                       task.hxx_school_year11 || ' ' || task.hxx_semester11 AS 学期,
                       task.hxx_teaching_place11 AS 上课地点,
                       (SELECT count(*)
                          FROM Huangxx_CourseSelection11 sel
                         WHERE sel.hxx_task_id11 = task.hxx_task_id11
                           AND sel.hxx_selection_status11 <> '退选') || '/' || task.hxx_max_count11 AS 选课人数,
                       task.hxx_task_status11 AS 任务状态,
                       CASE WHEN task.hxx_score_publish_flag11 = 'Y' THEN '已发布' ELSE '未发布' END AS 成绩发布状态
                  FROM Huangxx_ViewTeacherTask11 task
                 WHERE task.hxx_task_id11 = ? AND task.hxx_teacher_id11 = ?
                """, taskId, teacherId);
    }

    public boolean ownsTask(String taskId, String teacherId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*) FROM Huangxx_TeachingTask11
                 WHERE hxx_task_id11 = ? AND hxx_teacher_id11 = ?
                """, Integer.class, taskId, teacherId);
        return count != null && count > 0;
    }

    public boolean taskEnded(String taskId, String teacherId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*) FROM Huangxx_TeachingTask11
                 WHERE hxx_task_id11 = ?
                   AND hxx_teacher_id11 = ?
                   AND hxx_task_status11 = '已结束'
                """, Integer.class, taskId, teacherId);
        return count != null && count > 0;
    }

    public boolean selectionCanReceiveScore(String selectionId, String teacherId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*)
                  FROM Huangxx_CourseSelection11 sel
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                 WHERE sel.hxx_selection_id11 = ?
                   AND task.hxx_teacher_id11 = ?
                   AND task.hxx_task_status11 = '已结束'
                   AND sel.hxx_selection_status11 <> '退选'
                """, Integer.class, selectionId, teacherId);
        return count != null && count > 0;
    }

    public boolean scoreCanBeEdited(String scoreId, String teacherId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*)
                  FROM Huangxx_Score11 sc
                  JOIN Huangxx_CourseSelection11 sel ON sel.hxx_selection_id11 = sc.hxx_selection_id11
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                 WHERE sc.hxx_score_id11 = ?
                   AND task.hxx_teacher_id11 = ?
                   AND task.hxx_task_status11 = '已结束'
                """, Integer.class, scoreId, teacherId);
        return count != null && count > 0;
    }

    public List<Map<String, Object>> taskStudents(String taskId, String teacherId) {
        return jdbcTemplate.queryForList("""
                SELECT sel.hxx_selection_id11 AS selectionId,
                       sc.hxx_score_id11 AS scoreId,
                       stu.hxx_student_id11 AS 学号,
                       stu.hxx_student_name11 AS 姓名,
                       cls.hxx_class_name11 AS 班级,
                       sel.hxx_selection_status11 AS 选课状态,
                       sc.hxx_usual_score11 AS 平时成绩,
                       sc.hxx_exam_score11 AS 期末成绩,
                       sc.hxx_final_score11 AS 最终成绩,
                       sc.hxx_grade_level11 AS 等级,
                       CASE
                           WHEN sc.hxx_pass_flag11 = 'Y' THEN '通过'
                           WHEN sc.hxx_pass_flag11 = 'N' THEN '未通过'
                           ELSE '未录入'
                       END AS 通过情况,
                       CASE
                           WHEN sc.hxx_publish_flag11 = 'Y' THEN '已发布'
                           WHEN sc.hxx_publish_flag11 = 'N' THEN '未发布'
                           ELSE '未录入'
                       END AS 发布状态
                  FROM Huangxx_CourseSelection11 sel
                  JOIN Huangxx_Student11 stu ON stu.hxx_student_id11 = sel.hxx_student_id11
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = stu.hxx_class_id11
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                  LEFT JOIN Huangxx_Score11 sc ON sc.hxx_selection_id11 = sel.hxx_selection_id11
                 WHERE sel.hxx_task_id11 = ?
                   AND task.hxx_teacher_id11 = ?
                   AND sel.hxx_selection_status11 <> '退选'
                 ORDER BY stu.hxx_student_id11
                """, taskId, teacherId);
    }

    public Map<String, Object> selectionForScore(String selectionId, String teacherId) {
        return jdbcTemplate.queryForMap("""
                SELECT sel.hxx_selection_id11, sel.hxx_task_id11, stu.hxx_student_id11, stu.hxx_student_name11,
                       c.hxx_course_name11, task.hxx_teacher_id11
                  FROM Huangxx_CourseSelection11 sel
                  JOIN Huangxx_Student11 stu ON stu.hxx_student_id11 = sel.hxx_student_id11
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                 WHERE sel.hxx_selection_id11 = ?
                   AND task.hxx_teacher_id11 = ?
                """, selectionId, teacherId);
    }

    public Map<String, Object> score(String scoreId, String teacherId) {
        return jdbcTemplate.queryForMap("""
                SELECT sc.*, sel.hxx_task_id11, stu.hxx_student_id11, stu.hxx_student_name11, c.hxx_course_name11
                  FROM Huangxx_Score11 sc
                  JOIN Huangxx_CourseSelection11 sel ON sel.hxx_selection_id11 = sc.hxx_selection_id11
                  JOIN Huangxx_Student11 stu ON stu.hxx_student_id11 = sel.hxx_student_id11
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                 WHERE sc.hxx_score_id11 = ?
                   AND task.hxx_teacher_id11 = ?
                """, scoreId, teacherId);
    }

    public void addScore(String scoreId, String selectionId, BigDecimal usualScore, BigDecimal examScore, String teacherId) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Score11
                (hxx_score_id11, hxx_selection_id11, hxx_usual_score11, hxx_exam_score11, hxx_input_teacher11)
                VALUES (?, ?, ?, ?, ?)
                """, scoreId, selectionId, usualScore, examScore, teacherId);
        jdbcTemplate.update("""
                UPDATE Huangxx_CourseSelection11
                   SET hxx_selection_status11 = '完成'
                 WHERE hxx_selection_id11 = ?
                   AND hxx_selection_status11 = '已选'
                """, selectionId);
    }

    public void editScore(String scoreId, BigDecimal usualScore, BigDecimal examScore, String teacherId, String reason) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Score11
                   SET hxx_usual_score11 = ?,
                       hxx_exam_score11 = ?,
                       hxx_modifier_id11 = ?,
                       hxx_modifier_role11 = '教师',
                       hxx_modify_reason11 = ?,
                       hxx_score_status11 = '已修改'
                 WHERE hxx_score_id11 = ?
                """, usualScore, examScore, teacherId, reason, scoreId);
    }

    public void publishTaskScores(String taskId, String teacherId) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Score11 sc
                   SET hxx_publish_flag11 = 'Y', hxx_lock_flag11 = 'Y'
                  FROM Huangxx_CourseSelection11 sel
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                 WHERE sc.hxx_selection_id11 = sel.hxx_selection_id11
                   AND sel.hxx_task_id11 = ?
                   AND task.hxx_teacher_id11 = ?
                """, taskId, teacherId);
        jdbcTemplate.update("""
                UPDATE Huangxx_TeachingTask11
                   SET hxx_score_publish_flag11 = 'Y'
                 WHERE hxx_task_id11 = ? AND hxx_teacher_id11 = ?
                """, taskId, teacherId);
    }

    public List<Map<String, Object>> courseStat(String taskId, String teacherId) {
        return jdbcTemplate.queryForList("""
                SELECT stat.hxx_course_name11 AS 课程名称,
                       stat.hxx_teacher_name11 AS 任课教师,
                       stat.hxx_class_name11 AS 班级,
                       stat.hxx_score_count11 AS 成绩人数,
                       stat.hxx_avg_score11 AS 平均分,
                       stat.hxx_max_score11 AS 最高分,
                       stat.hxx_min_score11 AS 最低分,
                       stat.hxx_pass_rate11 AS 通过率,
                       stat.hxx_excellent_rate11 AS 优秀率,
                       stat.hxx_fail_count11 AS 不及格人数
                  FROM Huangxx_ViewCourseStat11 stat
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = stat.hxx_task_id11
                 WHERE stat.hxx_task_id11 = ?
                   AND task.hxx_teacher_id11 = ?
                """, taskId, teacherId);
    }

    public List<Map<String, Object>> appeals(String teacherId) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_appeal_id11 AS appealId,
                       hxx_student_name11 AS 学生姓名,
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
                 WHERE hxx_teacher_id11 = ?
                 ORDER BY hxx_appeal_status11, hxx_create_time11 DESC
                """, teacherId);
    }

    public void handleAppeal(String appealId, String teacherId, String status, String result) {
        int updated = jdbcTemplate.update("""
                UPDATE Huangxx_ScoreAppeal11
                   SET hxx_appeal_status11 = ?,
                       hxx_handler_id11 = ?,
                       hxx_handle_result11 = ?
                 WHERE hxx_appeal_id11 = ?
                   AND EXISTS (
                       SELECT 1
                         FROM Huangxx_Score11 sc
                         JOIN Huangxx_CourseSelection11 sel ON sel.hxx_selection_id11 = sc.hxx_selection_id11
                         JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                        WHERE sc.hxx_score_id11 = Huangxx_ScoreAppeal11.hxx_score_id11
                          AND task.hxx_teacher_id11 = ?
                   )
                """, status, teacherId, result, appealId, teacherId);
        if (updated == 0) {
            throw new IllegalArgumentException("申诉不存在，或不属于当前教师的教学任务");
        }
    }
}
