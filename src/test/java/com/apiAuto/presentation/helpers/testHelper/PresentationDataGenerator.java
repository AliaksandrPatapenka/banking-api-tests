package com.apiAuto.presentation.helpers.testHelper;

import java.security.SecureRandom;
import java.util.Random;

public class PresentationDataGenerator {

    public static class GenderGenerator {
        private static final String[] gender = {
                "MALE", "FEMALE"
        };
        private static final Random RANDOM = new Random();

        public static String randomGender() {
            return gender[RANDOM.nextInt(gender.length)];
        }
    }

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

    public static int randomAge() {
        return 18 + new SecureRandom().nextInt(63); // 63 = 80 - 18 + 1
    }

}
