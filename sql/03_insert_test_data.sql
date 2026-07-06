-- 03_insert_test_data.sql
-- 测试数据覆盖管理员、教师、学生三类角色和完整演示链路。

INSERT INTO Huangxx_Region11 VALUES
('R-BJ', '北京市', '省', NULL, '华北地区，直辖市'),
('R-TJ', '天津市', '省', NULL, '华北地区，直辖市'),
('R-HEB', '河北省', '省', NULL, '华北地区'),
('R-SX', '山西省', '省', NULL, '华北地区'),
('R-NMG', '内蒙古自治区', '省', NULL, '华北地区'),
('R-LN', '辽宁省', '省', NULL, '东北地区'),
('R-JL', '吉林省', '省', NULL, '东北地区'),
('R-HLJ', '黑龙江省', '省', NULL, '东北地区'),
('R-SH', '上海市', '省', NULL, '华东地区，直辖市'),
('R-JS', '江苏省', '省', NULL, '华东地区'),
('R-ZJ', '浙江省', '省', NULL, '华东地区'),
('R-AH', '安徽省', '省', NULL, '华东地区'),
('R-FJ', '福建省', '省', NULL, '华东地区'),
('R-JX', '江西省', '省', NULL, '华东地区'),
('R-SD', '山东省', '省', NULL, '华东地区'),
('R-HA', '河南省', '省', NULL, '华中地区'),
('R-HB', '湖北省', '省', NULL, '华中地区'),
('R-HN', '湖南省', '省', NULL, '华中地区'),
('R-GD', '广东省', '省', NULL, '华南地区'),
('R-GUANGXI', '广西壮族自治区', '省', NULL, '华南地区'),
('R-HAIN', '海南省', '省', NULL, '华南地区'),
('R-CQ', '重庆市', '省', NULL, '西南地区，直辖市'),
('R-SC', '四川省', '省', NULL, '西南地区'),
('R-GZP', '贵州省', '省', NULL, '西南地区'),
('R-YN', '云南省', '省', NULL, '西南地区'),
('R-XZ', '西藏自治区', '省', NULL, '西南地区'),
('R-SN', '陕西省', '省', NULL, '西北地区'),
('R-GS', '甘肃省', '省', NULL, '西北地区'),
('R-QH', '青海省', '省', NULL, '西北地区'),
('R-NX', '宁夏回族自治区', '省', NULL, '西北地区'),
('R-XJ', '新疆维吾尔自治区', '省', NULL, '西北地区'),
('R-HK', '香港特别行政区', '省', NULL, '港澳台地区'),
('R-MO', '澳门特别行政区', '省', NULL, '港澳台地区'),
('R-TW', '台湾省', '省', NULL, '港澳台地区'),
('R-BJ-C', '北京市', '市', 'R-BJ', '直辖市市级节点'),
('R-TJ-C', '天津市', '市', 'R-TJ', '直辖市市级节点'),
('R-SH-C', '上海市', '市', 'R-SH', '直辖市市级节点'),
('R-CQ-C', '重庆市', '市', 'R-CQ', '直辖市市级节点'),
('R-SJZ', '石家庄市', '市', 'R-HEB', NULL),
('R-TY', '太原市', '市', 'R-SX', NULL),
('R-HHHT', '呼和浩特市', '市', 'R-NMG', NULL),
('R-SY', '沈阳市', '市', 'R-LN', NULL),
('R-CC', '长春市', '市', 'R-JL', NULL),
('R-HEB-C', '哈尔滨市', '市', 'R-HLJ', NULL),
('R-NJ', '南京市', '市', 'R-JS', NULL),
('R-SUZ', '苏州市', '市', 'R-JS', NULL),
('R-HZ', '杭州市', '市', 'R-ZJ', NULL),
('R-NB', '宁波市', '市', 'R-ZJ', NULL),
('R-HF', '合肥市', '市', 'R-AH', NULL),
('R-FZ', '福州市', '市', 'R-FJ', NULL),
('R-NC', '南昌市', '市', 'R-JX', NULL),
('R-JN', '济南市', '市', 'R-SD', NULL),
('R-ZZ', '郑州市', '市', 'R-HA', NULL),
('R-WH', '武汉市', '市', 'R-HB', NULL),
('R-CS', '长沙市', '市', 'R-HN', NULL),
('R-GZ', '广州市', '市', 'R-GD', NULL),
('R-SZ', '深圳市', '市', 'R-GD', NULL),
('R-NN', '南宁市', '市', 'R-GUANGXI', NULL),
('R-HKOU', '海口市', '市', 'R-HAIN', NULL),
('R-CD', '成都市', '市', 'R-SC', NULL),
('R-GY', '贵阳市', '市', 'R-GZP', NULL),
('R-KM', '昆明市', '市', 'R-YN', NULL),
('R-LS', '拉萨市', '市', 'R-XZ', NULL),
('R-XA', '西安市', '市', 'R-SN', NULL),
('R-LZ', '兰州市', '市', 'R-GS', NULL),
('R-XN', '西宁市', '市', 'R-QH', NULL),
('R-YC', '银川市', '市', 'R-NX', NULL),
('R-WLMQ', '乌鲁木齐市', '市', 'R-XJ', NULL),
('R-HK-C', '香港特别行政区', '市', 'R-HK', '特别行政区市级节点'),
('R-MO-C', '澳门特别行政区', '市', 'R-MO', '特别行政区市级节点'),
('R-TPE', '台北市', '市', 'R-TW', NULL),
('R-HD', '海淀区', '区县', 'R-BJ-C', NULL),
('R-HP', '黄浦区', '区县', 'R-SH-C', NULL),
('R-PD', '浦东新区', '区县', 'R-SH-C', NULL),
('R-HP-TJ', '和平区', '区县', 'R-TJ-C', NULL),
('R-YZ', '渝中区', '区县', 'R-CQ-C', NULL),
('R-XW', '玄武区', '区县', 'R-NJ', NULL),
('R-GYQ', '姑苏区', '区县', 'R-SUZ', NULL),
('R-XH', '西湖区', '区县', 'R-HZ', NULL),
('R-BL', '北仑区', '区县', 'R-NB', NULL),
('R-SS', '蜀山区', '区县', 'R-HF', NULL),
('R-TH', '天河区', '区县', 'R-GZ', NULL),
('R-NS', '南山区', '区县', 'R-SZ', NULL),
('R-GX', '高新区', '区县', 'R-CD', NULL),
('R-HS', '洪山区', '区县', 'R-WH', NULL);

