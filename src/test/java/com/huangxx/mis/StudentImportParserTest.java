package com.huangxx.mis;

import com.huangxx.mis.service.StudentImportParser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentImportParserTest {

    @Test
    void parsesHeaderAndStudentRows() {
        String csv = """
                studentId,studentName,gender,age,regionId,classId,phone,status,enrollDate
                S2023998,测试一,男,20,R-XH,CL-CS2301,13899990001,在读,2023-09-01

                S2023999,测试二,女,19,R-BL,CL-CS2302,13899990002,在读,2023-09-01
                """;

        var rows = StudentImportParser.parse(csv);

        assertThat(rows).hasSize(2);
        assertThat(rows.get(0))
                .containsEntry("studentId", "S2023998")
                .containsEntry("studentName", "测试一")
                .containsEntry("classId", "CL-CS2301");
        assertThat(rows.get(1))
                .containsEntry("gender", "女")
                .containsEntry("regionId", "R-BL");
    }

    @Test
    void rejectsRowsWithWrongColumnCount() {
        String csv = """
                studentId,studentName,gender,age,regionId,classId,phone,status,enrollDate
                S2023998,测试一,男
                """;

        assertThatThrownBy(() -> StudentImportParser.parse(csv))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("第 2 行字段数量应为 9");
    }

    @Test
    void rejectsEmptyImportText() {
        assertThatThrownBy(() -> StudentImportParser.parse("  \n  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("导入内容不能为空");
    }
}
