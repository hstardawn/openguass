package com.huangxx.mis.dto;

import java.math.BigDecimal;

public record ScoreForm(String selectionId, String scoreId, BigDecimal usualScore, BigDecimal examScore, String reason) {
}
