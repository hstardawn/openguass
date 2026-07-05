-- 05_views.sql

DROP VIEW IF EXISTS Huangxx_ViewScoreAppeal11 CASCADE;
DROP VIEW IF EXISTS Huangxx_ViewRegionStudentStat11 CASCADE;
DROP VIEW IF EXISTS Huangxx_ViewStudentGpaCredit11 CASCADE;
DROP VIEW IF EXISTS Huangxx_ViewScoreRank11 CASCADE;
DROP VIEW IF EXISTS Huangxx_ViewCourseStat11 CASCADE;
DROP VIEW IF EXISTS Huangxx_ViewClassCourse11 CASCADE;
DROP VIEW IF EXISTS Huangxx_ViewTeacherTask11 CASCADE;
DROP VIEW IF EXISTS Huangxx_ViewStudentScore11 CASCADE;

CREATE OR REPLACE VIEW Huangxx_ViewStudentScore11 AS
SELECT
    s.hxx_student_id11,
    s.hxx_student_name11,
    cl.hxx_class_id11,
    cl.hxx_class_name11,
    major.hxx_major_id11,
    major.hxx_major_name11,
    cl.hxx_grade_year11,
    c.hxx_course_id11,
    c.hxx_course_name11,
    c.hxx_credit11,
    t.hxx_teacher_id11,
    t.hxx_teacher_name11,
    term.hxx_school_year11,
    term.hxx_semester11,
    task.hxx_task_id11,
    sc.hxx_score_id11,
    sc.hxx_usual_score11,
    sc.hxx_exam_score11,
    sc.hxx_final_score11,
    sc.hxx_grade_level11,
    sc.hxx_pass_flag11,
    sc.hxx_grade_point11,
    sc.hxx_publish_flag11,
    sc.hxx_lock_flag11,
    sc.hxx_score_status11
FROM Huangxx_Score11 sc
JOIN Huangxx_CourseSelection11 sel ON sel.hxx_selection_id11 = sc.hxx_selection_id11
JOIN Huangxx_Student11 s ON s.hxx_student_id11 = sel.hxx_student_id11
JOIN Huangxx_Class11 cl ON cl.hxx_class_id11 = s.hxx_class_id11
JOIN Huangxx_Major11 major ON major.hxx_major_id11 = cl.hxx_major_id11
JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
JOIN Huangxx_Teacher11 t ON t.hxx_teacher_id11 = task.hxx_teacher_id11
JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11;

CREATE OR REPLACE VIEW Huangxx_ViewTeacherTask11 AS
SELECT
    task.hxx_task_id11,
    task.hxx_teacher_id11,
    tea.hxx_teacher_name11,
    c.hxx_course_name11,
    cls.hxx_class_name11,
    term.hxx_school_year11,
    term.hxx_semester11,
    task.hxx_teaching_place11,
    task.hxx_max_count11,
    task.hxx_current_count11,
    task.hxx_task_status11,
    task.hxx_score_publish_flag11,
    count(sc.hxx_score_id11) AS hxx_score_count11
FROM Huangxx_TeachingTask11 task
JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11
JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = task.hxx_class_id11
JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11
LEFT JOIN Huangxx_CourseSelection11 sel ON sel.hxx_task_id11 = task.hxx_task_id11
LEFT JOIN Huangxx_Score11 sc ON sc.hxx_selection_id11 = sel.hxx_selection_id11
GROUP BY task.hxx_task_id11, task.hxx_teacher_id11, tea.hxx_teacher_name11, c.hxx_course_name11,
         cls.hxx_class_name11, term.hxx_school_year11, term.hxx_semester11, task.hxx_teaching_place11,
         task.hxx_max_count11, task.hxx_current_count11, task.hxx_task_status11, task.hxx_score_publish_flag11;

CREATE OR REPLACE VIEW Huangxx_ViewClassCourse11 AS
SELECT
    cls.hxx_class_id11,
    cls.hxx_class_name11,
    major.hxx_major_name11,
    task.hxx_task_id11,
    c.hxx_course_name11,
    c.hxx_credit11,
    tea.hxx_teacher_name11,
    term.hxx_school_year11,
    term.hxx_semester11,
    task.hxx_teaching_place11,
    task.hxx_task_status11
FROM Huangxx_TeachingTask11 task
JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = task.hxx_class_id11
JOIN Huangxx_Major11 major ON major.hxx_major_id11 = cls.hxx_major_id11
JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11
JOIN Huangxx_Term11 term ON term.hxx_term_id11 = task.hxx_term_id11;

CREATE OR REPLACE VIEW Huangxx_ViewCourseStat11 AS
SELECT
    task.hxx_task_id11,
    c.hxx_course_name11,
    tea.hxx_teacher_name11,
    cls.hxx_class_name11,
    count(sc.hxx_score_id11) AS hxx_score_count11,
    round(avg(sc.hxx_final_score11), 2) AS hxx_avg_score11,
    max(sc.hxx_final_score11) AS hxx_max_score11,
    min(sc.hxx_final_score11) AS hxx_min_score11,
    round(sum(CASE WHEN sc.hxx_pass_flag11 = 'Y' THEN 1 ELSE 0 END) * 100.0 / nullif(count(sc.hxx_score_id11), 0), 2) AS hxx_pass_rate11,
    round(sum(CASE WHEN sc.hxx_final_score11 >= 90 THEN 1 ELSE 0 END) * 100.0 / nullif(count(sc.hxx_score_id11), 0), 2) AS hxx_excellent_rate11,
    sum(CASE WHEN sc.hxx_pass_flag11 = 'N' THEN 1 ELSE 0 END) AS hxx_fail_count11
