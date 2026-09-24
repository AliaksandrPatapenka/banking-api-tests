package com.apiAuto.common.helpers;

import java.io.InputStream;
import java.util.Properties;


/**
 * Загрузчик properties-файлов из classpath по профилю (/config/{profile}/{service}.properties) и хранение их в общем поле props
 */

public class PropertiesHelper {
    public static final Properties props = new Properties();

    static {
        String service = System.getProperty("service", "presentation");
        String profile = System.getProperty("profile", "local");
        load("/config/" + profile + "/" + service + ".properties");
    }

    private static void load(String path) {
        try (InputStream in = PropertiesHelper.class.getResourceAsStream(path)) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            // ignore
        }
    }
}
