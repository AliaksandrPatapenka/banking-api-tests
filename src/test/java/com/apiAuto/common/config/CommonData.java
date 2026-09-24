package com.apiAuto.common.config;

/**
 * Общие тестовые константы
 */

public final class CommonData {
    private CommonData() {
    }

    public static final String DEFAULT_STRING = System.getProperty("default.string", "Test_%s");
}
