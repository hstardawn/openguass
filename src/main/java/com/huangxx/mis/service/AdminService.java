package com.huangxx.mis.service;

import com.huangxx.mis.repository.AdminRepository;
import com.huangxx.mis.view.TableColumn;
import com.huangxx.mis.view.TablePage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
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
            case "tasks" -> adminRepository.tasks(filters);
            case "stat-course" -> adminRepository.courseStats();
            case "stat-class-credit" -> adminRepository.classCredits();
            case "stat-region" -> adminRepository.regionStats();
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

    public List<TableColumn> rankColumns(String mode) {
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
            case "tasks" -> adminRepository.task(id);
            default -> Map.of();
        };
    }

    public Map<String, List<Map<String, Object>>> options() {
        return Map.of(
                "regions", adminRepository.options("Huangxx_Region11", "hxx_region_id11", "hxx_region_name11"),
                "classes", adminRepository.options("Huangxx_Class11", "hxx_class_id11", "hxx_class_name11"),
                "teachers", adminRepository.options("Huangxx_Teacher11", "hxx_teacher_id11", "hxx_teacher_name11"),
                "courses", adminRepository.options("Huangxx_Course11", "hxx_course_id11", "hxx_course_name11"),
                "majors", adminRepository.options("Huangxx_Major11", "hxx_major_id11", "hxx_major_name11"),
                "terms", adminRepository.options("Huangxx_Term11", "hxx_term_id11", "hxx_school_year11 || ' ' || hxx_semester11")
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
            case "tasks" -> List.of(
                    TableColumn.text("课程名称", "课程名称"),
                    TableColumn.text("任课教师", "任课教师"),
                    TableColumn.text("班级", "班级"),
                    TableColumn.text("学期", "学期"),
                    TableColumn.text("上课地点", "上课地点"),
                    TableColumn.number("最大人数", "最大人数"),
                    TableColumn.number("当前人数", "当前人数"),
                    TableColumn.status("成绩发布状态", "成绩发布状态")
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
        return List.of("students", "teachers", "courses", "majors", "classes", "tasks").contains(name);
    }

    private boolean deletable(String name) {
        return editable(name);
    }

    private String title(String key) {
        return switch (key) {
            case "students" -> "学生管理";
            case "teachers" -> "教师管理";
            case "courses" -> "课程管理";
            case "majors" -> "专业管理";
            case "classes" -> "班级管理";
            case "tasks" -> "教学任务管理";
            case "stat-course" -> "课程成绩统计";
            case "stat-class-credit" -> "班级学分统计";
            case "stat-region" -> "生源地统计";
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
            case "tasks" -> "安排课程、教师、班级和学期之间的教学任务。";
            case "audit-score" -> "展示成绩修改的业务上下文和修改轨迹，隐藏审计主键、成绩记录编号等技术字段。";
            case "logs" -> "展示系统业务操作记录，优先解析申诉、成绩、教学任务等业务对象名称。";
            case "stat-course" -> "按教学任务汇总课程成绩分布、通过率和优秀率。";
            case "stat-class-credit" -> "查看学生学分、GPA 和课程通过情况。";
            case "stat-region" -> "统计各生源地学生分布。";
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
        return adminRepository.importStudents(StudentImportParser.parse(importText));
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
        require(form.get("majorId"), "专业编号不能为空");
        require(form.get("majorName"), "专业名称不能为空");
        if (!edit && adminRepository.countWhere("Huangxx_Major11", "hxx_major_id11", form.get("majorId")) > 0) {
            throw new IllegalArgumentException("专业编号已存在");
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
        require(form.get("classId"), "班级号不能为空");
        require(form.get("className"), "班级名称不能为空");
        require(form.get("majorId"), "所属专业不能为空");
        validateIntRange(form.get("gradeYear"), 2000, 2100, "入学年份");
        validateIntRange(form.get("classSize"), 0, 500, "班级人数");
        if (!edit && adminRepository.countWhere("Huangxx_Class11", "hxx_class_id11", form.get("classId")) > 0) {
            throw new IllegalArgumentException("班级号已存在");
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
    public void saveTask(Map<String, String> form, boolean edit) {
        require(form.get("taskId"), "教学任务编号不能为空");
        require(form.get("courseId"), "课程不能为空");
        require(form.get("teacherId"), "教师不能为空");
        require(form.get("classId"), "班级不能为空");
        require(form.get("termId"), "学期不能为空");
        validateIntRange(form.get("maxCount"), 1, 500, "最大人数");
        if (!edit && adminRepository.countWhere("Huangxx_TeachingTask11", "hxx_task_id11", form.get("taskId")) > 0) {
            throw new IllegalArgumentException("教学任务编号已存在");
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
}
