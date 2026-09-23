package com.apiAuto.common.config;

public final class CommonData {
    private CommonData() {
    }

    public static final String DEFAULT_STRING = System.getProperty("default.string", "Test_%s");
}
