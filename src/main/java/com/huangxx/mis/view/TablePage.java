package com.huangxx.mis.view;

import java.util.List;
import java.util.Map;

public record TablePage(
        String key,
        String title,
        String description,
        List<TableColumn> columns,
        List<Map<String, Object>> rows,
        boolean editable,
        boolean deletable
) {
}
