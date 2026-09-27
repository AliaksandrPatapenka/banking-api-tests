package com.apiAuto.presentation.helpers.testHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Генератор тестовых данных сервиса presentation
 */
public class PresentationDataGenerator {

    public PresentationDataGenerator() {
    }

    /**
     * Выбор пола
     */
    private static final String[] gender = {
            "MALE", "FEMALE"
    };

    public static String randomGender() {
        return gender[ThreadLocalRandom.current().nextInt(gender.length)];
    }

    /**
     * Выбор цвета волос
     */
    private static final String[] colors = {
            "White", "Black", "Red", "Yellow", "Orange",
            "Green", "Blue", "Purple", "Pink", "Brown", "Grey"
    };

    public static String randomHairColor() {
        return colors[ThreadLocalRandom.current().nextInt(colors.length)];
    }


    /**
     * Генератор рандомного числа от 18 до 80
     */
    public static int randomAge() {
        return ThreadLocalRandom.current().nextInt(18, 81);
    }

    /**
     * Генератор cуммы списания c баланса
     */
    public static BigDecimal debitAmount(BigDecimal balance) {
        int divisor = ThreadLocalRandom.current().nextInt(1, 101);   // 1..100
        return balance.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.DOWN);
    }
}
