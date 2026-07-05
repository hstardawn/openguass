package com.huangxx.mis;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class PrivacyAndRankingSqlTest {

    @Test
    void adminSensitivePagesUseBusinessQueriesInsteadOfRawInternalTables() throws IOException {
        String repository = Files.readString(Path.of("src/main/java/com/huangxx/mis/repository/AdminRepository.java"));

        assertThat(repository).doesNotContain("SELECT * FROM Huangxx_ScoreAudit11");
        assertThat(repository).doesNotContain("SELECT * FROM Huangxx_OperationLog11");
        assertThat(repository).contains("课程名称", "学生姓名", "操作人", "业务对象");
    }

    @Test
    void teacherAppealTableDoesNotRenderInternalAppealIdAsVisibleColumn() throws IOException {
        String template = Files.readString(Path.of("src/main/resources/templates/teacher/appeals.html"));

        assertThat(template).doesNotContain("<th>编号</th>");
        assertThat(template).doesNotContain("<td th:text=\"${r['hxx_appeal_id11']}\"></td>");
        assertThat(template).contains("type=\"hidden\" name=\"appealId\"");
    }

    @Test
    void visibleStudentAndTeacherTablesDoNotShowInternalIdsOrRawColumnNames() throws IOException {
        String taskStudents = Files.readString(Path.of("src/main/resources/templates/teacher/task-students.html"));
        String teacherTasks = Files.readString(Path.of("src/main/resources/templates/teacher/tasks.html"));
        String studentScores = Files.readString(Path.of("src/main/resources/templates/student/scores.html"));
        String studentProfile = Files.readString(Path.of("src/main/resources/templates/student/profile.html"));
        String layout = Files.readString(Path.of("src/main/resources/templates/fragments/layout.html"));

        assertThat(taskStudents).doesNotContain("<th>选课编号</th>", "r['成绩编号']", "r['选课编号']");
        assertThat(teacherTasks).doesNotContain("<th>任务</th>", "hxx_task_id11", "hxx_score_publish_flag11");
        assertThat(studentScores).doesNotContain("hxx_course_name11", "hxx_score_id11");
        assertThat(studentProfile).doesNotContain("profile.entrySet()");
        assertThat(layout).contains("!#strings.endsWith(entry.key, 'Id')");
    }

    @Test
    void scoreRankViewPartitionsByTeachingTaskClassAndMajorGradeScope() throws IOException {
        String views = Files.readString(Path.of("sql/05_views.sql"));

        assertThat(views).contains("PARTITION BY v.hxx_task_id11");
        assertThat(views).contains("PARTITION BY v.hxx_class_id11, v.hxx_school_year11, v.hxx_semester11");
        assertThat(views).contains("PARTITION BY v.hxx_major_id11, v.hxx_grade_year11, v.hxx_school_year11, v.hxx_semester11");
        assertThat(views).contains("hxx_rank_scope11");
    }

    @Test
    void rankPageProvidesClassDropdownAndMajorRankMode() throws IOException {
        String template = Files.readString(Path.of("src/main/resources/templates/admin/rank.html"));
        String repository = Files.readString(Path.of("src/main/java/com/huangxx/mis/repository/AdminRepository.java"));

        assertThat(template).contains("name=\"classId\"", "班级排名", "专业年级排名");
        assertThat(repository).contains("scoreRanksByClass", "scoreRanksByMajor");
    }
}
