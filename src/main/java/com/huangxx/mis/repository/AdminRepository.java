package com.huangxx.mis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public List<Map<String, Object>> students(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT s.hxx_student_id11 AS rowId,
                       '/admin/students/edit/' || s.hxx_student_id11 AS editPath,
                       '/admin/students/delete/' || s.hxx_student_id11 AS deletePath,
                       s.hxx_student_id11 AS 学号, s.hxx_student_name11 AS 姓名, s.hxx_gender11 AS 性别,
                       cls.hxx_class_name11 AS 班级, r.hxx_region_name11 AS 生源地,
                       s.hxx_total_credit11 AS 已修学分, s.hxx_gpa11 AS GPA, s.hxx_status11 AS 学籍状态
                  FROM Huangxx_Student11 s
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = s.hxx_class_id11
                  JOIN Huangxx_Region11 r ON r.hxx_region_id11 = s.hxx_region_id11
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND (s.hxx_student_id11 LIKE ? OR s.hxx_student_name11 LIKE ?)");
            args.add(like(filters.get("keyword")));
            args.add(like(filters.get("keyword")));
        }
        if (!isBlank(filters.get("classId"))) {
            sql.append(" AND s.hxx_class_id11 = ?");
            args.add(filters.get("classId"));
        }
        if (!isBlank(filters.get("majorId"))) {
            sql.append(" AND cls.hxx_major_id11 = ?");
            args.add(filters.get("majorId"));
        }
        if (!isBlank(filters.get("status"))) {
            sql.append(" AND s.hxx_status11 = ?");
            args.add(filters.get("status"));
        }
        if (!isBlank(filters.get("regionId"))) {
            sql.append(" AND s.hxx_region_id11 = ?");
            args.add(filters.get("regionId"));
        }
        sql.append(" ORDER BY s.hxx_student_id11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> teachers(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT tea.hxx_teacher_id11 AS rowId,
                       '/admin/teachers/edit/' || tea.hxx_teacher_id11 AS editPath,
                       '/admin/teachers/delete/' || tea.hxx_teacher_id11 AS deletePath,
                       tea.hxx_teacher_name11 AS 姓名, tea.hxx_gender11 AS 性别,
                       tea.hxx_title11 AS 职称, tea.hxx_phone11 AS 联系电话, tea.hxx_college_name11 AS 所属学院,
                       CASE WHEN admin_user.hxx_user_id11 IS NULL THEN '否' ELSE '是' END AS 管理员权限,
                       tea.hxx_status11 AS 状态
                  FROM Huangxx_Teacher11 tea
                  LEFT JOIN Huangxx_SystemUser11 admin_user
                    ON admin_user.hxx_ref_id11 = tea.hxx_teacher_id11
                   AND admin_user.hxx_role11 = '管理员'
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND (tea.hxx_teacher_id11 LIKE ? OR tea.hxx_teacher_name11 LIKE ?)");
            args.add(like(filters.get("keyword")));
            args.add(like(filters.get("keyword")));
        }
        if (!isBlank(filters.get("collegeName"))) {
            sql.append(" AND tea.hxx_college_name11 LIKE ?");
            args.add(like(filters.get("collegeName")));
        }
        if (!isBlank(filters.get("title"))) {
            sql.append(" AND tea.hxx_title11 = ?");
            args.add(filters.get("title"));
        }
        if (!isBlank(filters.get("adminFlag"))) {
            if ("Y".equals(filters.get("adminFlag"))) {
                sql.append(" AND admin_user.hxx_user_id11 IS NOT NULL");
            } else if ("N".equals(filters.get("adminFlag"))) {
                sql.append(" AND admin_user.hxx_user_id11 IS NULL");
            }
        }
        sql.append(" ORDER BY tea.hxx_teacher_id11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> courses(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT hxx_course_id11 AS rowId,
                       '/admin/courses/edit/' || hxx_course_id11 AS editPath,
                       '/admin/courses/delete/' || hxx_course_id11 AS deletePath,
                       hxx_course_name11 AS 课程名称, hxx_credit11 AS 学分,
                       hxx_period11 AS 学时, hxx_exam_type11 AS 考核方式, hxx_course_type11 AS 课程类型
                  FROM Huangxx_Course11
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND hxx_course_name11 LIKE ?");
            args.add(like(filters.get("keyword")));
        }
        if (!isBlank(filters.get("examType"))) {
            sql.append(" AND hxx_exam_type11 = ?");
            args.add(filters.get("examType"));
        }
        if (!isBlank(filters.get("courseType"))) {
            sql.append(" AND hxx_course_type11 = ?");
            args.add(filters.get("courseType"));
        }
        sql.append(" ORDER BY hxx_course_id11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> majors(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT hxx_major_id11 AS rowId,
                       '/admin/majors/edit/' || hxx_major_id11 AS editPath,
                       '/admin/majors/delete/' || hxx_major_id11 AS deletePath,
                       hxx_major_name11 AS 专业名称,
                       hxx_college_name11 AS 所属学院, hxx_major_desc11 AS 专业说明
                  FROM Huangxx_Major11
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND hxx_major_name11 LIKE ?");
            args.add(like(filters.get("keyword")));
        }
        sql.append(" ORDER BY hxx_major_id11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> classes(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT cls.hxx_class_id11 AS rowId,
                       '/admin/classes/edit/' || cls.hxx_class_id11 AS editPath,
                       '/admin/classes/delete/' || cls.hxx_class_id11 AS deletePath,
                       cls.hxx_class_name11 AS 班级名称,
                       major.hxx_major_name11 AS 专业, cls.hxx_grade_year11 AS 入学年份,
                       cls.hxx_class_size11 AS 班级人数
                  FROM Huangxx_Class11 cls
                  JOIN Huangxx_Major11 major ON major.hxx_major_id11 = cls.hxx_major_id11
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND cls.hxx_class_name11 LIKE ?");
            args.add(like(filters.get("keyword")));
        }
        if (!isBlank(filters.get("majorId"))) {
            sql.append(" AND cls.hxx_major_id11 = ?");
            args.add(filters.get("majorId"));
        }
        sql.append(" ORDER BY cls.hxx_class_id11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> regions(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT r.hxx_region_id11 AS rowId,
                       r.hxx_region_name11 AS 地区名称,
                       r.hxx_region_level11 AS 地区层级,
                       COALESCE(parent.hxx_region_name11, '无') AS 上级地区,
                       COALESCE(r.hxx_region_remark11, '') AS 备注
                  FROM Huangxx_Region11 r
                  LEFT JOIN Huangxx_Region11 parent ON parent.hxx_region_id11 = r.hxx_parent_region_id11
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND (r.hxx_region_name11 LIKE ? OR parent.hxx_region_name11 LIKE ?)");
            args.add(like(filters.get("keyword")));
            args.add(like(filters.get("keyword")));
        }
        if (!isBlank(filters.get("level"))) {
            sql.append(" AND r.hxx_region_level11 = ?");
            args.add(filters.get("level"));
        }
        if (!isBlank(filters.get("parentId"))) {
            sql.append(" AND r.hxx_parent_region_id11 = ?");
            args.add(filters.get("parentId"));
        }
        sql.append("""
                 ORDER BY CASE r.hxx_region_level11 WHEN '省' THEN 1 WHEN '市' THEN 2 ELSE 3 END,
                          COALESCE(parent.hxx_region_name11, r.hxx_region_name11),
                          r.hxx_region_name11
                """);
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> tasks(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT task.hxx_task_id11 AS rowId,
                       '/admin/tasks/edit/' || task.hxx_task_id11 AS editPath,
                       '/admin/tasks/delete/' || task.hxx_task_id11 AS deletePath,
                       c.hxx_course_name11 AS 课程名称,
                       tea.hxx_teacher_name11 AS 任课教师, cls.hxx_class_name11 AS 班级,
                       term.hxx_school_year11 || ' ' || term.hxx_semester11 AS 学期,
                       task.hxx_teaching_place11 AS 上课地点, task.hxx_max_count11 AS 最大人数,
                       (SELECT count(*)
                          FROM Huangxx_CourseSelection11 sel
                         WHERE sel.hxx_task_id11 = task.hxx_task_id11
                           AND sel.hxx_selection_status11 <> '退选') AS 当前人数,
                       task.hxx_task_status11 AS 任务状态,
                       CASE WHEN task.hxx_score_publish_flag11 = 'Y' THEN '已发布' ELSE '未发布' END AS 成绩发布状态
                  FROM Huangxx_TeachingTask11 task
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                  JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = task.hxx_class_id11
                  JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("teacherId"))) {
            sql.append(" AND task.hxx_teacher_id11 = ?");
            args.add(filters.get("teacherId"));
        }
        if (!isBlank(filters.get("courseId"))) {
            sql.append(" AND task.hxx_course_id11 = ?");
            args.add(Integer.parseInt(filters.get("courseId")));
        }
        if (!isBlank(filters.get("classId"))) {
            sql.append(" AND task.hxx_class_id11 = ?");
            args.add(filters.get("classId"));
        }
        if (!isBlank(filters.get("termId"))) {
            sql.append(" AND task.hxx_term_id11 = ?");
            args.add(filters.get("termId"));
        }
        sql.append(" ORDER BY task.hxx_task_id11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> terms(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT hxx_term_id11 AS rowId,
                       hxx_school_year11 AS 学年,
                       hxx_semester11 AS 学期,
                       hxx_start_date11 AS 开始日期,
                       hxx_end_date11 AS 结束日期,
                       hxx_term_status11 AS 状态
                  FROM Huangxx_Term11
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND (hxx_term_id11 LIKE ? OR hxx_school_year11 LIKE ? OR hxx_semester11 LIKE ?)");
            args.add(like(filters.get("keyword")));
            args.add(like(filters.get("keyword")));
            args.add(like(filters.get("keyword")));
        }
        if (!isBlank(filters.get("status"))) {
            sql.append(" AND hxx_term_status11 = ?");
            args.add(filters.get("status"));
        }
        sql.append(" ORDER BY hxx_school_year11 DESC, hxx_start_date11 DESC");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    public List<Map<String, Object>> users(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                SELECT u.hxx_user_id11 AS rowId,
                       u.hxx_login_name11 AS 登录名,
                       u.hxx_role11 AS 角色,
                       COALESCE(stu.hxx_student_name11, tea.hxx_teacher_name11, '系统管理员') AS 关联人员,
                       u.hxx_user_status11 AS 状态,
                       u.hxx_create_time11 AS 创建时间,
                       u.hxx_last_login_time11 AS 最后登录时间
                  FROM Huangxx_SystemUser11 u
                  LEFT JOIN Huangxx_Student11 stu ON stu.hxx_student_id11 = u.hxx_ref_id11
                  LEFT JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = u.hxx_ref_id11
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND (u.hxx_login_name11 LIKE ? OR stu.hxx_student_name11 LIKE ? OR tea.hxx_teacher_name11 LIKE ?)");
            args.add(like(filters.get("keyword")));
            args.add(like(filters.get("keyword")));
            args.add(like(filters.get("keyword")));
        }
        if (!isBlank(filters.get("role"))) {
            sql.append(" AND u.hxx_role11 = ?");
            args.add(filters.get("role"));
        }
        if (!isBlank(filters.get("status"))) {
            sql.append(" AND u.hxx_user_status11 = ?");
            args.add(filters.get("status"));
        }
        sql.append(" ORDER BY u.hxx_role11, u.hxx_login_name11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
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

    public List<Map<String, Object>> scoreDistribution() {
        return jdbcTemplate.queryForList("""
                SELECT c.hxx_course_name11 AS 课程名称,
                       cls.hxx_class_name11 AS 班级,
                       term.hxx_school_year11 || ' ' || term.hxx_semester11 AS 学期,
                       sum(CASE WHEN sc.hxx_final_score11 >= 90 THEN 1 ELSE 0 END) AS 优秀,
                       sum(CASE WHEN sc.hxx_final_score11 >= 80 AND sc.hxx_final_score11 < 90 THEN 1 ELSE 0 END) AS 良好,
                       sum(CASE WHEN sc.hxx_final_score11 >= 70 AND sc.hxx_final_score11 < 80 THEN 1 ELSE 0 END) AS 中等,
                       sum(CASE WHEN sc.hxx_final_score11 >= 60 AND sc.hxx_final_score11 < 70 THEN 1 ELSE 0 END) AS 及格,
                       sum(CASE WHEN sc.hxx_final_score11 < 60 THEN 1 ELSE 0 END) AS 不及格
                  FROM Huangxx_Score11 sc
                  JOIN Huangxx_CourseSelection11 sel ON sel.hxx_selection_id11 = sc.hxx_selection_id11
                  JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = task.hxx_class_id11
                  JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11
                 WHERE sc.hxx_score_status11 <> '作废'
                 GROUP BY c.hxx_course_name11, cls.hxx_class_name11, term.hxx_school_year11, term.hxx_semester11
                 ORDER BY term.hxx_school_year11 DESC, c.hxx_course_name11, cls.hxx_class_name11
                """);
    }

    public List<Map<String, Object>> academicWarnings() {
        return jdbcTemplate.queryForList("""
                SELECT stu.hxx_student_name11 AS 学生姓名,
                       cls.hxx_class_name11 AS 班级,
                       stu.hxx_total_credit11 AS 已修学分,
                       stu.hxx_gpa11 AS GPA,
                       count(CASE WHEN sc.hxx_pass_flag11 = 'N' AND sc.hxx_score_status11 <> '作废' THEN 1 END) AS 未通过课程数,
                       CASE
                           WHEN stu.hxx_gpa11 < 1.5 THEN '高风险'
                           WHEN count(CASE WHEN sc.hxx_pass_flag11 = 'N' AND sc.hxx_score_status11 <> '作废' THEN 1 END) >= 2 THEN '需关注'
                           ELSE '正常'
                       END AS 预警等级
                  FROM Huangxx_Student11 stu
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = stu.hxx_class_id11
                  LEFT JOIN Huangxx_CourseSelection11 sel ON sel.hxx_student_id11 = stu.hxx_student_id11
                  LEFT JOIN Huangxx_Score11 sc ON sc.hxx_selection_id11 = sel.hxx_selection_id11
                 GROUP BY stu.hxx_student_id11, stu.hxx_student_name11, cls.hxx_class_name11, stu.hxx_total_credit11, stu.hxx_gpa11
                 ORDER BY CASE
                              WHEN stu.hxx_gpa11 < 1.5 THEN 1
                              WHEN count(CASE WHEN sc.hxx_pass_flag11 = 'N' AND sc.hxx_score_status11 <> '作废' THEN 1 END) >= 2 THEN 2
                              ELSE 3
                          END,
                          stu.hxx_gpa11 ASC,
                          count(CASE WHEN sc.hxx_pass_flag11 = 'N' AND sc.hxx_score_status11 <> '作废' THEN 1 END) DESC
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

    public List<Map<String, Object>> regionStats(Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("""
                WITH RECURSIVE region_tree AS (
                    SELECT hxx_region_id11 AS ancestor_id,
                           hxx_region_id11 AS child_id
                      FROM Huangxx_Region11
                    UNION ALL
                    SELECT rt.ancestor_id,
                           child.hxx_region_id11
                      FROM region_tree rt
                      JOIN Huangxx_Region11 child
                        ON child.hxx_parent_region_id11 = rt.child_id
                )
                SELECT r.hxx_region_name11 AS 生源地,
                       r.hxx_region_level11 AS 地区层级,
                       COALESCE(parent.hxx_region_name11, '无') AS 上级地区,
                       count(s.hxx_student_id11) AS 学生人数
                  FROM Huangxx_Region11 r
                  LEFT JOIN Huangxx_Region11 parent ON parent.hxx_region_id11 = r.hxx_parent_region_id11
                  LEFT JOIN region_tree rt ON rt.ancestor_id = r.hxx_region_id11
                  LEFT JOIN Huangxx_Student11 s ON s.hxx_region_id11 = rt.child_id
                 WHERE 1 = 1
                """);
        ArrayList<Object> args = new ArrayList<>();
        if (!isBlank(filters.get("keyword"))) {
            sql.append(" AND (r.hxx_region_name11 LIKE ? OR parent.hxx_region_name11 LIKE ?)");
            args.add(like(filters.get("keyword")));
            args.add(like(filters.get("keyword")));
        }
        if (!isBlank(filters.get("level"))) {
            sql.append(" AND r.hxx_region_level11 = ?");
            args.add(filters.get("level"));
        }
        if (!isBlank(filters.get("parentId"))) {
            sql.append(" AND r.hxx_parent_region_id11 = ?");
            args.add(filters.get("parentId"));
        }
        sql.append("""
                 GROUP BY r.hxx_region_id11, r.hxx_region_name11, r.hxx_region_level11, parent.hxx_region_name11
                 ORDER BY CASE r.hxx_region_level11 WHEN '省' THEN 1 WHEN '市' THEN 2 ELSE 3 END,
                          r.hxx_region_name11
                """);
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
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

    public List<Map<String, Object>> scoreRanks(Map<String, String> filters) {
        String mode = defaultValue(filters.get("mode"), "class");
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> args = new ArrayList<>();
        if ("gpa".equals(mode)) {
            boolean allTerms = isBlank(filters.get("termId")) || "ALL".equals(filters.get("termId"));
            if (allTerms) {
                sql.append("""
                        SELECT hxx_student_name11 AS 学生姓名,
                               hxx_class_name11 AS 班级,
                               '全部学期' AS 学期范围,
                               count(*) AS 课程数,
                               COALESCE(round(sum(hxx_grade_point11 * hxx_credit11) / nullif(sum(hxx_credit11), 0), 2), 0) AS GPA,
                               dense_rank() OVER (
                                   PARTITION BY hxx_class_id11
                                   ORDER BY COALESCE(sum(hxx_grade_point11 * hxx_credit11) / nullif(sum(hxx_credit11), 0), 0) DESC
                               ) AS 班级GPA排名
                          FROM Huangxx_ViewScoreRank11
                         WHERE 1 = 1
                        """);
                appendRankFilters(sql, args, filters);
                sql.append("""
                         GROUP BY hxx_student_id11, hxx_student_name11, hxx_class_id11, hxx_class_name11
                         ORDER BY hxx_class_name11, 班级GPA排名
                        """);
            } else {
                sql.append("""
                        SELECT hxx_student_name11 AS 学生姓名,
                               hxx_class_name11 AS 班级,
                               hxx_school_year11 || ' ' || hxx_semester11 AS 学期范围,
                               count(*) AS 课程数,
                               COALESCE(round(sum(hxx_grade_point11 * hxx_credit11) / nullif(sum(hxx_credit11), 0), 2), 0) AS GPA,
                               dense_rank() OVER (
                                   PARTITION BY hxx_class_id11, hxx_school_year11, hxx_semester11
                                   ORDER BY COALESCE(sum(hxx_grade_point11 * hxx_credit11) / nullif(sum(hxx_credit11), 0), 0) DESC
                               ) AS 班级GPA排名
                          FROM Huangxx_ViewScoreRank11
                         WHERE 1 = 1
                        """);
                appendRankFilters(sql, args, filters);
                sql.append("""
                         GROUP BY hxx_student_id11, hxx_student_name11, hxx_class_id11, hxx_class_name11, hxx_school_year11, hxx_semester11
                         ORDER BY hxx_school_year11 DESC, hxx_semester11, hxx_class_name11, 班级GPA排名
                        """);
            }
            return jdbcTemplate.queryForList(sql.toString(), args.toArray());
        }
        if ("student".equals(mode)) {
            sql.append("""
                    SELECT hxx_student_name11 AS 学生姓名,
                           hxx_class_name11 AS 班级,
                           hxx_school_year11 AS 学年,
                           hxx_semester11 AS 学期,
                           count(*) AS 课程数,
                           round(avg(hxx_final_score11), 2) AS 平均成绩,
                           dense_rank() OVER (
                               PARTITION BY hxx_class_id11, hxx_school_year11, hxx_semester11
                               ORDER BY avg(hxx_final_score11) DESC
                           ) AS 班级平均分排名
                      FROM Huangxx_ViewScoreRank11
                     WHERE 1 = 1
                    """);
            appendRankFilters(sql, args, filters);
            sql.append("""
                     GROUP BY hxx_student_id11, hxx_student_name11, hxx_class_id11, hxx_class_name11, hxx_school_year11, hxx_semester11
                     ORDER BY hxx_school_year11 DESC, hxx_semester11, hxx_class_name11, 班级平均分排名
                    """);
            return jdbcTemplate.queryForList(sql.toString(), args.toArray());
        }
        sql.append("""
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
                 WHERE 1 = 1
                """);
        appendRankFilters(sql, args, filters);
        sql.append(" ORDER BY hxx_school_year11 DESC, hxx_semester11, hxx_class_name11, hxx_course_name11, hxx_course_rank11");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    private void appendRankFilters(StringBuilder sql, ArrayList<Object> args, Map<String, String> filters) {
        if (!isBlank(filters.get("termId")) && !"ALL".equals(filters.get("termId"))) {
            sql.append(" AND hxx_task_id11 IN (SELECT hxx_task_id11 FROM Huangxx_TeachingTask11 WHERE hxx_term_id11 = ?)");
            args.add(filters.get("termId"));
        }
        if (!isBlank(filters.get("taskId"))) {
            sql.append(" AND hxx_task_id11 = ?");
            args.add(filters.get("taskId"));
        }
        if (!isBlank(filters.get("courseId"))) {
            sql.append(" AND hxx_course_id11 = ?");
            args.add(Integer.parseInt(filters.get("courseId")));
        }
        if (!isBlank(filters.get("classId"))) {
            sql.append(" AND hxx_class_id11 = ?");
            args.add(filters.get("classId"));
        }
        if (!isBlank(filters.get("majorId"))) {
            sql.append(" AND hxx_major_id11 = ?");
            args.add(filters.get("majorId"));
        }
        if (!isBlank(filters.get("gradeYear"))) {
            sql.append(" AND hxx_grade_year11 = ?");
            args.add(Integer.parseInt(filters.get("gradeYear")));
        }
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

    public List<Map<String, Object>> classOptionsForForms() {
        return jdbcTemplate.queryForList("""
                SELECT hxx_class_id11 AS id,
                       hxx_class_name11 AS name,
                       hxx_major_id11 AS majorId
                  FROM Huangxx_Class11
                 ORDER BY hxx_grade_year11 DESC, hxx_class_name11
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
        return jdbcTemplate.queryForMap("""
                SELECT s.*, cls.hxx_major_id11
                  FROM Huangxx_Student11 s
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = s.hxx_class_id11
                 WHERE s.hxx_student_id11 = ?
                """, id);
    }

    public Map<String, Object> teacher(String id) {
        Map<String, Object> row = new LinkedHashMap<>(jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Teacher11 WHERE hxx_teacher_id11 = ?", id));
        row.put("adminFlag", countWhere("Huangxx_SystemUser11", "hxx_ref_id11", id) > 0
                && jdbcTemplate.queryForObject("""
                    SELECT count(*) FROM Huangxx_SystemUser11
                     WHERE hxx_ref_id11 = ? AND hxx_role11 = '管理员'
                    """, Integer.class, id) > 0 ? "Y" : "N");
        return row;
    }

    public Map<String, Object> course(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Course11 WHERE hxx_course_id11 = ?", Integer.parseInt(id));
    }

    public Map<String, Object> major(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Major11 WHERE hxx_major_id11 = ?", id);
    }

    public Map<String, Object> classInfo(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Class11 WHERE hxx_class_id11 = ?", id);
    }

    public Map<String, Object> region(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Region11 WHERE hxx_region_id11 = ?", id);
    }

    public Map<String, Object> task(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_TeachingTask11 WHERE hxx_task_id11 = ?", id);
    }

    public Map<String, Object> term(String id) {
        return jdbcTemplate.queryForMap("SELECT * FROM Huangxx_Term11 WHERE hxx_term_id11 = ?", id);
    }

    public Map<String, Object> user(String id) {
        return jdbcTemplate.queryForMap("""
                SELECT hxx_user_id11, hxx_login_name11, hxx_role11, hxx_ref_id11, hxx_user_status11
                  FROM Huangxx_SystemUser11
                 WHERE hxx_user_id11 = ?
                """, id);
    }

    public void saveStudent(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Student11
                (hxx_student_id11, hxx_student_name11, hxx_gender11, hxx_age11, hxx_region_id11,
                 hxx_class_id11, hxx_total_credit11, hxx_gpa11, hxx_phone11, hxx_status11, hxx_enroll_date11)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, to_date(?, 'YYYY-MM-DD'))
                """,
                form.get("studentId"), form.get("studentName"), form.get("gender"), Integer.parseInt(form.get("age")),
                form.get("regionId"), form.get("classId"), decimal(defaultValue(form.get("totalCredit"), "0")),
                decimal(defaultValue(form.get("gpa"), "0")), blankToNull(form.get("phone")), form.get("status"), form.get("enrollDate"));
        upsertStudentUser(form.get("studentId"), form.get("password"));
    }

    public int importStudents(List<Map<String, String>> rows) {
        int imported = 0;
        for (Map<String, String> row : rows) {
            saveStudent(row);
            imported++;
        }
        return imported;
    }

    public List<String> classIdsByNameOrId(String value) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_class_id11
                  FROM Huangxx_Class11
                 WHERE hxx_class_id11 = ? OR hxx_class_name11 = ?
                 ORDER BY hxx_class_id11
                """, String.class, value, value);
    }

    public List<String> regionIdsByNameOrId(String value) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_region_id11
                  FROM Huangxx_Region11
                 WHERE hxx_region_id11 = ? OR hxx_region_name11 = ?
                 ORDER BY CASE hxx_region_level11 WHEN '区县' THEN 1 WHEN '市' THEN 2 ELSE 3 END,
                          hxx_region_id11
                """, String.class, value, value);
    }

    public void updateStudent(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Student11
                   SET hxx_student_name11 = ?, hxx_gender11 = ?, hxx_age11 = ?, hxx_region_id11 = ?,
                       hxx_class_id11 = ?, hxx_total_credit11 = ?, hxx_gpa11 = ?,
                       hxx_phone11 = ?, hxx_status11 = ?, hxx_enroll_date11 = to_date(?, 'YYYY-MM-DD')
                 WHERE hxx_student_id11 = ?
                """,
                form.get("studentName"), form.get("gender"), Integer.parseInt(form.get("age")),
                form.get("regionId"), form.get("classId"), decimal(defaultValue(form.get("totalCredit"), "0")),
                decimal(defaultValue(form.get("gpa"), "0")), blankToNull(form.get("phone")), form.get("status"),
                form.get("enrollDate"), form.get("studentId"));
        upsertStudentUser(form.get("studentId"), form.get("password"));
    }

    public void deleteStudent(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_SystemUser11 WHERE hxx_ref_id11 = ? AND hxx_role11 = '学生'", id);
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
                form.get("title"), blankToNull(form.get("phone")), form.get("collegeName"), form.get("status"));
        upsertTeacherUser(form.get("teacherId"), form.get("password"), "Y".equals(form.get("adminFlag")));
    }

    public void updateTeacher(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Teacher11
                   SET hxx_teacher_name11 = ?, hxx_gender11 = ?, hxx_age11 = ?, hxx_title11 = ?,
                       hxx_phone11 = ?, hxx_college_name11 = ?, hxx_status11 = ?
                 WHERE hxx_teacher_id11 = ?
                """,
                form.get("teacherName"), form.get("gender"), Integer.parseInt(form.get("age")), form.get("title"),
                blankToNull(form.get("phone")), form.get("collegeName"), form.get("status"), form.get("teacherId"));
        upsertTeacherUser(form.get("teacherId"), form.get("password"), "Y".equals(form.get("adminFlag")));
    }

    public void deleteTeacher(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_SystemUser11 WHERE hxx_ref_id11 = ? AND hxx_role11 IN ('教师', '管理员')", id);
        jdbcTemplate.update("DELETE FROM Huangxx_Teacher11 WHERE hxx_teacher_id11 = ?", id);
    }

    public void saveCourse(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Course11
                (hxx_course_name11, hxx_credit11, hxx_period11,
                 hxx_exam_type11, hxx_course_type11, hxx_course_desc11)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                form.get("courseName"), decimal(form.get("credit")), Integer.parseInt(form.get("period")),
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
                form.get("examType"), form.get("courseType"), form.get("courseDesc"), Integer.parseInt(form.get("courseId")));
    }

    public void deleteCourse(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_Course11 WHERE hxx_course_id11 = ?", Integer.parseInt(id));
    }

    public void saveMajor(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Major11
                (hxx_major_id11, hxx_major_name11, hxx_college_name11, hxx_major_desc11)
                VALUES (?, ?, ?, ?)
                """, form.get("majorId"), form.get("majorName"), defaultValue(form.get("collegeName"), "计算机学院"), blankToNull(form.get("majorDesc")));
    }

    public void updateMajor(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Major11
                   SET hxx_major_name11 = ?,
                       hxx_college_name11 = ?,
                       hxx_major_desc11 = ?
                 WHERE hxx_major_id11 = ?
                """, form.get("majorName"), defaultValue(form.get("collegeName"), "计算机学院"), blankToNull(form.get("majorDesc")), form.get("majorId"));
    }

    public void deleteMajor(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_Major11 WHERE hxx_major_id11 = ?", id);
    }

    public void saveClass(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Class11
                (hxx_class_id11, hxx_class_name11, hxx_major_id11, hxx_grade_year11, hxx_class_size11, hxx_remark11)
                VALUES (?, ?, ?, ?, ?, ?)
                """, form.get("classId"), form.get("className"), form.get("majorId"),
                Integer.parseInt(form.get("gradeYear")), Integer.parseInt(defaultValue(form.get("classSize"), "0")), blankToNull(form.get("remark")));
    }

    public void updateClass(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Class11
                   SET hxx_class_name11 = ?,
                       hxx_major_id11 = ?,
                       hxx_grade_year11 = ?,
                       hxx_class_size11 = ?,
                       hxx_remark11 = ?
                 WHERE hxx_class_id11 = ?
                """, form.get("className"), form.get("majorId"), Integer.parseInt(form.get("gradeYear")),
                Integer.parseInt(defaultValue(form.get("classSize"), "0")), blankToNull(form.get("remark")), form.get("classId"));
    }

    public void deleteClass(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_Class11 WHERE hxx_class_id11 = ?", id);
    }

    public void saveRegion(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Region11
                (hxx_region_id11, hxx_region_name11, hxx_region_level11, hxx_parent_region_id11, hxx_region_remark11)
                VALUES (?, ?, ?, ?, ?)
                """, form.get("regionId"), form.get("regionName"), form.get("regionLevel"),
                blankToNull(form.get("parentRegionId")), blankToNull(form.get("regionRemark")));
    }

    public void updateRegion(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Region11
                   SET hxx_region_name11 = ?,
                       hxx_region_level11 = ?,
                       hxx_parent_region_id11 = ?,
                       hxx_region_remark11 = ?
                 WHERE hxx_region_id11 = ?
                """, form.get("regionName"), form.get("regionLevel"), blankToNull(form.get("parentRegionId")),
                blankToNull(form.get("regionRemark")), form.get("regionId"));
    }

    public List<String> majorIdsByPrefix(String prefix) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_major_id11
                  FROM Huangxx_Major11
                 WHERE hxx_major_id11 LIKE ?
                 ORDER BY hxx_major_id11
                """, String.class, prefix + "%");
    }

    public List<String> classIdsByPrefix(String prefix) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_class_id11
                  FROM Huangxx_Class11
                 WHERE hxx_class_id11 LIKE ?
                 ORDER BY hxx_class_id11
                """, String.class, prefix + "%");
    }

    public List<String> regionIdsByPrefix(String prefix) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_region_id11
                  FROM Huangxx_Region11
                 WHERE hxx_region_id11 LIKE ?
                 ORDER BY hxx_region_id11
                """, String.class, prefix + "%");
    }

    public List<String> taskIdsByPrefix(String prefix) {
        return jdbcTemplate.queryForList("""
                SELECT hxx_task_id11
                  FROM Huangxx_TeachingTask11
                 WHERE hxx_task_id11 LIKE ?
                 ORDER BY hxx_task_id11
                """, String.class, prefix + "%");
    }

    public String regionLevel(String regionId) {
        List<String> levels = jdbcTemplate.queryForList("""
                SELECT hxx_region_level11
                  FROM Huangxx_Region11
                 WHERE hxx_region_id11 = ?
                """, String.class, regionId);
        return levels.isEmpty() ? null : levels.get(0);
    }

    public boolean regionHasChildren(String regionId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT count(*)
                  FROM Huangxx_Region11
                 WHERE hxx_parent_region_id11 = ?
                """, Integer.class, regionId);
        return count != null && count > 0;
    }

    public void saveTask(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_TeachingTask11
                (hxx_task_id11, hxx_course_id11, hxx_teacher_id11, hxx_class_id11,
                 hxx_term_id11, hxx_teaching_place11, hxx_max_count11, hxx_task_status11)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """,
                form.get("taskId"), Integer.parseInt(form.get("courseId")), form.get("teacherId"), form.get("classId"),
                form.get("termId"), form.get("teachingPlace"), Integer.parseInt(form.get("maxCount")),
                defaultValue(form.get("taskStatus"), "开课中"));
    }

    public void updateTask(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_TeachingTask11
                   SET hxx_course_id11 = ?, hxx_teacher_id11 = ?, hxx_class_id11 = ?, hxx_term_id11 = ?,
                       hxx_teaching_place11 = ?, hxx_max_count11 = ?, hxx_task_status11 = ?
                 WHERE hxx_task_id11 = ?
                """,
                Integer.parseInt(form.get("courseId")), form.get("teacherId"), form.get("classId"), form.get("termId"),
                form.get("teachingPlace"), Integer.parseInt(form.get("maxCount")),
                defaultValue(form.get("taskStatus"), "开课中"), form.get("taskId"));
    }

    public void deleteTask(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_TeachingTask11 WHERE hxx_task_id11 = ?", id);
    }

    public void saveTerm(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_Term11
                (hxx_term_id11, hxx_school_year11, hxx_semester11, hxx_start_date11, hxx_end_date11, hxx_term_status11)
                VALUES (?, ?, ?, to_date(?, 'YYYY-MM-DD'), to_date(?, 'YYYY-MM-DD'), ?)
                """, form.get("termId"), form.get("schoolYear"), form.get("semester"),
                form.get("startDate"), form.get("endDate"), defaultValue(form.get("termStatus"), "启用"));
    }

    public void updateTerm(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_Term11
                   SET hxx_school_year11 = ?,
                       hxx_semester11 = ?,
                       hxx_start_date11 = to_date(?, 'YYYY-MM-DD'),
                       hxx_end_date11 = to_date(?, 'YYYY-MM-DD'),
                       hxx_term_status11 = ?
                 WHERE hxx_term_id11 = ?
                """, form.get("schoolYear"), form.get("semester"), form.get("startDate"), form.get("endDate"),
                defaultValue(form.get("termStatus"), "启用"), form.get("termId"));
    }

    public void deleteTerm(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_Term11 WHERE hxx_term_id11 = ?", id);
    }

    public void saveUser(Map<String, String> form) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_SystemUser11
                (hxx_user_id11, hxx_login_name11, hxx_password11, hxx_role11, hxx_ref_id11, hxx_user_status11)
                VALUES (?, ?, ?, ?, ?, ?)
                """, form.get("userId"), form.get("loginName"), defaultValue(form.get("password"), "123456"),
                form.get("role"), blankToNull(form.get("refId")), defaultValue(form.get("userStatus"), "正常"));
    }

    public void updateUser(Map<String, String> form) {
        jdbcTemplate.update("""
                UPDATE Huangxx_SystemUser11
                   SET hxx_login_name11 = ?,
                       hxx_role11 = ?,
                       hxx_ref_id11 = ?,
                       hxx_user_status11 = ?
                 WHERE hxx_user_id11 = ?
                """, form.get("loginName"), form.get("role"), blankToNull(form.get("refId")),
                defaultValue(form.get("userStatus"), "正常"), form.get("userId"));
        if (!isBlank(form.get("password"))) {
            resetUserPassword(form.get("userId"), form.get("password"));
        }
    }

    public void deleteUser(String id) {
        jdbcTemplate.update("DELETE FROM Huangxx_SystemUser11 WHERE hxx_user_id11 = ?", id);
    }

    public void resetUserPassword(String id, String password) {
        jdbcTemplate.update("UPDATE Huangxx_SystemUser11 SET hxx_password11 = ? WHERE hxx_user_id11 = ?", defaultValue(password, "123456"), id);
    }

    public int countWhere(String table, String column, String value) {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM " + table + " WHERE CAST(" + column + " AS varchar) = ?", Integer.class, value);
        return count == null ? 0 : count;
    }

    public int countMajorName(String name, String exceptId) {
        Integer count;
        if (isBlank(exceptId)) {
            count = jdbcTemplate.queryForObject("""
                    SELECT count(*) FROM Huangxx_Major11
                     WHERE hxx_major_name11 = ?
                    """, Integer.class, name);
        } else {
            count = jdbcTemplate.queryForObject("""
                    SELECT count(*) FROM Huangxx_Major11
                     WHERE hxx_major_name11 = ?
                       AND hxx_major_id11 <> ?
                    """, Integer.class, name, exceptId);
        }
        return count == null ? 0 : count;
    }

    public int countRegionDuplicateName(String name, String parentId, String exceptId) {
        StringBuilder sql = new StringBuilder("""
                SELECT count(*)
                  FROM Huangxx_Region11
                 WHERE hxx_region_name11 = ?
                """);
        ArrayList<Object> args = new ArrayList<>();
        args.add(name);
        if (isBlank(parentId)) {
            sql.append(" AND hxx_parent_region_id11 IS NULL");
        } else {
            sql.append(" AND hxx_parent_region_id11 = ?");
            args.add(parentId);
        }
        if (!isBlank(exceptId)) {
            sql.append(" AND hxx_region_id11 <> ?");
            args.add(exceptId);
        }
        Integer count = jdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
        return count == null ? 0 : count;
    }

    public int countTaskDuplicate(Map<String, String> form, boolean edit) {
        StringBuilder sql = new StringBuilder("""
                SELECT count(*) FROM Huangxx_TeachingTask11
                 WHERE hxx_course_id11 = ?
                   AND hxx_teacher_id11 = ?
                   AND hxx_class_id11 = ?
                   AND hxx_term_id11 = ?
                """);
        ArrayList<Object> args = new ArrayList<>();
        args.add(Integer.parseInt(form.get("courseId")));
        args.add(form.get("teacherId"));
        args.add(form.get("classId"));
        args.add(form.get("termId"));
        if (edit) {
            sql.append(" AND hxx_task_id11 <> ?");
            args.add(form.get("taskId"));
        }
        Integer count = jdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
        return count == null ? 0 : count;
    }

    public List<Map<String, Object>> options(String table, String idColumn, String nameColumn) {
        return jdbcTemplate.queryForList("SELECT " + idColumn + " AS id, " + nameColumn + " AS name FROM " + table + " ORDER BY " + idColumn);
    }

    public List<Map<String, Object>> regionOptionsForForms() {
        return jdbcTemplate.queryForList("""
                SELECT r.hxx_region_id11 AS id,
                       CASE
                           WHEN r.hxx_region_level11 = '区县' THEN COALESCE(grand.hxx_region_name11 || ' / ', '') || COALESCE(parent.hxx_region_name11 || ' / ', '') || r.hxx_region_name11
                           WHEN r.hxx_region_level11 = '市' THEN COALESCE(parent.hxx_region_name11 || ' / ', '') || r.hxx_region_name11
                           ELSE r.hxx_region_name11
                       END AS name,
                       r.hxx_region_level11 AS level,
                       r.hxx_parent_region_id11 AS parentId
                  FROM Huangxx_Region11 r
                  LEFT JOIN Huangxx_Region11 parent ON parent.hxx_region_id11 = r.hxx_parent_region_id11
                  LEFT JOIN Huangxx_Region11 grand ON grand.hxx_region_id11 = parent.hxx_parent_region_id11
                 ORDER BY CASE r.hxx_region_level11 WHEN '省' THEN 1 WHEN '市' THEN 2 ELSE 3 END,
                          COALESCE(parent.hxx_region_name11, r.hxx_region_name11),
                          r.hxx_region_name11
                """);
    }

    public List<Map<String, Object>> taskOptions() {
        return jdbcTemplate.queryForList("""
                SELECT task.hxx_task_id11 AS id,
                       c.hxx_course_name11 || ' / ' || cls.hxx_class_name11 || ' / ' ||
                       term.hxx_school_year11 || ' ' || term.hxx_semester11 AS name
                  FROM Huangxx_TeachingTask11 task
                  JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
                  JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = task.hxx_class_id11
                  JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11
                 ORDER BY term.hxx_school_year11 DESC, task.hxx_task_id11
                """);
    }

    private long count(String table) {
        Long value = jdbcTemplate.queryForObject("SELECT count(*) FROM " + table, Long.class);
        return value == null ? 0 : value;
    }

    private java.math.BigDecimal decimal(String value) {
        return new java.math.BigDecimal(value);
    }

    private void upsertStudentUser(String studentId, String password) {
        jdbcTemplate.update("""
                INSERT INTO Huangxx_SystemUser11
                (hxx_user_id11, hxx_login_name11, hxx_password11, hxx_role11, hxx_ref_id11, hxx_user_status11)
                SELECT 'U-' || ?, lower(?), ?, '学生', ?, '正常'
                 WHERE NOT EXISTS (SELECT 1 FROM Huangxx_SystemUser11 WHERE hxx_ref_id11 = ? AND hxx_role11 = '学生')
                """, studentId, studentId, defaultValue(password, "123456"), studentId, studentId);
        if (!isBlank(password)) {
            jdbcTemplate.update("UPDATE Huangxx_SystemUser11 SET hxx_password11 = ? WHERE hxx_ref_id11 = ? AND hxx_role11 = '学生'", password, studentId);
        }
    }

    private void upsertTeacherUser(String teacherId, String password, boolean adminFlag) {
        String role = adminFlag ? "管理员" : "教师";
        jdbcTemplate.update("""
                INSERT INTO Huangxx_SystemUser11
                (hxx_user_id11, hxx_login_name11, hxx_password11, hxx_role11, hxx_ref_id11, hxx_user_status11)
                SELECT 'U-' || ?, lower(?), ?, ?, ?, '正常'
                 WHERE NOT EXISTS (SELECT 1 FROM Huangxx_SystemUser11 WHERE hxx_ref_id11 = ? AND hxx_role11 IN ('教师', '管理员'))
                """, teacherId, teacherId, defaultValue(password, "123456"), role, teacherId, teacherId);
        jdbcTemplate.update("UPDATE Huangxx_SystemUser11 SET hxx_role11 = ? WHERE hxx_ref_id11 = ? AND hxx_role11 IN ('教师', '管理员')", role, teacherId);
        if (!isBlank(password)) {
            jdbcTemplate.update("UPDATE Huangxx_SystemUser11 SET hxx_password11 = ? WHERE hxx_ref_id11 = ? AND hxx_role11 IN ('教师', '管理员')", password, teacherId);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String like(String value) {
        return "%" + value.trim() + "%";
    }

    private String defaultValue(String value, String fallback) {
        return isBlank(value) ? fallback : value.trim();
    }

    private String blankToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }
}
