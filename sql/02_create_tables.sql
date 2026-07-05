-- 02_create_tables.sql
-- 请先连接数据库：\c HuangxxMIS11

DROP TABLE IF EXISTS Huangxx_OperationLog11 CASCADE;
DROP TABLE IF EXISTS Huangxx_ScoreAppeal11 CASCADE;
DROP TABLE IF EXISTS Huangxx_CreditLog11 CASCADE;
DROP TABLE IF EXISTS Huangxx_ScoreAudit11 CASCADE;
DROP TABLE IF EXISTS Huangxx_Score11 CASCADE;
DROP TABLE IF EXISTS Huangxx_CourseSelection11 CASCADE;
DROP TABLE IF EXISTS Huangxx_TeachingTask11 CASCADE;
DROP TABLE IF EXISTS Huangxx_Term11 CASCADE;
DROP TABLE IF EXISTS Huangxx_Course11 CASCADE;
DROP TABLE IF EXISTS Huangxx_Teacher11 CASCADE;
DROP TABLE IF EXISTS Huangxx_Student11 CASCADE;
DROP TABLE IF EXISTS Huangxx_Class11 CASCADE;
DROP TABLE IF EXISTS Huangxx_Major11 CASCADE;
DROP TABLE IF EXISTS Huangxx_Region11 CASCADE;
DROP TABLE IF EXISTS Huangxx_SystemUser11 CASCADE;

CREATE TABLE Huangxx_Region11 (
    hxx_region_id11 varchar(20) PRIMARY KEY,
    hxx_region_name11 varchar(60) NOT NULL,
    hxx_region_level11 varchar(20) NOT NULL CHECK (hxx_region_level11 IN ('省', '市', '区县')),
    hxx_parent_region_id11 varchar(20),
    hxx_region_remark11 varchar(200),
    CONSTRAINT fk_Huangxx_Region_parent11
        FOREIGN KEY (hxx_parent_region_id11) REFERENCES Huangxx_Region11(hxx_region_id11)
);

CREATE TABLE Huangxx_Major11 (
    hxx_major_id11 varchar(20) PRIMARY KEY,
    hxx_major_name11 varchar(60) NOT NULL UNIQUE,
    hxx_college_name11 varchar(80) NOT NULL,
    hxx_major_desc11 varchar(200),
    hxx_create_time11 timestamp DEFAULT current_timestamp
);

CREATE TABLE Huangxx_Class11 (
    hxx_class_id11 varchar(20) PRIMARY KEY,
    hxx_class_name11 varchar(60) NOT NULL,
    hxx_major_id11 varchar(20) NOT NULL,
    hxx_grade_year11 int NOT NULL CHECK (hxx_grade_year11 BETWEEN 2000 AND 2100),
    hxx_class_size11 int DEFAULT 0 CHECK (hxx_class_size11 >= 0),
    hxx_remark11 varchar(200),
    CONSTRAINT fk_Huangxx_Class_major11
        FOREIGN KEY (hxx_major_id11) REFERENCES Huangxx_Major11(hxx_major_id11)
);

CREATE TABLE Huangxx_Student11 (
    hxx_student_id11 varchar(20) PRIMARY KEY,
    hxx_student_name11 varchar(40) NOT NULL,
    hxx_gender11 varchar(8) NOT NULL CHECK (hxx_gender11 IN ('男', '女')),
    hxx_age11 int NOT NULL CHECK (hxx_age11 BETWEEN 15 AND 35),
    hxx_region_id11 varchar(20) NOT NULL,
    hxx_class_id11 varchar(20) NOT NULL,
    hxx_total_credit11 numeric(5,1) DEFAULT 0 CHECK (hxx_total_credit11 >= 0),
    hxx_gpa11 numeric(3,2) DEFAULT 0 CHECK (hxx_gpa11 BETWEEN 0 AND 5),
    hxx_phone11 varchar(20) UNIQUE,
    hxx_status11 varchar(20) DEFAULT '在读' CHECK (hxx_status11 IN ('在读', '休学', '毕业', '退学')),
    hxx_enroll_date11 date,
    CONSTRAINT fk_Huangxx_Student_region11
        FOREIGN KEY (hxx_region_id11) REFERENCES Huangxx_Region11(hxx_region_id11),
    CONSTRAINT fk_Huangxx_Student_class11
        FOREIGN KEY (hxx_class_id11) REFERENCES Huangxx_Class11(hxx_class_id11)
);

