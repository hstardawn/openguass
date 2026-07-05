-- 06_triggers.sql

DROP TRIGGER IF EXISTS Huangxx_TrigSetScoreComputed11 ON Huangxx_Score11;
DROP TRIGGER IF EXISTS Huangxx_TrigRecalculateCreditGpa11 ON Huangxx_Score11;
DROP TRIGGER IF EXISTS Huangxx_TrigScoreAudit11 ON Huangxx_Score11;
DROP TRIGGER IF EXISTS Huangxx_TrigCheckSelection11 ON Huangxx_CourseSelection11;
DROP TRIGGER IF EXISTS Huangxx_TrigSelectionCount11 ON Huangxx_CourseSelection11;
DROP TRIGGER IF EXISTS Huangxx_TrigAppealHandleTime11 ON Huangxx_ScoreAppeal11;
DROP TRIGGER IF EXISTS Huangxx_TrigAppealAudit11 ON Huangxx_ScoreAppeal11;
DROP TRIGGER IF EXISTS Huangxx_TrigStudentDefaultUser11 ON Huangxx_Student11;
DROP TRIGGER IF EXISTS Huangxx_TrigTeacherDefaultUser11 ON Huangxx_Teacher11;

DROP FUNCTION IF EXISTS Huangxx_FuncSetScoreComputed11() CASCADE;
DROP FUNCTION IF EXISTS Huangxx_FuncRecalculateCreditGpa11() CASCADE;
DROP FUNCTION IF EXISTS Huangxx_FuncScoreAudit11() CASCADE;
DROP FUNCTION IF EXISTS Huangxx_FuncCheckSelection11() CASCADE;
DROP FUNCTION IF EXISTS Huangxx_FuncSelectionCount11() CASCADE;
DROP FUNCTION IF EXISTS Huangxx_FuncAppealHandleTime11() CASCADE;
DROP FUNCTION IF EXISTS Huangxx_FuncAppealAudit11() CASCADE;
DROP FUNCTION IF EXISTS Huangxx_FuncStudentDefaultUser11() CASCADE;
DROP FUNCTION IF EXISTS Huangxx_FuncTeacherDefaultUser11() CASCADE;

-- 课程设计演示环境保持明文密码策略；新增学生/教师如果没有显式创建登录用户，
-- 由数据库触发器自动写入基础密码 123456，后续可在应用层修改。
CREATE OR REPLACE FUNCTION Huangxx_FuncStudentDefaultUser11()
RETURNS trigger AS $$
BEGIN
    INSERT INTO Huangxx_SystemUser11
    (hxx_user_id11, hxx_login_name11, hxx_password11, hxx_role11, hxx_ref_id11, hxx_user_status11)
    SELECT 'U-' || NEW.hxx_student_id11, lower(NEW.hxx_student_id11), '123456', '学生', NEW.hxx_student_id11, '正常'
     WHERE NOT EXISTS (
        SELECT 1 FROM Huangxx_SystemUser11
         WHERE hxx_ref_id11 = NEW.hxx_student_id11 AND hxx_role11 = '学生'
     );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigStudentDefaultUser11
AFTER INSERT ON Huangxx_Student11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncStudentDefaultUser11();

CREATE OR REPLACE FUNCTION Huangxx_FuncTeacherDefaultUser11()
RETURNS trigger AS $$
BEGIN
    INSERT INTO Huangxx_SystemUser11
    (hxx_user_id11, hxx_login_name11, hxx_password11, hxx_role11, hxx_ref_id11, hxx_user_status11)
    SELECT 'U-' || NEW.hxx_teacher_id11, lower(NEW.hxx_teacher_id11), '123456', '教师', NEW.hxx_teacher_id11, '正常'
     WHERE NOT EXISTS (
        SELECT 1 FROM Huangxx_SystemUser11
         WHERE hxx_ref_id11 = NEW.hxx_teacher_id11 AND hxx_role11 IN ('教师', '管理员')
     );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigTeacherDefaultUser11
AFTER INSERT ON Huangxx_Teacher11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncTeacherDefaultUser11();

CREATE OR REPLACE FUNCTION Huangxx_FuncSetScoreComputed11()
RETURNS trigger AS $$
DECLARE
    v_task_teacher varchar(20);