FROM Huangxx_TeachingTask11 task
JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11
JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = task.hxx_class_id11
LEFT JOIN Huangxx_CourseSelection11 sel ON sel.hxx_task_id11 = task.hxx_task_id11
LEFT JOIN Huangxx_Score11 sc ON sc.hxx_selection_id11 = sel.hxx_selection_id11 AND sc.hxx_score_status11 <> '作废'
GROUP BY task.hxx_task_id11, c.hxx_course_name11, tea.hxx_teacher_name11, cls.hxx_class_name11;

CREATE OR REPLACE VIEW Huangxx_ViewScoreRank11 AS
SELECT
    v.*,
    v.hxx_class_name11 || ' / ' || v.hxx_course_name11 || ' / ' || v.hxx_school_year11 || ' / ' || v.hxx_semester11 || '排名' AS hxx_rank_scope11,
    dense_rank() OVER (PARTITION BY v.hxx_task_id11 ORDER BY v.hxx_final_score11 DESC) AS hxx_course_rank11,
    dense_rank() OVER (
        PARTITION BY v.hxx_class_id11, v.hxx_school_year11, v.hxx_semester11
        ORDER BY v.hxx_grade_point11 DESC, v.hxx_final_score11 DESC
    ) AS hxx_class_rank11,
    dense_rank() OVER (
        PARTITION BY v.hxx_major_id11, v.hxx_grade_year11, v.hxx_school_year11, v.hxx_semester11
        ORDER BY v.hxx_grade_point11 DESC, v.hxx_final_score11 DESC
    ) AS hxx_major_grade_rank11
FROM Huangxx_ViewStudentScore11 v
WHERE v.hxx_score_status11 <> '作废';

CREATE OR REPLACE VIEW Huangxx_ViewStudentGpaCredit11 AS
SELECT
    s.hxx_student_id11,
    s.hxx_student_name11,
    cls.hxx_class_name11,
    s.hxx_total_credit11,
    s.hxx_gpa11,
    count(CASE WHEN sc.hxx_pass_flag11 = 'Y' AND sc.hxx_score_status11 <> '作废' THEN 1 END) AS hxx_pass_course_count11,
    count(CASE WHEN sc.hxx_pass_flag11 = 'N' AND sc.hxx_score_status11 <> '作废' THEN 1 END) AS hxx_fail_course_count11
FROM Huangxx_Student11 s
JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = s.hxx_class_id11
LEFT JOIN Huangxx_CourseSelection11 sel ON sel.hxx_student_id11 = s.hxx_student_id11
LEFT JOIN Huangxx_Score11 sc ON sc.hxx_selection_id11 = sel.hxx_selection_id11
GROUP BY s.hxx_student_id11, s.hxx_student_name11, cls.hxx_class_name11, s.hxx_total_credit11, s.hxx_gpa11;

CREATE OR REPLACE VIEW Huangxx_ViewRegionStudentStat11 AS
SELECT
    r.hxx_region_id11,
    r.hxx_region_name11,
    r.hxx_region_level11,
    parent.hxx_region_name11 AS hxx_parent_region_name11,
    count(s.hxx_student_id11) AS hxx_student_count11
FROM Huangxx_Region11 r
LEFT JOIN Huangxx_Region11 parent ON parent.hxx_region_id11 = r.hxx_parent_region_id11
LEFT JOIN Huangxx_Student11 s ON s.hxx_region_id11 = r.hxx_region_id11
GROUP BY r.hxx_region_id11, r.hxx_region_name11, r.hxx_region_level11, parent.hxx_region_name11;

CREATE OR REPLACE VIEW Huangxx_ViewScoreAppeal11 AS
SELECT
    a.hxx_appeal_id11,
    a.hxx_score_id11,
    a.hxx_student_id11,
    stu.hxx_student_name11,
    cls.hxx_class_name11,
    c.hxx_course_name11,
    tea.hxx_teacher_id11,
    tea.hxx_teacher_name11,
    sc.hxx_final_score11,
    sc.hxx_publish_flag11,
    a.hxx_appeal_reason11,
    a.hxx_appeal_status11,
    a.hxx_handler_id11,
    a.hxx_handle_result11,
    a.hxx_create_time11,
    a.hxx_handle_time11
FROM Huangxx_ScoreAppeal11 a
JOIN Huangxx_Student11 stu ON stu.hxx_student_id11 = a.hxx_student_id11
JOIN Huangxx_Score11 sc ON sc.hxx_score_id11 = a.hxx_score_id11
JOIN Huangxx_CourseSelection11 sel ON sel.hxx_selection_id11 = sc.hxx_selection_id11
JOIN Huangxx_Class11 cls ON cls.hxx_class_id11 = stu.hxx_class_id11
JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
JOIN Huangxx_Teacher11 tea ON tea.hxx_teacher_id11 = task.hxx_teacher_id11;
