package com.apiAuto.common.helpers;

import com.apiAuto.presentation.constants.testData.UserData;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesHelper {
    public static final Properties props = new Properties();

    static {
        try (InputStream in = UserData.class.getResourceAsStream("/local.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            // ignore
        }
    }
}
