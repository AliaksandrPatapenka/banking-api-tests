package com.apiAuto.common.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Утилита для проверок количества строк в БД.
 */
public class DbAssert {
    public static void assertCount(String sql, Object param, int expected) {
        int count = ((Number) DbUtils.getValue(sql, param)).intValue();

        assertEquals(expected, count, "Количество строк по запросу = " + param);
    }

}