INSERT INTO Huangxx_Major11 VALUES
('M-CS', '计算机科学与技术', '计算机学院', '软件与系统方向', current_timestamp),
('M-SE', '软件工程', '计算机学院', '工程实践方向', current_timestamp),
('M-DS', '数据科学与大数据技术', '计算机学院', '数据分析方向', current_timestamp);

INSERT INTO Huangxx_Class11 VALUES
('CL-CS2301', '计科2301', 'M-CS', 2023, 5, '演示班级'),
('CL-CS2302', '计科2302', 'M-CS', 2023, 5, '演示班级'),
('CL-SE2301', '软工2301', 'M-SE', 2023, 5, '演示班级'),
('CL-DS2301', '大数据2301', 'M-DS', 2023, 5, '演示班级');

INSERT INTO Huangxx_Student11
(hxx_student_id11, hxx_student_name11, hxx_gender11, hxx_age11, hxx_region_id11, hxx_class_id11, hxx_total_credit11, hxx_gpa11, hxx_phone11, hxx_status11, hxx_enroll_date11)
VALUES
('S2023001', '陈晨', '男', 20, 'R-XH', 'CL-CS2301', 0, 0, '13800010001', '在读', '2023-09-01'),
('S2023002', '李雨', '女', 19, 'R-BL', 'CL-CS2301', 0, 0, '13800010002', '在读', '2023-09-01'),
('S2023003', '王航', '男', 20, 'R-XW', 'CL-CS2301', 0, 0, '13800010003', '在读', '2023-09-01'),
('S2023004', '赵宁', '女', 19, 'R-SS', 'CL-CS2301', 0, 0, '13800010004', '在读', '2023-09-01'),
('S2023005', '周然', '男', 20, 'R-XH', 'CL-CS2301', 0, 0, '13800010005', '在读', '2023-09-01'),
('S2023006', '吴越', '女', 20, 'R-BL', 'CL-CS2302', 0, 0, '13800010006', '在读', '2023-09-01'),
('S2023007', '郑凯', '男', 19, 'R-XW', 'CL-CS2302', 0, 0, '13800010007', '在读', '2023-09-01'),
('S2023008', '孙悦', '女', 20, 'R-SS', 'CL-CS2302', 0, 0, '13800010008', '在读', '2023-09-01'),
('S2023009', '胡一鸣', '男', 19, 'R-XH', 'CL-CS2302', 0, 0, '13800010009', '在读', '2023-09-01'),
('S2023010', '林可', '女', 20, 'R-BL', 'CL-CS2302', 0, 0, '13800010010', '在读', '2023-09-01'),
('S2023011', '何青', '女', 19, 'R-XW', 'CL-SE2301', 0, 0, '13800010011', '在读', '2023-09-01'),
('S2023012', '高远', '男', 20, 'R-SS', 'CL-SE2301', 0, 0, '13800010012', '在读', '2023-09-01'),
('S2023013', '许诺', '女', 19, 'R-XH', 'CL-SE2301', 0, 0, '13800010013', '在读', '2023-09-01'),
('S2023014', '唐宁', '男', 20, 'R-BL', 'CL-SE2301', 0, 0, '13800010014', '在读', '2023-09-01'),
('S2023015', '沈星', '女', 19, 'R-XW', 'CL-SE2301', 0, 0, '13800010015', '在读', '2023-09-01'),
('S2023016', '曹原', '男', 20, 'R-SS', 'CL-DS2301', 0, 0, '13800010016', '在读', '2023-09-01'),
('S2023017', '邓琳', '女', 19, 'R-XH', 'CL-DS2301', 0, 0, '13800010017', '在读', '2023-09-01'),
('S2023018', '傅舟', '男', 20, 'R-BL', 'CL-DS2301', 0, 0, '13800010018', '在读', '2023-09-01'),
('S2023019', '叶澜', '女', 19, 'R-XW', 'CL-DS2301', 0, 0, '13800010019', '在读', '2023-09-01'),
('S2023020', '马骁', '男', 20, 'R-SS', 'CL-DS2301', 0, 0, '13800010020', '在读', '2023-09-01');

