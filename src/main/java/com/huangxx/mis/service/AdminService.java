package com.huangxx.mis.service;

import com.huangxx.mis.repository.AdminRepository;
import com.huangxx.mis.view.TableColumn;
import com.huangxx.mis.view.TablePage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final ExcelService excelService;

    public AdminService(AdminRepository adminRepository, ExcelService excelService) {
        this.adminRepository = adminRepository;
        this.excelService = excelService;
    }

    public Map<String, Object> dashboard() {
        return adminRepository.dashboard();
    }

    public TablePage table(String name, Map<String, String> filters) {
        List<Map<String, Object>> rows = switch (name) {
            case "students" -> adminRepository.students(filters);
            case "teachers" -> adminRepository.teachers(filters);
            case "courses" -> adminRepository.courses(filters);
            case "majors" -> adminRepository.majors(filters);
            case "classes" -> adminRepository.classes(filters);
            case "regions" -> adminRepository.regions(filters);
            case "terms" -> adminRepository.terms(filters);
            case "tasks" -> adminRepository.tasks(filters);
            case "users" -> adminRepository.users(filters);
            case "stat-course" -> adminRepository.courseStats();
            case "stat-class-credit" -> adminRepository.classCredits();
            case "stat-region" -> adminRepository.regionStats(filters);
            case "stat-score-distribution" -> adminRepository.scoreDistribution();
            case "stat-warning" -> adminRepository.academicWarnings();
            case "audit-score" -> adminRepository.scoreAudits();
            case "logs" -> adminRepository.operationLogs();
            default -> throw new IllegalArgumentException("Unknown table page: " + name);
        };
        return new TablePage(name, title(name), description(name), columns(name), rows, editable(name), deletable(name));
    }

    public List<Map<String, Object>> classScoreRanks(String classId) {
        return adminRepository.scoreRanksByClass(classId);
    }

    public List<Map<String, Object>> majorScoreRanks(String majorId, Integer gradeYear) {
        return adminRepository.scoreRanksByMajor(majorId, gradeYear);
    }

    public List<Map<String, Object>> scoreRanks(Map<String, String> filters) {
        return adminRepository.scoreRanks(filters);
    }

    public List<TableColumn> rankColumns(String mode) {
        if ("gpa".equals(mode)) {
            return List.of(
                    TableColumn.text("学生姓名", "学生姓名"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.text("学期范围", "学期范围"),
                    TableColumn.number("课程数", "课程数"),
                    TableColumn.number("GPA", "GPA"),
                    TableColumn.number("班级GPA排名", "班级GPA排名")
            );
        }
        if ("student".equals(mode)) {
            return List.of(
                    TableColumn.text("学生姓名", "学生姓名"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.text("学年", "学年"),
                    TableColumn.text("学期", "学期"),
                    TableColumn.number("课程数", "课程数"),
                    TableColumn.number("平均成绩", "平均成绩"),
                    TableColumn.number("班级平均分排名", "班级平均分排名")
            );
        }
        if ("major".equals(mode)) {
            return List.of(
                    TableColumn.text("专业", "专业"),
                    TableColumn.number("年级", "年级"),
                    TableColumn.text("学年", "学年"),
                    TableColumn.text("学期", "学期"),
                    TableColumn.text("学生姓名", "学生姓名"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.text("课程名称", "课程名称"),
                    TableColumn.number("总评成绩", "总评成绩"),
                    TableColumn.status("等级", "等级"),
                    TableColumn.number("专业年级排名", "专业年级排名")
            );
        }
        return List.of(
                TableColumn.text("排名范围", "排名范围"),
                TableColumn.text("学生姓名", "学生姓名"),
                TableColumn.text("班级", "班级"),
                TableColumn.text("课程名称", "课程名称"),
                TableColumn.text("任课教师", "任课教师"),
                TableColumn.text("学年", "学年"),
                TableColumn.text("学期", "学期"),
                TableColumn.number("总评成绩", "总评成绩"),
                TableColumn.status("等级", "等级"),
                TableColumn.number("教学班排名", "教学班排名"),
                TableColumn.number("班级学期排名", "班级学期排名")
        );
    }

    public List<Map<String, Object>> classOptions() {
        return adminRepository.classOptions();
    }

    public List<Map<String, Object>> majorOptions() {
        return adminRepository.majorOptions();
    }

    public List<Integer> gradeYears() {
        return adminRepository.gradeYears();
    }

    public Map<String, Object> editData(String type, String id) {
        return switch (type) {
            case "students" -> adminRepository.student(id);
            case "teachers" -> adminRepository.teacher(id);
            case "courses" -> adminRepository.course(id);
            case "majors" -> adminRepository.major(id);
            case "classes" -> adminRepository.classInfo(id);
            case "regions" -> adminRepository.region(id);
            case "tasks" -> adminRepository.task(id);
            case "terms" -> adminRepository.term(id);
            case "users" -> adminRepository.user(id);
            default -> Map.of();
        };
    }

    public Map<String, List<Map<String, Object>>> options() {
        return Map.of(
                "regions", adminRepository.regionOptionsForForms(),
                "classes", adminRepository.classOptionsForForms(),
                "teachers", adminRepository.options("Huangxx_Teacher11", "hxx_teacher_id11", "hxx_teacher_name11"),
                "courses", adminRepository.options("Huangxx_Course11", "hxx_course_id11", "hxx_course_name11"),
                "majors", adminRepository.options("Huangxx_Major11", "hxx_major_id11", "hxx_major_name11"),
                "terms", adminRepository.options("Huangxx_Term11", "hxx_term_id11", "hxx_school_year11 || ' ' || hxx_semester11"),
                "tasks", adminRepository.taskOptions()
        );
    }

    private List<TableColumn> columns(String name) {
        return switch (name) {
            case "students" -> List.of(
                    TableColumn.text("学号", "学号"),
                    TableColumn.text("姓名", "姓名"),
                    TableColumn.status("性别", "性别"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.text("生源地", "生源地"),
                    TableColumn.number("已修学分", "已修学分"),
                    TableColumn.number("GPA", "GPA"),
                    TableColumn.status("学籍状态", "学籍状态")
            );
            case "teachers" -> List.of(
                    TableColumn.text("姓名", "姓名"),
                    TableColumn.status("性别", "性别"),
                    TableColumn.status("职称", "职称"),
                    TableColumn.text("联系电话", "联系电话"),
                    TableColumn.text("所属学院", "所属学院"),
                    TableColumn.status("管理员权限", "管理员权限"),
                    TableColumn.status("状态", "状态")
            );
            case "courses" -> List.of(
                    TableColumn.text("课程名称", "课程名称"),
                    TableColumn.number("学分", "学分"),
                    TableColumn.number("学时", "学时"),
                    TableColumn.status("考核方式", "考核方式"),
                    TableColumn.status("课程类型", "课程类型")
            );
            case "majors" -> List.of(
                    TableColumn.text("专业名称", "专业名称"),
                    TableColumn.text("所属学院", "所属学院"),
                    TableColumn.longText("专业说明", "专业说明")
            );
            case "classes" -> List.of(
                    TableColumn.text("班级名称", "班级名称"),
                    TableColumn.text("专业", "专业"),
                    TableColumn.number("入学年份", "入学年份"),
                    TableColumn.number("班级人数", "班级人数")
            );
            case "regions" -> List.of(
                    TableColumn.text("地区名称", "地区名称"),
                    TableColumn.status("地区层级", "地区层级"),
                    TableColumn.text("上级地区", "上级地区"),
                    TableColumn.longText("备注", "备注")
            );
            case "tasks" -> List.of(
                    TableColumn.text("课程名称", "课程名称"),
                    TableColumn.text("任课教师", "任课教师"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.text("学期", "学期"),
                    TableColumn.text("上课地点", "上课地点"),
                    TableColumn.number("最大人数", "最大人数"),
                    TableColumn.number("当前人数", "当前人数"),
                    TableColumn.status("任务状态", "任务状态"),
                    TableColumn.status("成绩发布状态", "成绩发布状态")
            );
            case "terms" -> List.of(
                    TableColumn.text("学年", "学年"),
                    TableColumn.status("学期", "学期"),
                    TableColumn.dateTime("开始日期", "开始日期"),
                    TableColumn.dateTime("结束日期", "结束日期"),
                    TableColumn.status("状态", "状态")
            );
            case "users" -> List.of(
                    TableColumn.text("登录名", "登录名"),
                    TableColumn.status("角色", "角色"),
                    TableColumn.text("关联人员", "关联人员"),
                    TableColumn.status("状态", "状态"),
                    TableColumn.dateTime("创建时间", "创建时间"),
                    TableColumn.dateTime("最后登录时间", "最后登录时间")
            );
            case "stat-course" -> List.of(
                    TableColumn.text("课程名称", "课程名称"),
                    TableColumn.text("任课教师", "任课教师"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.number("成绩人数", "成绩人数"),
                    TableColumn.number("平均分", "平均分"),
                    TableColumn.number("最高分", "最高分"),
                    TableColumn.number("最低分", "最低分"),
                    TableColumn.number("通过率", "通过率"),
                    TableColumn.number("优秀率", "优秀率"),
                    TableColumn.number("不及格人数", "不及格人数")
            );
            case "stat-class-credit" -> List.of(
                    TableColumn.text("学生姓名", "学生姓名"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.number("已修学分", "已修学分"),
                    TableColumn.number("GPA", "GPA"),
                    TableColumn.number("通过课程数", "通过课程数"),
                    TableColumn.number("未通过课程数", "未通过课程数")
            );
            case "stat-region" -> List.of(
                    TableColumn.text("生源地", "生源地"),
                    TableColumn.status("地区层级", "地区层级"),
                    TableColumn.text("上级地区", "上级地区"),
                    TableColumn.number("学生人数", "学生人数")
            );
            case "stat-score-distribution" -> List.of(
                    TableColumn.text("课程名称", "课程名称"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.text("学期", "学期"),
                    TableColumn.number("优秀", "优秀"),
                    TableColumn.number("良好", "良好"),
                    TableColumn.number("中等", "中等"),
                    TableColumn.number("及格", "及格"),
                    TableColumn.number("不及格", "不及格")
            );
            case "stat-warning" -> List.of(
                    TableColumn.text("学生姓名", "学生姓名"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.number("已修学分", "已修学分"),
                    TableColumn.number("GPA", "GPA"),
                    TableColumn.number("未通过课程数", "未通过课程数"),
                    TableColumn.status("预警等级", "预警等级")
            );
            case "audit-score" -> List.of(
                    TableColumn.text("课程名称", "课程名称"),
                    TableColumn.text("学生姓名", "学生姓名"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.number("修改前平时成绩", "修改前平时成绩"),
                    TableColumn.number("修改后平时成绩", "修改后平时成绩"),
                    TableColumn.number("修改前考试成绩", "修改前考试成绩"),
                    TableColumn.number("修改后考试成绩", "修改后考试成绩"),
                    TableColumn.number("修改前总评成绩", "修改前总评成绩"),
                    TableColumn.number("修改后总评成绩", "修改后总评成绩"),
                    TableColumn.text("操作人", "操作人"),
                    TableColumn.status("角色", "角色"),
                    TableColumn.longText("修改原因", "修改原因"),
                    TableColumn.dateTime("修改时间", "修改时间")
            );
            case "logs" -> List.of(
                    TableColumn.text("操作人", "操作人"),
                    TableColumn.status("角色", "角色"),
                    TableColumn.status("操作类型", "操作类型"),
                    TableColumn.longText("业务对象", "业务对象"),
                    TableColumn.longText("业务描述", "业务描述"),
                    TableColumn.status("操作结果", "操作结果"),
                    TableColumn.dateTime("操作时间", "操作时间")
            );
            default -> new ArrayList<>();
        };
    }

    private boolean editable(String name) {
        return List.of("students", "teachers", "courses", "majors", "classes", "regions", "terms", "tasks", "users").contains(name);
    }

    private boolean deletable(String name) {
        return editable(name) && !List.of("students", "users", "regions").contains(name);
    }

    private String title(String key) {
        return switch (key) {
            case "students" -> "学生管理";
            case "teachers" -> "教师管理";
            case "courses" -> "课程管理";
            case "majors" -> "专业管理";
            case "classes" -> "班级管理";
            case "regions" -> "地区管理";
            case "terms" -> "学期管理";
            case "tasks" -> "教学任务管理";
            case "users" -> "系统用户管理";
            case "stat-course" -> "课程成绩统计";
            case "stat-class-credit" -> "班级学分统计";
            case "stat-region" -> "生源地统计";
            case "stat-score-distribution" -> "成绩区间分布";
            case "stat-warning" -> "学业预警统计";
            case "audit-score" -> "成绩审计日志";
            case "logs" -> "操作日志";
            default -> "管理页面";
        };
    }

    private String description(String key) {
        return switch (key) {
            case "students" -> "维护学生基本信息、班级、生源地和学籍状态。";
            case "teachers" -> "维护教师任职信息、联系方式和账号权限。";
            case "courses" -> "维护课程名称、学分、学时和考核方式。";
            case "majors" -> "维护学院专业信息和班级归属基础数据。";
            case "classes" -> "维护行政班级、所属专业和入学年份。";
            case "regions" -> "维护省、市、区县三级生源地字典，编号由系统自动生成。";
            case "terms" -> "维护学年、学期起止日期和启用状态。";
            case "tasks" -> "安排课程、教师、班级和学期之间的教学任务。";
            case "users" -> "维护登录账号、角色、状态，并提供管理员重置密码能力。";
            case "audit-score" -> "展示成绩修改的业务上下文和修改轨迹，隐藏审计主键、成绩记录编号等技术字段。";
            case "logs" -> "展示系统业务操作记录，优先解析申诉、成绩、教学任务等业务对象名称。";
            case "stat-course" -> "按教学任务汇总课程成绩分布、通过率和优秀率。";
            case "stat-class-credit" -> "查看学生学分、GPA 和课程通过情况。";
            case "stat-region" -> "统计各生源地学生分布。";
            case "stat-score-distribution" -> "按课程、班级和学期统计成绩区间分布。";
            case "stat-warning" -> "按 GPA 和未通过课程数识别学业风险学生。";
            default -> "维护教务基础数据和业务信息。";
        };
    }

    @Transactional
    public void saveStudent(Map<String, String> form, boolean edit) {
        require(form.get("studentId"), "学号不能为空");
        require(form.get("studentName"), "学生姓名不能为空");
        validateGender(form.get("gender"));
        validateIntRange(form.get("age"), 15, 35, "学生年龄");
        validateDecimalRange(form.get("totalCredit"), "0", "999", "已修学分");
        validateDecimalRange(form.get("gpa"), "0", "5", "当前绩点");
        require(form.get("regionId"), "生源地不能为空");
        require(form.get("classId"), "班级不能为空");
        if (!edit && adminRepository.countWhere("Huangxx_Student11", "hxx_student_id11", form.get("studentId")) > 0) {
            throw new IllegalArgumentException("学号已存在");
        }
        if (adminRepository.countWhere("Huangxx_Region11", "hxx_region_id11", form.get("regionId")) == 0) {
            throw new IllegalArgumentException("选择的生源地不存在");
        }
        if (adminRepository.countWhere("Huangxx_Class11", "hxx_class_id11", form.get("classId")) == 0) {
            throw new IllegalArgumentException("选择的班级不存在");
        }
        if (edit) {
            adminRepository.updateStudent(form);
        } else {
            adminRepository.saveStudent(form);
        }
    }

    @Transactional
    public int importStudents(String importText) {
        return adminRepository.importStudents(prepareImportedStudents(StudentImportParser.parse(importText)));
    }

    @Transactional
    public int importStudents(MultipartFile file, String importText) {
        if (file != null && !file.isEmpty()) {
            return adminRepository.importStudents(prepareImportedStudents(excelService.parseStudents(file)));
        }
        return importStudents(importText);
    }

    private List<Map<String, String>> prepareImportedStudents(List<Map<String, String>> rows) {
        List<Map<String, String>> prepared = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> row = new LinkedHashMap<>(rows.get(i));
            int lineNo = i + 1;
            require(row.get("studentId"), "第 " + lineNo + " 行学号不能为空");
            require(row.get("studentName"), "第 " + lineNo + " 行姓名不能为空");
            validateGender(row.get("gender"));
            validateIntRange(row.get("age"), 15, 35, "第 " + lineNo + " 行年龄");
            require(row.get("className"), "第 " + lineNo + " 行班级不能为空");
            require(row.get("regionName"), "第 " + lineNo + " 行生源地不能为空");
            if (adminRepository.countWhere("Huangxx_Student11", "hxx_student_id11", row.get("studentId")) > 0) {
                throw new IllegalArgumentException("第 " + lineNo + " 行学号已存在：" + row.get("studentId"));
            }
            row.put("classId", resolveSingle("班级", row.get("className"), lineNo, adminRepository.classIdsByNameOrId(row.get("className"))));
            row.put("regionId", resolveSingle("生源地", row.get("regionName"), lineNo, adminRepository.regionIdsByNameOrId(row.get("regionName"))));
            row.put("totalCredit", "0");
            row.put("gpa", "0");
            if (isBlank(row.get("status"))) {
                row.put("status", "在读");
            }
            prepared.add(row);
        }
        return prepared;
    }

    private String resolveSingle(String label, String value, int lineNo, List<String> ids) {
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("第 " + lineNo + " 行" + label + "不存在：" + value);
        }
        if (ids.size() > 1) {
            throw new IllegalArgumentException("第 " + lineNo + " 行" + label + "名称不唯一，请改用更明确的名称或编号：" + value);
        }
        return ids.get(0);
    }

    public byte[] exportCourseStatsExcel() {
        List<String> headers = List.of("课程名称", "任课教师", "班级", "成绩人数", "平均分", "最高分", "最低分", "通过率", "优秀率", "不及格人数");
        return excelService.exportRows("课程成绩统计", headers, adminRepository.courseStats());
    }

    @Transactional
    public void deleteStudent(String id) {
        if (adminRepository.countWhere("Huangxx_CourseSelection11", "hxx_student_id11", id) > 0) {
            throw new IllegalArgumentException("该学生已有选课记录，不能删除");
        }
        if (adminRepository.countWhere("Huangxx_ScoreAppeal11", "hxx_student_id11", id) > 0) {
            throw new IllegalArgumentException("该学生已有成绩申诉记录，不能删除");
        }
        adminRepository.deleteStudent(id);
    }

    @Transactional
    public void saveTeacher(Map<String, String> form, boolean edit) {
        require(form.get("teacherId"), "教师号不能为空");
        require(form.get("teacherName"), "教师姓名不能为空");
        validateGender(form.get("gender"));
        validateIntRange(form.get("age"), 22, 70, "教师年龄");
        require(form.get("title"), "职称不能为空");
        require(form.get("collegeName"), "学院不能为空");
        validatePhone(form.get("phone"));
        if (!edit && adminRepository.countWhere("Huangxx_Teacher11", "hxx_teacher_id11", form.get("teacherId")) > 0) {
            throw new IllegalArgumentException("教师号已存在");
        }
        if (edit) {
            adminRepository.updateTeacher(form);
        } else {
            adminRepository.saveTeacher(form);
        }
    }

    @Transactional
    public void deleteTeacher(String id) {
        if (adminRepository.countWhere("Huangxx_TeachingTask11", "hxx_teacher_id11", id) > 0) {
            throw new IllegalArgumentException("该教师已有任课记录，不能删除");
        }
        adminRepository.deleteTeacher(id);
    }

    @Transactional
    public void saveCourse(Map<String, String> form, boolean edit) {
        require(form.get("courseName"), "课程名称不能为空");
        validateDecimalRange(form.get("credit"), "0.1", "99", "课程学分");
        validateIntRange(form.get("period"), 1, 300, "课程学时");
        require(form.get("examType"), "考核方式不能为空");
        require(form.get("courseType"), "课程类型不能为空");
        if (edit) {
            require(form.get("courseId"), "课程编号不能为空");
        }
        if (edit) {
            adminRepository.updateCourse(form);
        } else {
            adminRepository.saveCourse(form);
        }
    }

    @Transactional
    public void deleteCourse(String id) {
        if (adminRepository.countWhere("Huangxx_TeachingTask11", "hxx_course_id11", id) > 0) {
            throw new IllegalArgumentException("该课程已有教学任务，不能删除");
        }
        adminRepository.deleteCourse(id);
    }

    @Transactional
    public void saveMajor(Map<String, String> form, boolean edit) {
        require(form.get("majorName"), "专业名称不能为空");
        if (!edit && isBlank(form.get("majorId"))) {
            form.put("majorId", nextGeneratedId("M-AUTO-", adminRepository.majorIdsByPrefix("M-AUTO-")));
        }
        if (edit) {
            require(form.get("majorId"), "专业编号不能为空");
        } else if (adminRepository.countWhere("Huangxx_Major11", "hxx_major_id11", form.get("majorId")) > 0) {
            throw new IllegalArgumentException("系统生成的专业编号已存在，请重试");
        }
        if (adminRepository.countMajorName(form.get("majorName"), edit ? form.get("majorId") : null) > 0) {
            throw new IllegalArgumentException("专业名称已存在");
        }
        if (edit) {
            adminRepository.updateMajor(form);
        } else {
            adminRepository.saveMajor(form);
        }
    }

    @Transactional
    public void deleteMajor(String id) {
        if (adminRepository.countWhere("Huangxx_Class11", "hxx_major_id11", id) > 0) {
            throw new IllegalArgumentException("该专业下存在班级，不能删除");
        }
        adminRepository.deleteMajor(id);
    }

    @Transactional
    public void saveClass(Map<String, String> form, boolean edit) {
        require(form.get("className"), "班级名称不能为空");
        require(form.get("majorId"), "所属专业不能为空");
        validateIntRange(form.get("gradeYear"), 2000, 2100, "入学年份");
        validateIntRange(form.get("classSize"), 0, 500, "班级人数");
        if (!edit && isBlank(form.get("classId"))) {
            form.put("classId", nextGeneratedId("CL-AUTO-", adminRepository.classIdsByPrefix("CL-AUTO-")));
        }
        if (edit) {
            require(form.get("classId"), "班级号不能为空");
        } else if (adminRepository.countWhere("Huangxx_Class11", "hxx_class_id11", form.get("classId")) > 0) {
            throw new IllegalArgumentException("系统生成的班级号已存在，请重试");
        }
        if (adminRepository.countWhere("Huangxx_Major11", "hxx_major_id11", form.get("majorId")) == 0) {
            throw new IllegalArgumentException("选择的专业不存在");
        }
        if (edit) {
            adminRepository.updateClass(form);
        } else {
            adminRepository.saveClass(form);
        }
    }

    @Transactional
    public void deleteClass(String id) {
        if (adminRepository.countWhere("Huangxx_Student11", "hxx_class_id11", id) > 0) {
            throw new IllegalArgumentException("该班级下已有学生，不能删除");
        }
        if (adminRepository.countWhere("Huangxx_TeachingTask11", "hxx_class_id11", id) > 0) {
            throw new IllegalArgumentException("该班级已有教学任务，不能删除");
        }
        adminRepository.deleteClass(id);
    }

    @Transactional
    public void saveRegion(Map<String, String> form, boolean edit) {
        require(form.get("regionName"), "地区名称不能为空");
        require(form.get("regionLevel"), "地区层级不能为空");
        if (!List.of("省", "市", "区县").contains(form.get("regionLevel"))) {
            throw new IllegalArgumentException("地区层级只能选择省、市或区县");
        }
        if ("省".equals(form.get("regionLevel"))) {
            form.put("parentRegionId", "");
        } else {
            require(form.get("parentRegionId"), "上级地区不能为空");
            String expectedParentLevel = "市".equals(form.get("regionLevel")) ? "省" : "市";
            String actualParentLevel = adminRepository.regionLevel(form.get("parentRegionId"));
            if (actualParentLevel == null) {
                throw new IllegalArgumentException("选择的上级地区不存在");
            }
            if (!expectedParentLevel.equals(actualParentLevel)) {
                throw new IllegalArgumentException(form.get("regionLevel") + "的上级地区必须是" + expectedParentLevel);
            }
        }
        if (!edit && isBlank(form.get("regionId"))) {
            form.put("regionId", nextGeneratedId(regionPrefix(form.get("regionLevel")), adminRepository.regionIdsByPrefix(regionPrefix(form.get("regionLevel")))));
        }
        if (edit) {
            require(form.get("regionId"), "地区编号不能为空");
            if (!isBlank(form.get("parentRegionId")) && form.get("regionId").equals(form.get("parentRegionId"))) {
                throw new IllegalArgumentException("上级地区不能选择自己");
            }
            if (adminRepository.regionHasChildren(form.get("regionId")) && "区县".equals(form.get("regionLevel"))) {
                throw new IllegalArgumentException("该地区下已有子地区，不能改为区县");
            }
        } else if (adminRepository.countWhere("Huangxx_Region11", "hxx_region_id11", form.get("regionId")) > 0) {
            throw new IllegalArgumentException("系统生成的地区编号已存在，请重试");
        }
        if (adminRepository.countRegionDuplicateName(form.get("regionName"), blankToNull(form.get("parentRegionId")), edit ? form.get("regionId") : null) > 0) {
            throw new IllegalArgumentException("同一上级地区下已存在同名地区");
        }
        if (edit) {
            adminRepository.updateRegion(form);
        } else {
            adminRepository.saveRegion(form);
        }
    }

    @Transactional
    public void saveTerm(Map<String, String> form, boolean edit) {
        require(form.get("termId"), "学期编号不能为空");
        require(form.get("schoolYear"), "学年不能为空");
        require(form.get("semester"), "学期不能为空");
        require(form.get("startDate"), "开始日期不能为空");
        require(form.get("endDate"), "结束日期不能为空");
        require(form.get("termStatus"), "学期状态不能为空");
        if (!List.of("第一学期", "第二学期", "短学期").contains(form.get("semester"))) {
            throw new IllegalArgumentException("学期只能选择第一学期、第二学期或短学期");
        }
        if (!List.of("启用", "停用").contains(form.get("termStatus"))) {
            throw new IllegalArgumentException("学期状态只能选择启用或停用");
        }
        if (!edit && adminRepository.countWhere("Huangxx_Term11", "hxx_term_id11", form.get("termId")) > 0) {
            throw new IllegalArgumentException("学期编号已存在");
        }
        if (edit) {
            adminRepository.updateTerm(form);
        } else {
            adminRepository.saveTerm(form);
        }
    }

    @Transactional
    public void deleteTerm(String id) {
        if (adminRepository.countWhere("Huangxx_TeachingTask11", "hxx_term_id11", id) > 0) {
            throw new IllegalArgumentException("该学期已有教学任务，不能删除，可改为停用");
        }
        adminRepository.deleteTerm(id);
    }

    @Transactional
    public void saveTask(Map<String, String> form, boolean edit) {
        require(form.get("courseId"), "课程不能为空");
        require(form.get("teacherId"), "教师不能为空");
        require(form.get("classId"), "班级不能为空");
        require(form.get("termId"), "学期不能为空");
        validateIntRange(form.get("maxCount"), 1, 500, "最大人数");
        if (!edit && isBlank(form.get("taskId"))) {
            form.put("taskId", nextGeneratedId("TASK-AUTO-", adminRepository.taskIdsByPrefix("TASK-AUTO-")));
        }
        if (edit) {
            require(form.get("taskId"), "教学任务编号不能为空");
        } else if (adminRepository.countWhere("Huangxx_TeachingTask11", "hxx_task_id11", form.get("taskId")) > 0) {
            throw new IllegalArgumentException("系统生成的教学任务编号已存在，请重试");
        }
        if (adminRepository.countTaskDuplicate(form, edit) > 0) {
            throw new IllegalArgumentException("同一教师、课程、班级、学期的任课记录已存在");
        }
        if (edit) {
            adminRepository.updateTask(form);
        } else {
            adminRepository.saveTask(form);
        }
    }

    @Transactional
    public void deleteTask(String id) {
        if (adminRepository.countWhere("Huangxx_CourseSelection11", "hxx_task_id11", id) > 0) {
            throw new IllegalArgumentException("该教学任务已有选课记录，不能删除");
        }
        adminRepository.deleteTask(id);
    }

    @Transactional
    public void saveUser(Map<String, String> form, boolean edit) {
        require(form.get("userId"), "用户编号不能为空");
        require(form.get("loginName"), "登录名不能为空");
        require(form.get("role"), "角色不能为空");
        require(form.get("userStatus"), "账号状态不能为空");
        if (!List.of("管理员", "教师", "学生").contains(form.get("role"))) {
            throw new IllegalArgumentException("角色不正确");
        }
        if (!List.of("正常", "禁用").contains(form.get("userStatus"))) {
            throw new IllegalArgumentException("账号状态不正确");
        }
        if (!edit) {
            require(form.get("password"), "新增用户必须设置初始密码");
            if (adminRepository.countWhere("Huangxx_SystemUser11", "hxx_user_id11", form.get("userId")) > 0) {
                throw new IllegalArgumentException("用户编号已存在");
            }
            if (adminRepository.countWhere("Huangxx_SystemUser11", "hxx_login_name11", form.get("loginName")) > 0) {
                throw new IllegalArgumentException("登录名已存在");
            }
        }
        if (edit) {
            adminRepository.updateUser(form);
        } else {
            adminRepository.saveUser(form);
        }
    }

    @Transactional
    public void deleteUser(String id) {
        adminRepository.deleteUser(id);
    }

    @Transactional
    public void resetUserPassword(String id, String password) {
        require(id, "用户编号不能为空");
        if (password == null || password.isBlank() || password.length() < 6) {
            throw new IllegalArgumentException("重置密码至少 6 位");
        }
        adminRepository.resetUserPassword(id, password);
    }

    private void require(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateGender(String value) {
        if (!"男".equals(value) && !"女".equals(value)) {
            throw new IllegalArgumentException("性别只能选择男或女");
        }
    }

    private void validateIntRange(String value, int min, int max, String label) {
        try {
            int number = Integer.parseInt(value == null || value.isBlank() ? "0" : value);
            if (number < min || number > max) {
                throw new IllegalArgumentException(label + "必须在 " + min + " 到 " + max + " 之间");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + "必须是整数");
        }
    }

    private void validateDecimalRange(String value, String min, String max, String label) {
        try {
            java.math.BigDecimal number = new java.math.BigDecimal(value == null || value.isBlank() ? "0" : value);
            if (number.compareTo(new java.math.BigDecimal(min)) < 0 || number.compareTo(new java.math.BigDecimal(max)) > 0) {
                throw new IllegalArgumentException(label + "范围不正确");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + "必须是数字");
        }
    }

    private void validatePhone(String value) {
        if (value != null && !value.isBlank() && !value.matches("^[0-9+\\-]{6,20}$")) {
            throw new IllegalArgumentException("联系电话格式不正确");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String nextGeneratedId(String prefix, List<String> existingIds) {
        int max = 0;
        for (String id : existingIds) {
            if (id != null && id.startsWith(prefix)) {
                try {
                    max = Math.max(max, Integer.parseInt(id.substring(prefix.length())));
                } catch (NumberFormatException ignored) {
                    // Skip legacy or manually entered ids that do not follow the generated suffix.
                }
            }
        }
        return prefix + String.format("%03d", max + 1);
    }

    private String regionPrefix(String level) {
        return switch (level) {
            case "省" -> "REG-P-";
            case "市" -> "REG-C-";
            default -> "REG-D-";
        };
    }

    private String blankToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }
}
