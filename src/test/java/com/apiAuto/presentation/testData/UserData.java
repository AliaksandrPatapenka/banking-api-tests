package com.apiAuto.presentation.testData;

import java.io.InputStream;
import java.util.Properties;

public final class UserData {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = UserData.class.getResourceAsStream("/local.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            // ignore
        }
    } // Загружает local.properties для локального запуска. В Jenkins переопределяется через -D.

    /**
     * Невалидные тестовые данные пользователя
     */
    public static final String LOGIN_NOT_EXIST = System.getProperty("user.loginNotExist", "loginNotExist");

}
