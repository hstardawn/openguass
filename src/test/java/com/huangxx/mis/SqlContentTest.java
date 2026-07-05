package com.huangxx.mis;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SqlContentTest {

    private static final List<String> SQL_FILES = List.of(
            "sql/01_create_database.sql",
            "sql/02_create_tables.sql",
            "sql/03_insert_test_data.sql",
            "sql/04_indexes.sql",
            "sql/05_views.sql",
            "sql/06_triggers.sql",
            "sql/07_procedures.sql",
            "sql/08_test_and_verify.sql"
    );

    @Test
    void allRequiredSqlFilesExist() {
        for (String file : SQL_FILES) {
            assertThat(Path.of(file)).exists();
        }
    }

    @Test
    void sqlUsesPersonalizedGroupElevenNaming() throws IOException {
        String allSql = readAllSql();

        assertThat(allSql).contains("HuangxxMIS11");
        assertThat(allSql).contains("Huangxx_Student11");
        assertThat(allSql).contains("hxx_student_id11");
        assertThat(allSql).doesNotContain("HuangxxMIS01");
        assertThat(allSql).doesNotContain("Huangxx_Student01");
        assertThat(allSql).doesNotContain("hxx_student_id01");
    }

    @Test
    void sqlContainsRequiredTablesViewsTriggersAndProcedures() throws IOException {
        String allSql = readAllSql();

        assertThat(allSql).contains(
                "Huangxx_Region11",
                "Huangxx_Major11",
                "Huangxx_Class11",
                "Huangxx_Student11",
                "Huangxx_Teacher11",
                "Huangxx_Course11",
                "Huangxx_Term11",
                "Huangxx_TeachingTask11",
                "Huangxx_CourseSelection11",
                "Huangxx_Score11",
                "Huangxx_ScoreAudit11",
                "Huangxx_CreditLog11",
                "Huangxx_ScoreAppeal11",
                "Huangxx_SystemUser11",
                "Huangxx_OperationLog11",
                "Huangxx_ViewStudentScore11",
                "Huangxx_ViewTeacherTask11",
                "Huangxx_ViewClassCourse11",
                "Huangxx_ViewCourseStat11",
                "Huangxx_ViewScoreRank11",
                "Huangxx_ViewStudentGpaCredit11",
                "Huangxx_ViewRegionStudentStat11",
                "Huangxx_ViewScoreAppeal11",
                "Huangxx_TrigSetScoreComputed11",
                "Huangxx_TrigScoreAudit11",
                "Huangxx_TrigSelectionCount11",
                "Huangxx_ProcStudentTranscript11",
                "Huangxx_ProcCourseScoreStat11",
                "Huangxx_ProcClassCreditStat11",
                "Huangxx_ProcRegionStudentStat11",
                "Huangxx_ProcTeacherTaskStat11",
                "Huangxx_ProcStudentYearlyScoreStat11",
                "Huangxx_ProcMajorGpaStat11",
                "Huangxx_ProcAppealStatusStat11",
                "Huangxx_ProcOperationLogStat11"
        );
    }

    private String readAllSql() throws IOException {
        StringBuilder builder = new StringBuilder();
        for (String file : SQL_FILES) {
            builder.append(Files.readString(Path.of(file))).append('\n');
        }
        return builder.toString();
    }
}
