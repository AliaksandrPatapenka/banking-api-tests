package com.apiAuto.common.helpers;

import com.apiAuto.common.base.config.CommonData;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Генерация текущих даты и времени
 */
public class CommonDataGenerator {
    public static String timeIndex() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
    }

    /**
     * Генератор случайной строки.<br>
     * Цифры, латиница верхний и нижний регистры.<br>
     * 6 знаков
     */
    public static String randomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }


    /**
     * Генератор name пользователя<br>
     * Формат Test_yyyy-MM-dd_HH-mm-ss<br>
     * Передаем текущее время из timeIndex и подставляем в user.email из config.properties
     */
    public static String generatorString(String timeIndex) {
        String template = CommonData.DEFAULT_STRING;
        return String.format(template, timeIndex);
    }


}