CREATE TABLE Huangxx_Teacher11 (
    hxx_teacher_id11 varchar(20) PRIMARY KEY,
    hxx_teacher_name11 varchar(40) NOT NULL,
    hxx_gender11 varchar(8) NOT NULL CHECK (hxx_gender11 IN ('男', '女')),
    hxx_age11 int NOT NULL CHECK (hxx_age11 BETWEEN 22 AND 70),
    hxx_title11 varchar(40) NOT NULL CHECK (hxx_title11 IN ('助教', '讲师', '副教授', '教授')),
    hxx_phone11 varchar(20) UNIQUE,
    hxx_college_name11 varchar(80) NOT NULL,
    hxx_status11 varchar(20) DEFAULT '在职' CHECK (hxx_status11 IN ('在职', '离职', '退休'))
);

CREATE TABLE Huangxx_Course11 (
    hxx_course_id11 serial PRIMARY KEY,
    hxx_course_name11 varchar(80) NOT NULL,
    hxx_credit11 numeric(3,1) NOT NULL CHECK (hxx_credit11 > 0),
    hxx_period11 int NOT NULL CHECK (hxx_period11 > 0),
    hxx_exam_type11 varchar(20) NOT NULL CHECK (hxx_exam_type11 IN ('考试', '考查')),
    hxx_course_type11 varchar(20) NOT NULL CHECK (hxx_course_type11 IN ('必修', '选修', '通识')),
    hxx_course_desc11 varchar(200)
);

CREATE TABLE Huangxx_Term11 (
    hxx_term_id11 varchar(20) PRIMARY KEY,
    hxx_school_year11 varchar(20) NOT NULL,
    hxx_semester11 varchar(20) NOT NULL CHECK (hxx_semester11 IN ('第一学期', '第二学期', '短学期')),
    hxx_start_date11 date NOT NULL,
    hxx_end_date11 date NOT NULL,
    hxx_term_status11 varchar(20) DEFAULT '启用' CHECK (hxx_term_status11 IN ('启用', '停用')),
    CHECK (hxx_end_date11 > hxx_start_date11)
);

CREATE TABLE Huangxx_TeachingTask11 (
    hxx_task_id11 varchar(20) PRIMARY KEY,
    hxx_course_id11 integer NOT NULL,
    hxx_teacher_id11 varchar(20) NOT NULL,
    hxx_class_id11 varchar(20) NOT NULL,
    hxx_term_id11 varchar(20) NOT NULL,
    hxx_teaching_place11 varchar(80),
    hxx_max_count11 int DEFAULT 60 CHECK (hxx_max_count11 > 0),
    hxx_current_count11 int DEFAULT 0 CHECK (hxx_current_count11 >= 0),
    hxx_task_status11 varchar(20) DEFAULT '开课中' CHECK (hxx_task_status11 IN ('未开始', '开课中', '已结束', '取消')),
    hxx_score_publish_flag11 char(1) DEFAULT 'N' CHECK (hxx_score_publish_flag11 IN ('Y', 'N')),
    CONSTRAINT uq_Huangxx_TeachingTask11 UNIQUE (hxx_course_id11, hxx_teacher_id11, hxx_class_id11, hxx_term_id11),
    CONSTRAINT fk_Huangxx_Task_course11 FOREIGN KEY (hxx_course_id11) REFERENCES Huangxx_Course11(hxx_course_id11),
    CONSTRAINT fk_Huangxx_Task_teacher11 FOREIGN KEY (hxx_teacher_id11) REFERENCES Huangxx_Teacher11(hxx_teacher_id11),
    CONSTRAINT fk_Huangxx_Task_class11 FOREIGN KEY (hxx_class_id11) REFERENCES Huangxx_Class11(hxx_class_id11),
    CONSTRAINT fk_Huangxx_Task_term11 FOREIGN KEY (hxx_term_id11) REFERENCES Huangxx_Term11(hxx_term_id11)
);

