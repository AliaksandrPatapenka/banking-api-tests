package com.apiAuto.common.config;

import com.apiAuto.common.helpers.PropertiesHelper;

/**
 * Настройки подключения к PostgreSQL.
 *
 */
public final class DbConfig {
    private DbConfig() {

    }

    public static final String DB_URL = System.getProperty("db.url", "jdbc:postgresql://localhost:54321/postgres");
    public static final String DB_USER = System.getProperty("db.user", "postgres");
    public static final String DB_PASSWORD = System.getProperty("db.password", PropertiesHelper.props.getProperty("user.password"));
}