BEGIN
    SELECT task.hxx_teacher_id11
      INTO v_task_teacher
      FROM Huangxx_CourseSelection11 sel
      JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
     WHERE sel.hxx_selection_id11 = NEW.hxx_selection_id11;

    IF v_task_teacher IS NULL THEN
        RAISE EXCEPTION '选课记录不存在，不能录入成绩';
    END IF;

    IF NEW.hxx_input_teacher11 <> v_task_teacher THEN
        RAISE EXCEPTION '教师 % 不能录入非本人教学任务成绩', NEW.hxx_input_teacher11;
    END IF;

    IF TG_OP = 'UPDATE'
       AND OLD.hxx_lock_flag11 = 'Y'
       AND (OLD.hxx_usual_score11 <> NEW.hxx_usual_score11 OR OLD.hxx_exam_score11 <> NEW.hxx_exam_score11)
       AND COALESCE(NEW.hxx_modify_reason11, '') = '' THEN
        RAISE EXCEPTION '已锁定成绩修改必须填写修改原因';
    END IF;

    NEW.hxx_final_score11 := round(NEW.hxx_usual_score11 * 0.4 + NEW.hxx_exam_score11 * 0.6, 2);
    NEW.hxx_grade_level11 := CASE
        WHEN NEW.hxx_final_score11 >= 90 THEN '优秀'
        WHEN NEW.hxx_final_score11 >= 80 THEN '良好'
        WHEN NEW.hxx_final_score11 >= 70 THEN '中等'
        WHEN NEW.hxx_final_score11 >= 60 THEN '及格'
        ELSE '不及格'
    END;
    NEW.hxx_pass_flag11 := CASE WHEN NEW.hxx_final_score11 >= 60 THEN 'Y' ELSE 'N' END;
    NEW.hxx_grade_point11 := CASE
        WHEN NEW.hxx_final_score11 >= 90 THEN 4.00
        WHEN NEW.hxx_final_score11 >= 80 THEN 3.00
        WHEN NEW.hxx_final_score11 >= 70 THEN 2.00
        WHEN NEW.hxx_final_score11 >= 60 THEN 1.00
        ELSE 0.00
    END;
    NEW.hxx_update_time11 := current_timestamp;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigSetScoreComputed11
BEFORE INSERT OR UPDATE ON Huangxx_Score11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncSetScoreComputed11();

CREATE OR REPLACE FUNCTION Huangxx_FuncRecalculateCreditGpa11()
RETURNS trigger AS $$
DECLARE
    v_student_id varchar(20);
    v_course_id varchar(20);
    v_score_id varchar(30);
    v_before_credit numeric(5,1);
    v_after_credit numeric(5,1);
    v_after_gpa numeric(3,2);
BEGIN
    IF TG_OP = 'DELETE' THEN
        v_score_id := OLD.hxx_score_id11;
        SELECT sel.hxx_student_id11, task.hxx_course_id11
          INTO v_student_id, v_course_id
          FROM Huangxx_CourseSelection11 sel
          JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
         WHERE sel.hxx_selection_id11 = OLD.hxx_selection_id11;
    ELSE
        v_score_id := NEW.hxx_score_id11;
        SELECT sel.hxx_student_id11, task.hxx_course_id11
          INTO v_student_id, v_course_id
          FROM Huangxx_CourseSelection11 sel
          JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
         WHERE sel.hxx_selection_id11 = NEW.hxx_selection_id11;
    END IF;

    SELECT hxx_total_credit11 INTO v_before_credit
      FROM Huangxx_Student11
     WHERE hxx_student_id11 = v_student_id;

    SELECT
        COALESCE(sum(CASE WHEN sc.hxx_pass_flag11 = 'Y' AND sc.hxx_score_status11 <> '作废' THEN c.hxx_credit11 ELSE 0 END), 0),
        COALESCE(round(sum(sc.hxx_grade_point11 * c.hxx_credit11) / nullif(sum(c.hxx_credit11), 0), 2), 0)
      INTO v_after_credit, v_after_gpa
      FROM Huangxx_CourseSelection11 sel
      JOIN Huangxx_TeachingTask11 task ON task.hxx_task_id11 = sel.hxx_task_id11
      JOIN Huangxx_Course11 c ON c.hxx_course_id11 = task.hxx_course_id11
      JOIN Huangxx_Score11 sc ON sc.hxx_selection_id11 = sel.hxx_selection_id11
     WHERE sel.hxx_student_id11 = v_student_id
       AND sc.hxx_score_status11 <> '作废';

    UPDATE Huangxx_Student11
       SET hxx_total_credit11 = v_after_credit,
           hxx_gpa11 = v_after_gpa
     WHERE hxx_student_id11 = v_student_id;

    IF v_before_credit <> v_after_credit THEN
        INSERT INTO Huangxx_CreditLog11
        VALUES ('CLG' || to_char(current_timestamp, 'YYYYMMDDHH24MISSMS') || substr(md5(random()::text), 1, 4),
                v_student_id, v_course_id, v_score_id, v_after_credit - v_before_credit,
                v_before_credit, v_after_credit, '成绩变化自动重算学分', current_timestamp);
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigRecalculateCreditGpa11
AFTER INSERT OR UPDATE OR DELETE ON Huangxx_Score11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncRecalculateCreditGpa11();

