package com.huangxx.mis;

import com.huangxx.mis.common.ScoreRules;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreRulesTest {

    @Test
    void calculatesFinalScoreWithUsualFortyAndExamSixty() {
        assertThat(ScoreRules.finalScore(80, 90)).isEqualByComparingTo("86.00");
    }

    @Test
    void mapsGradeLevelAndPassFlag() {
        assertThat(ScoreRules.gradeLevel(95)).isEqualTo("优秀");
        assertThat(ScoreRules.gradeLevel(83)).isEqualTo("良好");
        assertThat(ScoreRules.gradeLevel(73)).isEqualTo("中等");
        assertThat(ScoreRules.gradeLevel(61)).isEqualTo("及格");
        assertThat(ScoreRules.gradeLevel(59)).isEqualTo("不及格");
        assertThat(ScoreRules.passFlag(60)).isEqualTo("Y");
        assertThat(ScoreRules.passFlag(59.99)).isEqualTo("N");
    }

    @Test
    void mapsGradePoint() {
        assertThat(ScoreRules.gradePoint(91)).isEqualByComparingTo("4.00");
        assertThat(ScoreRules.gradePoint(82)).isEqualByComparingTo("3.00");
        assertThat(ScoreRules.gradePoint(75)).isEqualByComparingTo("2.00");
        assertThat(ScoreRules.gradePoint(68)).isEqualByComparingTo("1.00");
        assertThat(ScoreRules.gradePoint(58)).isEqualByComparingTo("0.00");
    }
}