CREATE TABLE Huangxx_CourseSelection11 (
    hxx_selection_id11 varchar(30) PRIMARY KEY,
    hxx_student_id11 varchar(20) NOT NULL,
    hxx_task_id11 varchar(20) NOT NULL,
    hxx_select_time11 timestamp DEFAULT current_timestamp,
    hxx_selection_status11 varchar(20) DEFAULT '已选' CHECK (hxx_selection_status11 IN ('已选', '退选', '完成')),
    hxx_remark11 varchar(200),
    CONSTRAINT uq_Huangxx_CourseSelection11 UNIQUE (hxx_student_id11, hxx_task_id11),
    CONSTRAINT fk_Huangxx_Selection_student11 FOREIGN KEY (hxx_student_id11) REFERENCES Huangxx_Student11(hxx_student_id11),
    CONSTRAINT fk_Huangxx_Selection_task11 FOREIGN KEY (hxx_task_id11) REFERENCES Huangxx_TeachingTask11(hxx_task_id11)
);

CREATE TABLE Huangxx_Score11 (
    hxx_score_id11 varchar(30) PRIMARY KEY,
    hxx_selection_id11 varchar(30) NOT NULL UNIQUE,
    hxx_usual_score11 numeric(5,2) NOT NULL CHECK (hxx_usual_score11 BETWEEN 0 AND 100),
    hxx_exam_score11 numeric(5,2) NOT NULL CHECK (hxx_exam_score11 BETWEEN 0 AND 100),
    hxx_final_score11 numeric(5,2) CHECK (hxx_final_score11 BETWEEN 0 AND 100),
    hxx_grade_level11 varchar(20),
    hxx_pass_flag11 char(1) CHECK (hxx_pass_flag11 IN ('Y', 'N')),
    hxx_grade_point11 numeric(3,2) DEFAULT 0 CHECK (hxx_grade_point11 BETWEEN 0 AND 5),
    hxx_exam_status11 varchar(20) DEFAULT '正常' CHECK (hxx_exam_status11 IN ('正常', '缺考', '缓考', '作弊')),
    hxx_score_source11 varchar(20) DEFAULT '教师录入' CHECK (hxx_score_source11 IN ('教师录入', '管理员导入', '补录')),
    hxx_input_teacher11 varchar(20) NOT NULL,
    hxx_input_time11 timestamp DEFAULT current_timestamp,
    hxx_score_status11 varchar(20) DEFAULT '正常' CHECK (hxx_score_status11 IN ('正常', '已修改', '作废')),
    hxx_publish_flag11 char(1) DEFAULT 'N' CHECK (hxx_publish_flag11 IN ('Y', 'N')),
    hxx_lock_flag11 char(1) DEFAULT 'N' CHECK (hxx_lock_flag11 IN ('Y', 'N')),
    hxx_modifier_id11 varchar(30),
    hxx_modifier_role11 varchar(20) CHECK (hxx_modifier_role11 IS NULL OR hxx_modifier_role11 IN ('管理员', '教师')),
    hxx_modify_reason11 varchar(200),
    hxx_update_time11 timestamp,
    CONSTRAINT fk_Huangxx_Score_selection11 FOREIGN KEY (hxx_selection_id11) REFERENCES Huangxx_CourseSelection11(hxx_selection_id11),
    CONSTRAINT fk_Huangxx_Score_teacher11 FOREIGN KEY (hxx_input_teacher11) REFERENCES Huangxx_Teacher11(hxx_teacher_id11)
);