INSERT INTO Huangxx_Teacher11 VALUES
('T001', '张明', '男', 42, '教授', '13900020001', '计算机学院', '在职'),
('T002', '刘芳', '女', 38, '副教授', '13900020002', '计算机学院', '在职'),
('T003', '陈刚', '男', 35, '讲师', '13900020003', '计算机学院', '在职'),
('T004', '王敏', '女', 33, '讲师', '13900020004', '计算机学院', '在职'),
('T005', '赵磊', '男', 46, '教授', '13900020005', '计算机学院', '在职'),
('T006', '钱静', '女', 30, '助教', '13900020006', '计算机学院', '在职');

INSERT INTO Huangxx_Course11 VALUES
(1, '数据库系统', 3.0, 48, '考试', '必修', '关系数据库与 openGauss 实践'),
(2, 'Java Web 开发', 3.0, 48, '考试', '必修', 'Spring Boot 与 MVC 开发'),
(3, '数据结构', 4.0, 64, '考试', '必修', '线性表、树和图'),
(4, '操作系统', 3.5, 56, '考试', '必修', '进程、内存与文件系统'),
(5, '计算机网络', 3.0, 48, '考试', '必修', '网络协议基础'),
(6, '软件工程', 2.5, 40, '考查', '必修', '需求、设计与测试'),
(7, '大数据分析', 3.0, 48, '考试', '选修', '数据处理与分析'),
(8, '人工智能导论', 2.0, 32, '考查', '通识', 'AI 基础概念');

