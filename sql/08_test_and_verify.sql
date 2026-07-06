-- 08_test_and_verify.sql
-- 本文件用于答辩时验证表、视图、触发器和存储过程。

-- 1. 基础数据数量检查
SELECT '学生数量' AS hxx_check_item11, count(*) AS hxx_count11 FROM Huangxx_Student11;
SELECT '教师数量' AS hxx_check_item11, count(*) AS hxx_count11 FROM Huangxx_Teacher11;
SELECT '课程数量' AS hxx_check_item11, count(*) AS hxx_count11 FROM Huangxx_Course11;
SELECT '成绩数量' AS hxx_check_item11, count(*) AS hxx_count11 FROM Huangxx_Score11;

-- 2. 视图检查
SELECT * FROM Huangxx_ViewStudentScore11 ORDER BY hxx_student_id11, hxx_course_name11 LIMIT 10;
SELECT * FROM Huangxx_ViewTeacherTask11 ORDER BY hxx_teacher_id11, hxx_task_id11;
SELECT * FROM Huangxx_ViewClassCourse11 ORDER BY hxx_class_id11, hxx_course_name11;
SELECT * FROM Huangxx_ViewCourseStat11 ORDER BY hxx_task_id11;
SELECT * FROM Huangxx_ViewScoreRank11 ORDER BY hxx_task_id11, hxx_course_rank11 LIMIT 10;
SELECT * FROM Huangxx_ViewStudentGpaCredit11 ORDER BY hxx_student_id11 LIMIT 10;
SELECT * FROM Huangxx_ViewRegionStudentStat11 ORDER BY hxx_region_name11;
SELECT * FROM Huangxx_ViewScoreAppeal11 ORDER BY hxx_create_time11 DESC;

-- 3. 触发器验证：成绩自动计算、GPA/学分重算
DELETE FROM Huangxx_ScoreAudit11 WHERE hxx_score_id11 = 'SC-DEMO-001';
DELETE FROM Huangxx_Score11 WHERE hxx_score_id11 = 'SC-DEMO-001';
DELETE FROM Huangxx_CourseSelection11 WHERE hxx_selection_id11 = 'SEL-DEMO-001';

INSERT INTO Huangxx_CourseSelection11
(hxx_selection_id11, hxx_student_id11, hxx_task_id11, hxx_selection_status11)
VALUES ('SEL-DEMO-001', 'S2023016', 'TASK003', '已选');

UPDATE Huangxx_TeachingTask11
   SET hxx_task_status11 = '已结束'
 WHERE hxx_task_id11 = 'TASK003';

INSERT INTO Huangxx_Score11
(hxx_score_id11, hxx_selection_id11, hxx_usual_score11, hxx_exam_score11, hxx_input_teacher11, hxx_publish_flag11)
VALUES ('SC-DEMO-001', 'SEL-DEMO-001', 88, 92, 'T003', 'N');

SELECT hxx_score_id11, hxx_usual_score11, hxx_exam_score11, hxx_final_score11,
       hxx_grade_level11, hxx_pass_flag11, hxx_grade_point11
  FROM Huangxx_Score11
 WHERE hxx_score_id11 = 'SC-DEMO-001';

SELECT hxx_student_id11, hxx_total_credit11, hxx_gpa11
  FROM Huangxx_Student11
 WHERE hxx_student_id11 = 'S2023016';

-- 4. 触发器验证：成绩修改审计
UPDATE Huangxx_Score11
   SET hxx_usual_score11 = 90,
       hxx_exam_score11 = 94,
       hxx_modifier_id11 = 'T003',
       hxx_modifier_role11 = '教师',
       hxx_modify_reason11 = '答辩演示：复核后调整成绩'
 WHERE hxx_score_id11 = 'SC-DEMO-001';

SELECT * FROM Huangxx_ScoreAudit11
 WHERE hxx_score_id11 = 'SC-DEMO-001'
 ORDER BY hxx_modify_time11 DESC;

UPDATE Huangxx_TeachingTask11
   SET hxx_task_status11 = '开课中'
 WHERE hxx_task_id11 = 'TASK003';

-- 5. 触发器验证：重复选课会被唯一约束阻止，容量检查可通过插入满员任务验证。
-- 下面语句用于答辩现场单独执行，预期失败：
-- INSERT INTO Huangxx_CourseSelection11(hxx_selection_id11, hxx_student_id11, hxx_task_id11) VALUES ('SEL-DUP-001', 'S2023001', 'TASK001');

-- 6. 触发器验证：教师不能录入非本人教学任务成绩，预期失败：
-- INSERT INTO Huangxx_Score11(hxx_score_id11, hxx_selection_id11, hxx_usual_score11, hxx_exam_score11, hxx_input_teacher11) VALUES ('SC-WRONG-001', 'SEL011', 80, 80, 'T001');

-- 7. 申诉处理触发操作日志
UPDATE Huangxx_ScoreAppeal11
   SET hxx_appeal_status11 = '已通过',
       hxx_handler_id11 = 'T001',
       hxx_handle_result11 = '答辩演示：已复核并给出处理意见'
 WHERE hxx_appeal_id11 = 'AP001';

SELECT * FROM Huangxx_OperationLog11
 WHERE hxx_operation_type11 = '申诉处理'
 ORDER BY hxx_operation_time11 DESC;

-- 8. 存储过程调用示例。openGauss 使用 refcursor 时需要在事务内取游标。
BEGIN;
CALL Huangxx_ProcStudentTranscript11('S2023001', 'cur_student_transcript11');
FETCH ALL FROM cur_student_transcript11;
COMMIT;

BEGIN;
CALL Huangxx_ProcCourseScoreStat11('TASK001', 'cur_course_stat11');
FETCH ALL FROM cur_course_stat11;
COMMIT;

BEGIN;
CALL Huangxx_ProcClassCreditStat11('CL-CS2301', 'cur_class_credit11');
FETCH ALL FROM cur_class_credit11;
COMMIT;

BEGIN;
CALL Huangxx_ProcRegionStudentStat11('cur_region_stat11');
FETCH ALL FROM cur_region_stat11;
COMMIT;

BEGIN;
CALL Huangxx_ProcTeacherTaskStat11('T001', 'cur_teacher_task11');
FETCH ALL FROM cur_teacher_task11;
COMMIT;

BEGIN;
CALL Huangxx_ProcStudentYearlyScoreStat11('S2023001', 'cur_year_score11');
FETCH ALL FROM cur_year_score11;
COMMIT;

BEGIN;
CALL Huangxx_ProcMajorGpaStat11('cur_major_gpa11');
FETCH ALL FROM cur_major_gpa11;
COMMIT;

BEGIN;
CALL Huangxx_ProcAppealStatusStat11('cur_appeal_status11');
FETCH ALL FROM cur_appeal_status11;
COMMIT;

BEGIN;
CALL Huangxx_ProcOperationLogStat11('cur_log_stat11');
FETCH ALL FROM cur_log_stat11;
COMMIT;