CREATE TABLE Huangxx_ScoreAudit11 (
    hxx_audit_id11 varchar(30) PRIMARY KEY,
    hxx_score_id11 varchar(30) NOT NULL,
    hxx_old_usual_score11 numeric(5,2),
    hxx_new_usual_score11 numeric(5,2),
    hxx_old_exam_score11 numeric(5,2),
    hxx_new_exam_score11 numeric(5,2),
    hxx_old_score11 numeric(5,2) NOT NULL CHECK (hxx_old_score11 BETWEEN 0 AND 100),
    hxx_new_score11 numeric(5,2) NOT NULL CHECK (hxx_new_score11 BETWEEN 0 AND 100),
    hxx_operator_id11 varchar(30) NOT NULL,
    hxx_operator_role11 varchar(20) NOT NULL CHECK (hxx_operator_role11 IN ('管理员', '教师')),
    hxx_modify_reason11 varchar(200) NOT NULL,
    hxx_modify_time11 timestamp DEFAULT current_timestamp,
    CONSTRAINT fk_Huangxx_Audit_score11 FOREIGN KEY (hxx_score_id11) REFERENCES Huangxx_Score11(hxx_score_id11)
);

CREATE TABLE Huangxx_CreditLog11 (
    hxx_credit_log_id11 varchar(30) PRIMARY KEY,
    hxx_student_id11 varchar(20) NOT NULL,
    hxx_course_id11 integer NOT NULL,
    hxx_score_id11 varchar(30) NOT NULL,
    hxx_credit_change11 numeric(3,1) NOT NULL CHECK (hxx_credit_change11 <> 0),
    hxx_before_credit11 numeric(5,1) NOT NULL CHECK (hxx_before_credit11 >= 0),
    hxx_after_credit11 numeric(5,1) NOT NULL CHECK (hxx_after_credit11 >= 0),
    hxx_change_reason11 varchar(100) NOT NULL,
    hxx_change_time11 timestamp DEFAULT current_timestamp,
    CONSTRAINT fk_Huangxx_CreditLog_student11 FOREIGN KEY (hxx_student_id11) REFERENCES Huangxx_Student11(hxx_student_id11),
    CONSTRAINT fk_Huangxx_CreditLog_course11 FOREIGN KEY (hxx_course_id11) REFERENCES Huangxx_Course11(hxx_course_id11)
);

CREATE TABLE Huangxx_ScoreAppeal11 (
    hxx_appeal_id11 varchar(30) PRIMARY KEY,
    hxx_score_id11 varchar(30) NOT NULL,
    hxx_student_id11 varchar(20) NOT NULL,
    hxx_appeal_reason11 varchar(300) NOT NULL,
    hxx_appeal_status11 varchar(20) DEFAULT '待处理' CHECK (hxx_appeal_status11 IN ('待处理', '已通过', '已驳回', '已撤回')),
    hxx_handler_id11 varchar(30),
    hxx_handle_result11 varchar(300),
    hxx_create_time11 timestamp DEFAULT current_timestamp,
    hxx_handle_time11 timestamp,
    CONSTRAINT fk_Huangxx_Appeal_score11 FOREIGN KEY (hxx_score_id11) REFERENCES Huangxx_Score11(hxx_score_id11),
    CONSTRAINT fk_Huangxx_Appeal_student11 FOREIGN KEY (hxx_student_id11) REFERENCES Huangxx_Student11(hxx_student_id11)
);

CREATE TABLE Huangxx_SystemUser11 (
    hxx_user_id11 varchar(30) PRIMARY KEY,
    hxx_login_name11 varchar(40) NOT NULL UNIQUE,
    hxx_password11 varchar(100) NOT NULL,
    hxx_role11 varchar(20) NOT NULL CHECK (hxx_role11 IN ('管理员', '教师', '学生')),
    hxx_ref_id11 varchar(20),
    hxx_user_status11 varchar(20) DEFAULT '正常' CHECK (hxx_user_status11 IN ('正常', '禁用')),
    hxx_create_time11 timestamp DEFAULT current_timestamp,
    hxx_last_login_time11 timestamp
);

CREATE TABLE Huangxx_OperationLog11 (
    hxx_log_id11 varchar(30) PRIMARY KEY,
    hxx_user_id11 varchar(30),
    hxx_login_name11 varchar(40),
    hxx_role11 varchar(20),
    hxx_operation_type11 varchar(40) NOT NULL,
    hxx_operation_object11 varchar(80),
    hxx_operation_desc11 varchar(300),
    hxx_operation_result11 varchar(20) DEFAULT '成功' CHECK (hxx_operation_result11 IN ('成功', '失败')),
    hxx_operation_time11 timestamp DEFAULT current_timestamp
);
