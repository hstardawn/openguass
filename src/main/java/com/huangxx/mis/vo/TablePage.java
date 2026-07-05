package com.huangxx.mis.vo;

import java.util.List;
import java.util.Map;

public record TablePage(String title, List<Map<String, Object>> rows) {
}
