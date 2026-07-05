package com.huangxx.mis.view;

public record TableColumn(
        String field,
        String label,
        String type,
        boolean visible,
        String width,
        String align
) {
    public static TableColumn text(String field, String label) {
        return new TableColumn(field, label, "text", true, "", "left");
    }

    public static TableColumn longText(String field, String label) {
        return new TableColumn(field, label, "longText", true, "", "left");
    }

    public static TableColumn number(String field, String label) {
        return new TableColumn(field, label, "number", true, "", "right");
    }

    public static TableColumn status(String field, String label) {
        return new TableColumn(field, label, "status", true, "", "center");
    }

    public static TableColumn dateTime(String field, String label) {
        return new TableColumn(field, label, "dateTime", true, "", "left");
    }
}
