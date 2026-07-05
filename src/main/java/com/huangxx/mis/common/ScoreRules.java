package com.huangxx.mis.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ScoreRules {

    private ScoreRules() {
    }

    public static BigDecimal finalScore(double usualScore, double examScore) {
        return BigDecimal.valueOf(usualScore)
                .multiply(BigDecimal.valueOf(0.4))
                .add(BigDecimal.valueOf(examScore).multiply(BigDecimal.valueOf(0.6)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static String gradeLevel(double finalScore) {
        if (finalScore >= 90) {
            return "优秀";
        }
        if (finalScore >= 80) {
            return "良好";
        }
        if (finalScore >= 70) {
            return "中等";
        }
        if (finalScore >= 60) {
            return "及格";
        }
        return "不及格";
    }

    public static String passFlag(double finalScore) {
        return finalScore >= 60 ? "Y" : "N";
    }

    public static BigDecimal gradePoint(double finalScore) {
        if (finalScore >= 90) {
            return BigDecimal.valueOf(4).setScale(2);
        }
        if (finalScore >= 80) {
            return BigDecimal.valueOf(3).setScale(2);
        }
        if (finalScore >= 70) {
            return BigDecimal.valueOf(2).setScale(2);
        }
        if (finalScore >= 60) {
            return BigDecimal.valueOf(1).setScale(2);
        }
        return BigDecimal.ZERO.setScale(2);
    }
}
