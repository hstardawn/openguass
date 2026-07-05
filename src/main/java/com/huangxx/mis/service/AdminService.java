package com.huangxx.mis.service;

import com.huangxx.mis.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Map<String, Object>> table(String name) {
        return switch (name) {
            case "students" -> adminRepository.students();
            case "teachers" -> adminRepository.teachers();
            case "courses" -> adminRepository.courses();
            case "majors" -> adminRepository.majors();
            case "classes" -> adminRepository.classes();
            case "tasks" -> adminRepository.tasks();
            case "stat-course" -> adminRepository.courseStats();
            case "stat-class-credit" -> adminRepository.classCredits();
            case "stat-region" -> adminRepository.regionStats();
            case "audit-score" -> adminRepository.scoreAudits();
            case "logs" -> adminRepository.operationLogs();
            default -> throw new IllegalArgumentException("Unknown table page: " + name);
        };
    }

    public List<Map<String, Object>> classScoreRanks(String classId) {
        return adminRepository.scoreRanksByClass(classId);
    }

    public List<Map<String, Object>> majorScoreRanks(String majorId, Integer gradeYear) {
        return adminRepository.scoreRanksByMajor(majorId, gradeYear);
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
                "terms", adminRepository.options("Huangxx_Term11", "hxx_term_id11", "hxx_school_year11 || ' ' || hxx_semester11")
        );
    }

    @Transactional
    public void saveStudent(Map<String, String> form, boolean edit) {
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
        adminRepository.deleteStudent(id);
    }

    @Transactional
    public void saveTeacher(Map<String, String> form, boolean edit) {
        if (edit) {
            adminRepository.updateTeacher(form);
        } else {
            adminRepository.saveTeacher(form);
        }
    }

    @Transactional
    public void deleteTeacher(String id) {
        adminRepository.deleteTeacher(id);
    }

    @Transactional
    public void saveCourse(Map<String, String> form, boolean edit) {
        if (edit) {
            adminRepository.updateCourse(form);
        } else {
            adminRepository.saveCourse(form);
        }
    }

    @Transactional
    public void deleteCourse(String id) {
        adminRepository.deleteCourse(id);
    }

    @Transactional
    public void saveTask(Map<String, String> form, boolean edit) {
        if (edit) {
            adminRepository.updateTask(form);
        } else {
            adminRepository.saveTask(form);
        }
    }
}