CREATE OR REPLACE FUNCTION Huangxx_FuncScoreAudit11()
RETURNS trigger AS $$
BEGIN
    IF OLD.hxx_usual_score11 <> NEW.hxx_usual_score11 OR OLD.hxx_exam_score11 <> NEW.hxx_exam_score11 THEN
        IF COALESCE(NEW.hxx_modify_reason11, '') = '' THEN
            RAISE EXCEPTION '成绩修改必须填写修改原因';
        END IF;

        INSERT INTO Huangxx_ScoreAudit11
        VALUES ('AUD' || to_char(current_timestamp, 'YYYYMMDDHH24MISSMS') || substr(md5(random()::text), 1, 4),
                NEW.hxx_score_id11,
                OLD.hxx_usual_score11, NEW.hxx_usual_score11,
                OLD.hxx_exam_score11, NEW.hxx_exam_score11,
                OLD.hxx_final_score11, NEW.hxx_final_score11,
                COALESCE(NEW.hxx_modifier_id11, NEW.hxx_input_teacher11),
                COALESCE(NEW.hxx_modifier_role11, '教师'),
                NEW.hxx_modify_reason11,
                current_timestamp);
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigScoreAudit11
AFTER UPDATE ON Huangxx_Score11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncScoreAudit11();

CREATE OR REPLACE FUNCTION Huangxx_FuncCheckSelection11()
RETURNS trigger AS $$
DECLARE
    v_max_count int;
    v_active_count int;
    v_task_status varchar(20);
BEGIN
    SELECT hxx_max_count11, hxx_task_status11
      INTO v_max_count, v_task_status
      FROM Huangxx_TeachingTask11
     WHERE hxx_task_id11 = NEW.hxx_task_id11;

    IF v_task_status IS NULL THEN
        RAISE EXCEPTION '教学任务不存在';
    END IF;

    IF v_task_status NOT IN ('未开始', '开课中') AND NEW.hxx_selection_status11 <> '退选' THEN
        RAISE EXCEPTION '教学任务状态为 %, 不允许选课', v_task_status;
    END IF;

    IF NEW.hxx_selection_status11 <> '退选' THEN
        SELECT count(*) INTO v_active_count
          FROM Huangxx_CourseSelection11
         WHERE hxx_task_id11 = NEW.hxx_task_id11
           AND hxx_selection_status11 <> '退选'
           AND hxx_selection_id11 <> NEW.hxx_selection_id11;

        IF v_active_count >= v_max_count THEN
            RAISE EXCEPTION '教学任务 % 已满，不能继续选课', NEW.hxx_task_id11;
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigCheckSelection11
BEFORE INSERT OR UPDATE ON Huangxx_CourseSelection11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncCheckSelection11();

CREATE OR REPLACE FUNCTION Huangxx_FuncSelectionCount11()
RETURNS trigger AS $$
DECLARE
    v_task_id varchar(20);
BEGIN
    v_task_id := COALESCE(NEW.hxx_task_id11, OLD.hxx_task_id11);
    UPDATE Huangxx_TeachingTask11
       SET hxx_current_count11 = (
           SELECT count(*)
             FROM Huangxx_CourseSelection11
            WHERE hxx_task_id11 = v_task_id
              AND hxx_selection_status11 <> '退选'
       )
     WHERE hxx_task_id11 = v_task_id;

    IF TG_OP = 'UPDATE' AND OLD.hxx_task_id11 <> NEW.hxx_task_id11 THEN
        UPDATE Huangxx_TeachingTask11
           SET hxx_current_count11 = (
               SELECT count(*)
                 FROM Huangxx_CourseSelection11
                WHERE hxx_task_id11 = OLD.hxx_task_id11
                  AND hxx_selection_status11 <> '退选'
           )
         WHERE hxx_task_id11 = OLD.hxx_task_id11;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigSelectionCount11
AFTER INSERT OR UPDATE OR DELETE ON Huangxx_CourseSelection11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncSelectionCount11();

CREATE OR REPLACE FUNCTION Huangxx_FuncAppealHandleTime11()
RETURNS trigger AS $$
BEGIN
    IF OLD.hxx_appeal_status11 = '待处理'
       AND NEW.hxx_appeal_status11 IN ('已通过', '已驳回')
       AND NEW.hxx_handle_time11 IS NULL THEN
        NEW.hxx_handle_time11 := current_timestamp;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigAppealHandleTime11
BEFORE UPDATE ON Huangxx_ScoreAppeal11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncAppealHandleTime11();

CREATE OR REPLACE FUNCTION Huangxx_FuncAppealAudit11()
RETURNS trigger AS $$
BEGIN
    IF OLD.hxx_appeal_status11 = '待处理'
       AND NEW.hxx_appeal_status11 IN ('已通过', '已驳回') THEN
        INSERT INTO Huangxx_OperationLog11
        VALUES ('LOG' || to_char(current_timestamp, 'YYYYMMDDHH24MISSMS') || substr(md5(random()::text), 1, 4),
                NEW.hxx_handler_id11,
                NEW.hxx_handler_id11,
                '教师',
                '申诉处理',
                NEW.hxx_appeal_id11,
                '处理成绩申诉，结果：' || NEW.hxx_appeal_status11 || '，说明：' || COALESCE(NEW.hxx_handle_result11, ''),
                '成功',
                current_timestamp);
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER Huangxx_TrigAppealAudit11
AFTER UPDATE ON Huangxx_ScoreAppeal11
FOR EACH ROW EXECUTE PROCEDURE Huangxx_FuncAppealAudit11();