SELECT setval(pg_get_serial_sequence('huangxx_course11', 'hxx_course_id11'), 8);

INSERT INTO Huangxx_Term11 VALUES
('TERM-2024-1', '2024-2025', '第一学期', '2024-09-01', '2025-01-15', '启用'),
('TERM-2024-2', '2024-2025', '第二学期', '2025-02-20', '2025-07-01', '启用'),
('TERM-2025-3', '2025-2026', '短学期', '2026-06-20', '2026-07-20', '启用');

INSERT INTO Huangxx_TeachingTask11 VALUES
('TASK001', 1, 'T001', 'CL-CS2301', 'TERM-2025-3', '博易楼A101', 8, 5, '开课中', 'Y'),
('TASK002', 1, 'T002', 'CL-CS2302', 'TERM-2025-3', '博易楼A102', 8, 5, '开课中', 'Y'),
('TASK003', 2, 'T003', 'CL-SE2301', 'TERM-2025-3', '博易楼B201', 8, 5, '开课中', 'N'),
('TASK004', 7, 'T004', 'CL-DS2301', 'TERM-2025-3', '博易楼B202', 8, 5, '开课中', 'Y'),
('TASK005', 3, 'T005', 'CL-CS2301', 'TERM-2024-2', '广知楼C301', 8, 5, '已结束', 'Y'),
('TASK006', 4, 'T006', 'CL-CS2302', 'TERM-2024-2', '广知楼C302', 8, 5, '已结束', 'Y'),
('TASK007', 5, 'T001', 'CL-SE2301', 'TERM-2024-2', '广知楼D401', 8, 5, '已结束', 'Y'),
('TASK008', 6, 'T002', 'CL-DS2301', 'TERM-2024-2', '广知楼D402', 8, 5, '已结束', 'Y'),
('TASK009', 8, 'T003', 'CL-CS2301', 'TERM-2024-1', '仁和楼E101', 8, 5, '已结束', 'Y'),
('TASK010', 8, 'T004', 'CL-SE2301', 'TERM-2024-1', '仁和楼E102', 8, 5, '已结束', 'Y');

