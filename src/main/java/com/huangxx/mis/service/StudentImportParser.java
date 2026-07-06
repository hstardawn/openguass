package com.huangxx.mis.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class StudentImportParser {

    private static final List<String> FIELDS = List.of(
            "studentId", "studentName", "gender", "age", "className",
            "regionName", "phone", "status", "enrollDate"
    );

    private StudentImportParser() {
    }

    public static List<Map<String, String>> parse(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("导入内容不能为空");
        }

        String[] lines = text.split("\\R");
        List<Map<String, String>> rows = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }
            if (isHeader(line)) {
                continue;
            }
            String[] values = line.split("\\s*,\\s*", -1);
            if (values.length != FIELDS.size()) {
                throw new IllegalArgumentException("第 " + (i + 1) + " 行字段数量应为 9，实际为 " + values.length);
            }
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < FIELDS.size(); j++) {
                row.put(FIELDS.get(j), values[j].trim());
            }
            rows.add(row);
        }

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("导入内容没有有效学生行");
        }
        return rows;
    }

    private static boolean isHeader(String line) {
        return line.toLowerCase().startsWith("studentid,")
                || line.startsWith("学号,");
    }
}
