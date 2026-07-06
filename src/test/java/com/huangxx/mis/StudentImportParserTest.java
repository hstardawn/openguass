package com.huangxx.mis;

import com.huangxx.mis.service.StudentImportParser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentImportParserTest {

    @Test
    void parsesHeaderAndStudentRows() {
        String csv = """
                学号,姓名,性别,年龄,班级,生源地,手机号,学籍状态,入学日期
                S2023998,测试一,男,20,计科2301,西湖区,13899990001,在读,2023-09-01

                S2023999,测试二,女,19,软工2301,北仑区,13899990002,在读,2023-09-01
                """;

        var rows = StudentImportParser.parse(csv);

        assertThat(rows).hasSize(2);
        assertThat(rows.get(0))
                .containsEntry("studentId", "S2023998")
                .containsEntry("studentName", "测试一")
                .containsEntry("className", "计科2301");
        assertThat(rows.get(1))
                .containsEntry("gender", "女")
                .containsEntry("regionName", "北仑区")
                .doesNotContainKey("gpa")
                .doesNotContainKey("totalCredit");
    }

    @Test
    void rejectsRowsWithWrongColumnCount() {
        String csv = """
                学号,姓名,性别,年龄,班级,生源地,手机号,学籍状态,入学日期
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