INSERT INTO Huangxx_CourseSelection11 VALUES
('SEL001', 'S2023001', 'TASK001', current_timestamp, '完成', NULL),
('SEL002', 'S2023002', 'TASK001', current_timestamp, '完成', NULL),
('SEL003', 'S2023003', 'TASK001', current_timestamp, '完成', NULL),
('SEL004', 'S2023004', 'TASK001', current_timestamp, '完成', NULL),
('SEL005', 'S2023005', 'TASK001', current_timestamp, '完成', NULL),
('SEL006', 'S2023006', 'TASK002', current_timestamp, '完成', NULL),
('SEL007', 'S2023007', 'TASK002', current_timestamp, '完成', NULL),
('SEL008', 'S2023008', 'TASK002', current_timestamp, '完成', NULL),
('SEL009', 'S2023009', 'TASK002', current_timestamp, '完成', NULL),
('SEL010', 'S2023010', 'TASK002', current_timestamp, '完成', NULL),
('SEL011', 'S2023011', 'TASK003', current_timestamp, '已选', NULL),
('SEL012', 'S2023012', 'TASK003', current_timestamp, '已选', NULL),
('SEL013', 'S2023013', 'TASK003', current_timestamp, '已选', NULL),
('SEL014', 'S2023014', 'TASK003', current_timestamp, '已选', NULL),
('SEL015', 'S2023015', 'TASK003', current_timestamp, '已选', NULL),
('SEL016', 'S2023016', 'TASK004', current_timestamp, '完成', NULL),
('SEL017', 'S2023017', 'TASK004', current_timestamp, '完成', NULL),
('SEL018', 'S2023018', 'TASK004', current_timestamp, '完成', NULL),
('SEL019', 'S2023019', 'TASK004', current_timestamp, '完成', NULL),
('SEL020', 'S2023020', 'TASK004', current_timestamp, '完成', NULL),
('SEL021', 'S2023001', 'TASK005', current_timestamp, '完成', NULL),
('SEL022', 'S2023002', 'TASK005', current_timestamp, '完成', NULL),
('SEL023', 'S2023003', 'TASK005', current_timestamp, '完成', NULL),
('SEL024', 'S2023004', 'TASK005', current_timestamp, '完成', NULL),
('SEL025', 'S2023005', 'TASK005', current_timestamp, '完成', NULL),
('SEL026', 'S2023006', 'TASK006', current_timestamp, '完成', NULL),
('SEL027', 'S2023007', 'TASK006', current_timestamp, '完成', NULL),
('SEL028', 'S2023008', 'TASK006', current_timestamp, '完成', NULL),
('SEL029', 'S2023009', 'TASK006', current_timestamp, '完成', NULL),
('SEL030', 'S2023010', 'TASK006', current_timestamp, '完成', NULL),
('SEL031', 'S2023011', 'TASK007', current_timestamp, '完成', NULL),
('SEL032', 'S2023012', 'TASK007', current_timestamp, '完成', NULL),
('SEL033', 'S2023013', 'TASK007', current_timestamp, '完成', NULL),
('SEL034', 'S2023014', 'TASK007', current_timestamp, '完成', NULL),
('SEL035', 'S2023015', 'TASK007', current_timestamp, '完成', NULL),
('SEL036', 'S2023016', 'TASK008', current_timestamp, '完成', NULL),
('SEL037', 'S2023017', 'TASK008', current_timestamp, '完成', NULL),
('SEL038', 'S2023018', 'TASK008', current_timestamp, '完成', NULL),
('SEL039', 'S2023019', 'TASK008', current_timestamp, '完成', NULL),
('SEL040', 'S2023020', 'TASK008', current_timestamp, '完成', NULL),
('SEL041', 'S2023001', 'TASK009', current_timestamp, '完成', NULL),
('SEL042', 'S2023002', 'TASK009', current_timestamp, '完成', NULL),
('SEL043', 'S2023003', 'TASK009', current_timestamp, '完成', NULL),
('SEL044', 'S2023004', 'TASK009', current_timestamp, '完成', NULL),
('SEL045', 'S2023005', 'TASK009', current_timestamp, '完成', NULL),
('SEL046', 'S2023011', 'TASK010', current_timestamp, '完成', NULL),
('SEL047', 'S2023012', 'TASK010', current_timestamp, '完成', NULL),
('SEL048', 'S2023013', 'TASK010', current_timestamp, '完成', NULL),
('SEL049', 'S2023014', 'TASK010', current_timestamp, '完成', NULL),
('SEL050', 'S2023015', 'TASK010', current_timestamp, '完成', NULL);

INSERT INTO Huangxx_Score11
(hxx_score_id11, hxx_selection_id11, hxx_usual_score11, hxx_exam_score11, hxx_final_score11, hxx_grade_level11, hxx_pass_flag11, hxx_grade_point11, hxx_exam_status11, hxx_score_source11, hxx_input_teacher11, hxx_input_time11, hxx_score_status11, hxx_publish_flag11, hxx_lock_flag11)
SELECT
    'SC' || substring(cs.hxx_selection_id11 from 4),
    cs.hxx_selection_id11,
    75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20),
    70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25),
    round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2),
    CASE
        WHEN round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2) >= 90 THEN '优秀'
        WHEN round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2) >= 80 THEN '良好'
        WHEN round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2) >= 70 THEN '中等'
        WHEN round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2) >= 60 THEN '及格'
        ELSE '不及格'
    END,
    'Y',
    CASE
        WHEN round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2) >= 90 THEN 4.00
        WHEN round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2) >= 80 THEN 3.00
        WHEN round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2) >= 70 THEN 2.00
        WHEN round(((75 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 20)) * 0.4 + (70 + (CAST(substring(cs.hxx_selection_id11 from 4) AS int) % 25)) * 0.6)::numeric, 2) >= 60 THEN 1.00
        ELSE 0.00
    END,
    '正常',
    '教师录入',
    tt.hxx_teacher_id11,
    current_timestamp,
    '正常',
    tt.hxx_score_publish_flag11,
    CASE WHEN tt.hxx_score_publish_flag11 = 'Y' THEN 'Y' ELSE 'N' END
