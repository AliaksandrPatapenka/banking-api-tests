package com.apiAuto.presentation.helpers.testHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;
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
    public static class GenderGenerator {
        private static final String[] gender = {
                "MALE", "FEMALE"
        };
        private static final Random RANDOM = new Random();

        public static String randomGender() {
            return gender[RANDOM.nextInt(gender.length)];
        }
    }

    /**
     * Выбор цвета волос
     */
    public static class HairColorGenerator {
        private static final String[] colors = {
                "White", "Black", "Red", "Yellow", "Orange",
                "Green", "Blue", "Purple", "Pink", "Brown", "Grey"
        };
        private static final Random RANDOM = new Random();

        public static String randomHairColor() {
            return colors[RANDOM.nextInt(colors.length)];
        }
    }

    /**
     * Генератор рандомного числа от 18 до 80
     */
    public static int randomAge() {
        return 18 + new SecureRandom().nextInt(63); // 63 = 80 - 18 + 1
    }

    /**
     * Генератор cуммы списания c баланса
     */
    public static BigDecimal debitAmount(BigDecimal balance) {
        int divisor = ThreadLocalRandom.current().nextInt(1, 101);   // 1..100
        return balance.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.DOWN);
    }

}
