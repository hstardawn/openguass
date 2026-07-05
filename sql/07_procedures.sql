-- 07_procedures.sql

DROP PROCEDURE IF EXISTS Huangxx_ProcStudentTranscript11(varchar, refcursor);
DROP PROCEDURE IF EXISTS Huangxx_ProcCourseScoreStat11(varchar, refcursor);
DROP PROCEDURE IF EXISTS Huangxx_ProcClassCreditStat11(varchar, refcursor);
DROP PROCEDURE IF EXISTS Huangxx_ProcRegionStudentStat11(refcursor);
DROP PROCEDURE IF EXISTS Huangxx_ProcTeacherTaskStat11(varchar, refcursor);
DROP PROCEDURE IF EXISTS Huangxx_ProcStudentYearlyScoreStat11(varchar, refcursor);
DROP PROCEDURE IF EXISTS Huangxx_ProcMajorGpaStat11(refcursor);
DROP PROCEDURE IF EXISTS Huangxx_ProcAppealStatusStat11(refcursor);
DROP PROCEDURE IF EXISTS Huangxx_ProcOperationLogStat11(refcursor);

CREATE OR REPLACE PROCEDURE Huangxx_ProcStudentTranscript11(
    IN p_student_id varchar,
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT *
          FROM Huangxx_ViewStudentScore11
         WHERE hxx_student_id11 = p_student_id
         ORDER BY hxx_school_year11, hxx_semester11, hxx_course_name11;
END;
/

CREATE OR REPLACE PROCEDURE Huangxx_ProcCourseScoreStat11(
    IN p_task_id varchar,
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT *
          FROM Huangxx_ViewCourseStat11
         WHERE hxx_task_id11 = p_task_id;
END;
/

CREATE OR REPLACE PROCEDURE Huangxx_ProcClassCreditStat11(
    IN p_class_id varchar,
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT s.hxx_student_id11,
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
         WHERE s.hxx_class_id11 = p_class_id
         GROUP BY s.hxx_student_id11, s.hxx_student_name11, cls.hxx_class_name11, s.hxx_total_credit11, s.hxx_gpa11
         ORDER BY s.hxx_student_id11;
END;
/

CREATE OR REPLACE PROCEDURE Huangxx_ProcRegionStudentStat11(
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT *
          FROM Huangxx_ViewRegionStudentStat11
         ORDER BY hxx_region_level11, hxx_region_name11;
END;
/

CREATE OR REPLACE PROCEDURE Huangxx_ProcTeacherTaskStat11(
    IN p_teacher_id varchar,
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT v.*,
               stat.hxx_avg_score11,
               stat.hxx_pass_rate11
          FROM Huangxx_ViewTeacherTask11 v
          LEFT JOIN Huangxx_ViewCourseStat11 stat ON stat.hxx_task_id11 = v.hxx_task_id11
         WHERE v.hxx_teacher_id11 = p_teacher_id
         ORDER BY v.hxx_school_year11 DESC, v.hxx_semester11 DESC, v.hxx_course_name11;
END;
/

CREATE OR REPLACE PROCEDURE Huangxx_ProcStudentYearlyScoreStat11(
    IN p_student_id varchar,
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT hxx_student_id11,
               hxx_student_name11,
               hxx_school_year11,
               count(*) AS hxx_course_count11,
               round(avg(hxx_final_score11), 2) AS hxx_avg_score11,
               max(hxx_final_score11) AS hxx_max_score11,
               min(hxx_final_score11) AS hxx_min_score11,
               sum(CASE WHEN hxx_pass_flag11 = 'Y' THEN hxx_credit11 ELSE 0 END) AS hxx_pass_credit11
          FROM Huangxx_ViewStudentScore11
         WHERE hxx_student_id11 = p_student_id
           AND hxx_score_status11 <> '作废'
         GROUP BY hxx_student_id11, hxx_student_name11, hxx_school_year11
         ORDER BY hxx_school_year11;
END;
/

CREATE OR REPLACE PROCEDURE Huangxx_ProcMajorGpaStat11(
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT major.hxx_major_id11,
               major.hxx_major_name11,
               count(stu.hxx_student_id11) AS hxx_student_count11,
               round(avg(stu.hxx_gpa11), 2) AS hxx_avg_gpa11,
               max(stu.hxx_gpa11) AS hxx_max_gpa11,
               min(stu.hxx_gpa11) AS hxx_min_gpa11,
               round(avg(stu.hxx_total_credit11), 2) AS hxx_avg_credit11
          FROM Huangxx_Major11 major
          JOIN Huangxx_Class11 cls ON cls.hxx_major_id11 = major.hxx_major_id11
          JOIN Huangxx_Student11 stu ON stu.hxx_class_id11 = cls.hxx_class_id11
         GROUP BY major.hxx_major_id11, major.hxx_major_name11
         ORDER BY major.hxx_major_id11;
END;
/

CREATE OR REPLACE PROCEDURE Huangxx_ProcAppealStatusStat11(
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT hxx_appeal_status11,
               count(*) AS hxx_appeal_count11,
               count(CASE WHEN hxx_handle_time11 IS NOT NULL THEN 1 END) AS hxx_handled_count11
          FROM Huangxx_ScoreAppeal11
         GROUP BY hxx_appeal_status11
         ORDER BY hxx_appeal_status11;
END;
/

CREATE OR REPLACE PROCEDURE Huangxx_ProcOperationLogStat11(
    INOUT p_result refcursor
)
AS
BEGIN
    OPEN p_result FOR
        SELECT hxx_operation_type11,
               hxx_operation_result11,
               count(*) AS hxx_log_count11,
               min(hxx_operation_time11) AS hxx_first_time11,
               max(hxx_operation_time11) AS hxx_last_time11
          FROM Huangxx_OperationLog11
         GROUP BY hxx_operation_type11, hxx_operation_result11
         ORDER BY hxx_operation_type11, hxx_operation_result11;
END;
/

GRANT CONNECT ON DATABASE huangxx_mis11 TO gaussdb;
GRANT USAGE ON SCHEMA public TO gaussdb;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO gaussdb;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO gaussdb;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO gaussdb;