FROM Huangxx_CourseSelection11 cs
JOIN Huangxx_TeachingTask11 tt ON tt.hxx_task_id11 = cs.hxx_task_id11
WHERE cs.hxx_selection_status11 = '完成';

UPDATE Huangxx_Student11 s
SET hxx_total_credit11 = COALESCE(x.total_credit, 0),
    hxx_gpa11 = COALESCE(x.gpa, 0)
FROM (
    SELECT cs.hxx_student_id11,
           sum(CASE WHEN sc.hxx_pass_flag11 = 'Y' AND sc.hxx_score_status11 <> '作废' THEN c.hxx_credit11 ELSE 0 END) AS total_credit,
           round(sum(sc.hxx_grade_point11 * c.hxx_credit11) / nullif(sum(c.hxx_credit11), 0), 2) AS gpa
    FROM Huangxx_Score11 sc
    JOIN Huangxx_CourseSelection11 cs ON cs.hxx_selection_id11 = sc.hxx_selection_id11
    JOIN Huangxx_TeachingTask11 tt ON tt.hxx_task_id11 = cs.hxx_task_id11
    JOIN Huangxx_Course11 c ON c.hxx_course_id11 = tt.hxx_course_id11
    WHERE sc.hxx_score_status11 <> '作废'
    GROUP BY cs.hxx_student_id11
) x
WHERE s.hxx_student_id11 = x.hxx_student_id11;

INSERT INTO Huangxx_ScoreAppeal11 VALUES
('AP001', 'SC001', 'S2023001', '希望复核期末卷面分。', '待处理', NULL, NULL, current_timestamp, NULL),
('AP002', 'SC006', 'S2023006', '平时成绩登记可能有误。', '已驳回', 'T002', '经核对成绩无误。', current_timestamp - interval '2 day', current_timestamp - interval '1 day'),
('AP003', 'SC016', 'S2023016', '项目成绩未计入。', '待处理', NULL, NULL, current_timestamp, NULL),
('AP004', 'SC031', 'S2023011', '申请查看评分细则。', '已通过', 'T001', '已重新核对并调整评分说明。', current_timestamp - interval '3 day', current_timestamp - interval '1 day'),
('AP005', 'SC041', 'S2023001', '考查课程成绩有疑问。', '待处理', NULL, NULL, current_timestamp, NULL);

INSERT INTO Huangxx_SystemUser11 VALUES
('U-ADMIN', 'admin', '123456', '管理员', NULL, '正常', current_timestamp, NULL),
('U-T001', 't001', '123456', '教师', 'T001', '正常', current_timestamp, NULL),
('U-T002', 't002', '123456', '教师', 'T002', '正常', current_timestamp, NULL),
('U-T003', 't003', '123456', '教师', 'T003', '正常', current_timestamp, NULL),
('U-S001', 's2023001', '123456', '学生', 'S2023001', '正常', current_timestamp, NULL),
('U-S006', 's2023006', '123456', '学生', 'S2023006', '正常', current_timestamp, NULL),
('U-S011', 's2023011', '123456', '学生', 'S2023011', '正常', current_timestamp, NULL),
('U-S016', 's2023016', '123456', '学生', 'S2023016', '正常', current_timestamp, NULL);

INSERT INTO Huangxx_OperationLog11 VALUES
('LOG001', 'U-ADMIN', 'admin', '管理员', '系统初始化', 'HuangxxMIS11', '插入课程设计演示数据', '成功', current_timestamp),
('LOG002', 'U-T001', 't001', '教师', '成绩发布', 'TASK001', '教师发布数据库系统成绩', '成功', current_timestamp),
('LOG003', 'U-T002', 't002', '教师', '申诉处理', 'AP002', '教师驳回成绩申诉', '成功', current_timestamp),
('LOG004', 'U-T001', 't001', '教师', '申诉处理', 'AP004', '教师通过成绩申诉', '成功', current_timestamp);
