package com.apiAuto.common.helpers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DbAssert {
    public static void assertCount(String sql,Object param, int expected){
        int count = ((Number) DbUtils.getValue(
                sql, param
        )).intValue();

        assertThat(count)
                .as("Количество строк по запросу = " + param)
                .isEqualTo(expected);
    }

}
