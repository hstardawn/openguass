package com.huangxx.mis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdminRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> dashboard() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("学生总数", count("Huangxx_Student11"));
        data.put("教师总数", count("Huangxx_Teacher11"));
        data.put("课程总数", count("Huangxx_Course11"));
        data.put("教学任务总数", count("Huangxx_TeachingTask11"));
        data.put("成绩记录总数", count("Huangxx_Score11"));
        data.put("待处理申诉", jdbcTemplate.queryForObject("SELECT count(*) FROM Huangxx_ScoreAppeal11 WHERE hxx_appeal_status11 = '待处理'", Long.class));
        return data;
    }

    public List<Map<String, Object>> students() {
        return jdbcTemplate.queryForList("""
                SELECT s.hxx_student_id11 AS 学号, s.hxx_student_name11 AS 姓名, s.hxx_gender11 AS 性别,
                       cls.hxx_class_name11 AS 班级, r.hxx_region_name11 AS 生源地,
                       s.hxx_total_credit11 AS 已修学分, s.hxx_gpa11 AS GPA, s.hxx_status11 AS 学籍状态
                  FROM Huangxx_Student11 s
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = s.hxx_class_id11
                  JOIN Huangxx_Region11 r ON r.hxx_region_id11 = s.hxx_region_id11
                 ORDER BY s.hxx_student_id11
                """);
    }

    public List<Map<String, Object>> teachers() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_teacher_name11 AS 姓名, hxx_gender11 AS 性别,
                       hxx_title11 AS 职称, hxx_phone11 AS 联系电话, hxx_college_name11 AS 所属学院,
                       hxx_status11 AS 状态
                  FROM Huangxx_Teacher11
                 ORDER BY hxx_teacher_id11
                """);
    }

    public List<Map<String, Object>> courses() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_course_name11 AS 课程名称, hxx_credit11 AS 学分,
                       hxx_period11 AS 学时, hxx_exam_type11 AS 考核方式, hxx_course_type11 AS 课程类型
                  FROM Huangxx_Course11
                 ORDER BY hxx_course_id11
                """);
    }

    public List<Map<String, Object>> majors() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_major_name11 AS 专业名称,
                       hxx_college_name11 AS 所属学院, hxx_major_desc11 AS 专业说明
                  FROM Huangxx_Major11
                 ORDER BY hxx_major_id11
                """);
    }

    public List<Map<String, Object>> classes() {
        return jdbcTemplate.queryForList("""
                SELECT cls.hxx_class_name11 AS 班级名称,
                       major.hxx_major_name11 AS 专业, cls.hxx_grade_year11 AS 入学年份,
                       cls.hxx_class_size11 AS 班级人数
                  FROM Huangxx_Class11 cls
                  JOIN Huangxx_Major11 major ON major.hxx_major_id11 = cls.hxx_major_id11
                 ORDER BY cls.hxx_class_id11
                """);
    }

    public List<Map<String, Object>> tasks() {
        return jdbcTemplate.queryForList("""
                SELECT c.hxx_course_name11 AS 课程名称,
                       tea.hxx_teacher_name11 AS 任课教师, cls.hxx_class_name11 AS 班级,
                       term.hxx_school_year11 || ' ' || term.hxx_semester11 AS 学期,
                       task.hxx_teaching_place11 AS 上课地点, task.hxx_max_count11 AS 最大人数,
                       task.hxx_current_count11 AS 当前人数,
                       CASE WHEN task.hxx_score_publish_flag11 = 'Y' THEN '已发布' ELSE '未发布' END AS 成绩发布状态
                  FROM Huangxx_TeachingTask11 task
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                  JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = task.hxx_class_id11
                  JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11
                 ORDER BY task.hxx_task_id11
                """);
    }

    public List<Map<String, Object>> courseStats() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_course_name11 AS 课程名称,
                       hxx_teacher_name11 AS 任课教师,
                       hxx_class_name11 AS 班级,
                       hxx_score_count11 AS 成绩人数,
                       hxx_avg_score11 AS 平均分,
                       hxx_max_score11 AS 最高分,
                       hxx_min_score11 AS 最低分,
                       hxx_pass_rate11 AS 通过率,
                       hxx_excellent_rate11 AS 优秀率,
                       hxx_fail_count11 AS 不及格人数
                  FROM Huangxx_ViewCourseStat11
                 ORDER BY hxx_course_name11, hxx_class_name11
                """);
    }

    public List<Map<String, Object>> classCredits() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_student_name11 AS 学生姓名,
                       hxx_class_name11 AS 班级,
                       hxx_total_credit11 AS 已修学分,
                       hxx_gpa11 AS GPA,
                       hxx_pass_course_count11 AS 通过课程数,
                       hxx_fail_course_count11 AS 未通过课程数
                  FROM Huangxx_ViewStudentGpaCredit11
                 ORDER BY hxx_class_name11, hxx_gpa11 DESC, hxx_student_name11
                """);
    }

    public List<Map<String, Object>> regionStats() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_region_name11 AS 生源地,
                       hxx_region_level11 AS 地区层级,
                       COALESCE(hxx_parent_region_name11, '无') AS 上级地区,
                       hxx_student_count11 AS 学生人数
                  FROM Huangxx_ViewRegionStudentStat11
                 ORDER BY hxx_region_level11, hxx_region_name11
                """);
    }

    public List<Map<String, Object>> scoreRanks() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_rank_scope11 AS 排名范围,
                       hxx_student_name11 AS 学生姓名,
                       hxx_class_name11 AS 班级,
                       hxx_major_name11 AS 专业,
                       hxx_grade_year11 AS 年级,
                       hxx_course_name11 AS 课程名称,
                       hxx_teacher_name11 AS 任课教师,
                       hxx_school_year11 AS 学年,
                       hxx_semester11 AS 学期,
                       hxx_final_score11 AS 总评成绩,
                       hxx_grade_level11 AS 等级,
                       hxx_course_rank11 AS 教学班排名,
                       hxx_class_rank11 AS 班级学期排名,
                       hxx_major_grade_rank11 AS 专业年级排名
                  FROM Huangxx_ViewScoreRank11
                 ORDER BY hxx_school_year11 DESC, hxx_semester11, hxx_class_name11, hxx_course_name11, hxx_course_rank11
                """);
    }

    public List<Map<String, Object>> scoreRanksByClass(String classId) {
        String filter = classId == null || classId.isBlank() ? "" : " WHERE hxx_class_id11 = ?";
        Object[] args = classId == null || classId.isBlank() ? new Object[]{} : new Object[]{classId};
        return jdbcTemplate.queryForList("""
                SELECT hxx_rank_scope11 AS 排名范围,
                       hxx_student_name11 AS 学生姓名,
                       hxx_class_name11 AS 班级,
                       hxx_course_name11 AS 课程名称,
                       hxx_teacher_name11 AS 任课教师,
                       hxx_school_year11 AS 学年,
                       hxx_semester11 AS 学期,
                       hxx_final_score11 AS 总评成绩,
                       hxx_grade_level11 AS 等级,
                       hxx_course_rank11 AS 教学班排名,
                       hxx_class_rank11 AS 班级学期排名
                  FROM Huangxx_ViewScoreRank11
                """ + filter + """
                 ORDER BY hxx_school_year11 DESC, hxx_semester11, hxx_class_name11, hxx_course_name11, hxx_course_rank11
                """, args);
    }

    public List<Map<String, Object>> scoreRanksByMajor(String majorId, Integer gradeYear) {
        StringBuilder sql = new StringBuilder("""
                SELECT hxx_major_name11 AS 专业,
                       hxx_grade_year11 AS 年级,
                       hxx_school_year11 AS 学年,
                       hxx_semester11 AS 学期,
                       hxx_student_name11 AS 学生姓名,
                       hxx_class_name11 AS 班级,
                       hxx_course_name11 AS 课程名称,
                       hxx_final_score11 AS 总评成绩,
                       hxx_grade_level11 AS 等级,
                       hxx_major_grade_rank11 AS 专业年级排名
                 FROM Huangxx_ViewScoreRank11
                 WHERE 1 = 1
                """);
        java.util.ArrayList<Object> args = new java.util.ArrayList<>();
        if (majorId != null && !majorId.isBlank()) {
            sql.append(" AND hxx_major_id11 = ?");
            args.add(majorId);
        }
        if (gradeYear != null) {
            sql.append(" AND hxx_grade_year11 = ?");
            args.add(gradeYear);
        }
        sql.append(" ORDER BY hxx_school_year11 DESC, hxx_semester11, hxx_major_name11, hxx_grade_year11, hxx_major_grade_rank11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> classOptions() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_class_id11 AS id,
                       hxx_class_name11 AS name
                  FROM Huangxx_Class11
                 ORDER BY hxx_grade_year11 DESC, hxx_class_name11
                """);
    }

    public List<Map<String, Object>> majorOptions() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_major_id11 AS id,
                       hxx_major_name11 AS name
                  FROM Huangxx_Major11
                 ORDER BY hxx_major_name11
                """);
    }

    public List<Integer> gradeYears() {
        return jdbcTemplate.queryForList("""
                SELECT DISTINCT hxx_grade_year11
                  FROM Huangxx_Class11
                 ORDER BY hxx_grade_year11 DESC
                """, Integer.class);
    }

    public List<Map<String, Object>> scoreAudits() {
        return jdbcTemplate.queryForList("""
                SELECT c.hxx_course_name11 AS 课程名称,
                       stu.hxx_student_name11 AS 学生姓名,
                       cls.hxx_class_name11 AS 班级,
                       audit.hxx_old_usual_score11 AS 修改前平时成绩,
                       audit.hxx_new_usual_score11 AS 修改后平时成绩,
                       audit.hxx_old_exam_score11 AS 修改前考试成绩,
                       audit.hxx_new_exam_score11 AS 修改后考试成绩,
                       audit.hxx_old_score11 AS 修改前总评成绩,
                       audit.hxx_new_score11 AS 修改后总评成绩,
                       COALESCE(tea.hxx_teacher_name11, sys.hxx_login_name11, audit.hxx_operator_role11, '系统用户') AS 操作人,
                       audit.hxx_operator_role11 AS 角色,
                       audit.hxx_modify_reason11 AS 修改原因,
                       audit.hxx_modify_time11 AS 修改时间
                  FROM Huangxx_ScoreAudit11 audit
                  JOIN Huangxx_Score11 sc ON sc.hxx_score_id11 = audit.hxx_score_id11
                  JOIN Huangxx_CourseSelection11 sel ON sel.hxx_selection_id11 = sc.hxx_selection_id11
                  JOIN Huangxx_Student11 stu ON stu.hxx_student_id11 = sel.hxx_student_id11
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = stu.hxx_class_id11
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                  LEFT JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = audit.hxx_operator_id11
                  LEFT JOIN Huangxx_SystemUser11 sys ON sys.hxx_user_id11 = audit.hxx_operator_id11
                                             OR sys.hxx_ref_id11 = audit.hxx_operator_id11
                                             OR sys.hxx_login_name11 = audit.hxx_operator_id11
                 ORDER BY audit.hxx_modify_time11 DESC
                """);
    }

    public List<Map<String, Object>> operationLogs() {
        return jdbcTemplate.queryForList("""
                SELECT COALESCE(tea.hxx_teacher_name11, stu_user.hxx_student_name11, sys.hxx_login_name11, log.hxx_login_name11, '系统用户') AS 操作人,
                       log.hxx_role11 AS 角色,
                       log.hxx_operation_type11 AS 操作类型,
                       CASE
                           WHEN appeal.hxx_appeal_id11 IS NOT NULL THEN appeal_stu.hxx_student_name11 || ' / ' || appeal_course.hxx_course_name11 || ' 成绩申诉'
                           WHEN score.hxx_score_id11 IS NOT NULL THEN score_stu.hxx_student_name11 || ' / ' || score_course.hxx_course_name11 || ' 成绩记录'
                           WHEN task.hxx_task_id11 IS NOT NULL THEN task_class.hxx_class_name11 || ' / ' || task_course.hxx_course_name11 || ' 教学任务'
                           WHEN sel.hxx_selection_id11 IS NOT NULL THEN sel_stu.hxx_student_name11 || ' / ' || sel_course.hxx_course_name11 || ' 选课记录'
                           WHEN log.hxx_operation_object11 = 'Huangxx_SystemUser11' THEN '系统登录'
                           ELSE COALESCE(log.hxx_operation_desc11, '业务操作')
                       END AS 业务对象,
                       log.hxx_operation_desc11 AS 业务描述,
                       log.hxx_operation_result11 AS 操作结果,
                       log.hxx_operation_time11 AS 操作时间
                  FROM Huangxx_OperationLog11 log
                  LEFT JOIN Huangxx_SystemUser11 sys ON sys.hxx_user_id11 = log.hxx_user_id11
                                              OR sys.hxx_login_name11 = log.hxx_login_name11
                  LEFT JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = COALESCE(sys.hxx_ref_id11, log.hxx_user_id11)
                  LEFT JOIN Huangxx_Student11 stu_user ON stu_user.hxx_student_id11 = COALESCE(sys.hxx_ref_id11, log.hxx_user_id11)
                  LEFT JOIN Huangxx_ScoreAppeal11 appeal ON appeal.hxx_appeal_id11 = log.hxx_operation_object11
                  LEFT JOIN Huangxx_Student11 appeal_stu ON appeal_stu.hxx_student_id11 = appeal.hxx_student_id11
                  LEFT JOIN Huangxx_Score11 appeal_score ON appeal_score.hxx_score_id11 = appeal.hxx_score_id11
                  LEFT JOIN Huangxx_CourseSelection11 appeal_sel ON appeal_sel.hxx_selection_id11 = appeal_score.hxx_selection_id11
                  LEFT JOIN Huangxx_TeachingTask11 appeal_task ON appeal_task.hxx_task_id11 = appeal_sel.hxx_task_id11
                  LEFT JOIN Huangxx_Course11 appeal_course ON appeal_course.hxx_course_id11 = appeal_task.hxx_course_id11
                  LEFT JOIN Huangxx_Score11 score ON score.hxx_score_id11 = log.hxx_operation_object11
                  LEFT JOIN Huangxx_CourseSelection11 score_sel ON score_sel.hxx_selection_id11 = score.hxx_selection_id11
                  LEFT JOIN Huangxx_Student11 score_stu ON score_stu.hxx_student_id11 = score_sel.hxx_student_id11
                  LEFT JOIN Huangxx_TeachingTask11 score_task ON score_task.hxx_task_id11 = score_sel.hxx_task_id11
                  LEFT JOIN Huangxx_Course11 score_course ON score_course.hxx_course_id11 = score_task.hxx_course_id11
                  LEFT JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = log.hxx_operation_object11
                  LEFT JOIN Huangxx_Class11 task_class ON task_class.hxx_class_id11 = task.hxx_class_id11
                  LEFT JOIN Huangxx_Course11 task_course ON task_course.hxx_course_id11 = task.hxx_course_id11
                  LEFT JOIN Huangxx_CourseSelection11 sel ON sel.hxx_selection_id11 = log.hxx_operation_object11
                  LEFT JOIN Huangxx_Student11 sel_stu ON sel_stu.hxx_student_id11 = sel.hxx_student_id11
                  LEFT JOIN Huangxx_TeachingTask11 sel_task ON sel_task.hxx_task_id11 = sel.hxx_task_id11
                  LEFT JOIN Huangxx_Course11 sel_course ON sel_course.hxx_course_id11 = sel_task.hxx_course_id11
                 ORDER BY log.hxx_operation_time11 DESC
                """);
    }

    public Map<String, Object> student(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Student11 WHERE hxx_student_id11 = ?", id);
    }

    public Map<String, Object> teacher(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Teacher11 WHERE hxx_teacher_id11 = ?", id);
    }

    public Map<String, Object> course(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Course11 WHERE hxx_course_id11 = ?", id);
    }

    public Map<String, Object> task(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_TeachingTask11 WHERE hxx_task_id11 = ?", id);
    }

    public void saveStudent(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Student11
                (hxx_student_id11, hxx_student_name11, hxx_gender11, hxx_age11, hxx_region_id11,
                 hxx_class_id11, hxx_phone11, hxx_status11, hxx_enroll_date11)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, to_date(?, 'YYYY-MM-DD'))
                """,
                form.get("studentId"), form.get("studentName"), form.get("gender"), Integer.parseInt(form.get("age")),
                form.get("regionId"), form.get("classId"), form.get("phone"), form.get("status"), form.get("enrollDate"));
    }

    public int importStudents(List<Map<String, String>> rows) {
        int imported = 0;
        for (Map<String, String> row : rows) {
            saveStudent(row);
            imported++;
        }
        return imported;
    }

    public void updateStudent(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Student11
                   SET hxx_student_name11 = ?, hxx_gender11 = ?, hxx_age11 = ?, hxx_region_id11 = ?,
                       hxx_class_id11 = ?, hxx_phone11 = ?, hxx_status11 = ?, hxx_enroll_date11 = to_date(?, 'YYYY-MM-DD')
                 WHERE hxx_student_id11 = ?
                """,
                form.get("studentName"), form.get("gender"), Integer.parseInt(form.get("age")),
                form.get("regionId"), form.get("classId"), form.get("phone"), form.get("status"),
                form.get("enrollDate"), form.get("studentId"));
    }

    public void deleteStudent(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_Student11 WHERE hxx_student_id11 = ?", id);
    }

    public void saveTeacher(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Teacher11
                (hxx_teacher_id11, hxx_teacher_name11, hxx_gender11, hxx_age11, hxx_title11,
                 hxx_phone11, hxx_college_name11, hxx_status11)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                form.get("teacherId"), form.get("teacherName"), form.get("gender"), Integer.parseInt(form.get("age")),
                form.get("title"), form.get("phone"), form.get("collegeName"), form.get("status"));
    }

    public void updateTeacher(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Teacher11
                   SET hxx_teacher_name11 = ?, hxx_gender11 = ?, hxx_age11 = ?, hxx_title11 = ?,
                       hxx_phone11 = ?, hxx_college_name11 = ?, hxx_status11 = ?
                 WHERE hxx_teacher_id11 = ?
                """,
                form.get("teacherName"), form.get("gender"), Integer.parseInt(form.get("age")), form.get("title"),
                form.get("phone"), form.get("collegeName"), form.get("status"), form.get("teacherId"));
    }

    public void deleteTeacher(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_Teacher11 WHERE hxx_teacher_id11 = ?", id);
    }

    public void saveCourse(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Course11
                (hxx_course_id11, hxx_course_name11, hxx_credit11, hxx_period11,
                 hxx_exam_type11, hxx_course_type11, hxx_course_desc11)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                form.get("courseId"), form.get("courseName"), decimal(form.get("credit")), Integer.parseInt(form.get("period")),
                form.get("examType"), form.get("courseType"), form.get("courseDesc"));
    }

    public void updateCourse(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Course11
                   SET hxx_course_name11 = ?, hxx_credit11 = ?, hxx_period11 = ?,
                       hxx_exam_type11 = ?, hxx_course_type11 = ?, hxx_course_desc11 = ?
                 WHERE hxx_course_id11 = ?
                """,
                form.get("courseName"), decimal(form.get("credit")), Integer.parseInt(form.get("period")),
                form.get("examType"), form.get("courseType"), form.get("courseDesc"), form.get("courseId"));
    }

    public void deleteCourse(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_Course11 WHERE hxx_course_id11 = ?", id);
    }

    public void saveTask(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_TeachingTask11
                (hxx_task_id11, hxx_course_id11, hxx_teacher_id11, hxx_class_id11,
                 hxx_term_id11, hxx_teaching_place11, hxx_max_count11, hxx_task_status11)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                form.get("taskId"), form.get("courseId"), form.get("teacherId"), form.get("classId"),
                form.get("termId"), form.get("teachingPlace"), Integer.parseInt(form.get("maxCount")), form.get("taskStatus"));
    }

    public void updateTask(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_TeachingTask11
                   SET hxx_course_id11 = ?, hxx_teacher_id11 = ?, hxx_class_id11 = ?, hxx_term_id11 = ?,
                       hxx_teaching_place11 = ?, hxx_max_count11 = ?, hxx_task_status11 = ?
                 WHERE hxx_task_id11 = ?
                """,
                form.get("courseId"), form.get("teacherId"), form.get("classId"), form.get("termId"),
                form.get("teachingPlace"), Integer.parseInt(form.get("maxCount")), form.get("taskStatus"), form.get("taskId"));
    }

    public List<Map<String, Object>> options(String table, String idColumn, String nameColumn) {
        return jdbcTemplate.queryForList("SELECT " + idColumn + " AS id, " + nameColumn + " AS name FROM " + table + " ORDER BY " + idColumn);
    }

    private long count(String table) {
        Long value = jdbcTemplate.queryForObject("SELECT count(*) FROM " + table, Long.class);
        return value == null ? 0 : value;
    }

    private java.math.BigDecimal decimal(String value) {
        return new java.math.BigDecimal(value);
    }
}
