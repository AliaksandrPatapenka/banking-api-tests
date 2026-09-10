package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.helpers.testHelper.commonDataGenerator;
import com.apiAuto.presentation.helpers.testHelper.presentationDataGenerator;
import java.util.HashMap;
import java.util.Map;

public class UserJsonTemplate {
    public static Map<String, Object> userJsonTemplate() {
        String timeIndex = commonDataGenerator.timeIndex();

        Map<String, Object> userJsonTemplate = new HashMap<>();
        userJsonTemplate.put("login", commonDataGenerator.generatorString(timeIndex));
        userJsonTemplate.put("name", commonDataGenerator.generatorString(timeIndex));
        userJsonTemplate.put("age", presentationDataGenerator.randomAge());
        userJsonTemplate.put("gender", presentationDataGenerator.GenderGenerator.randomGender());
        userJsonTemplate.put("hairColor", presentationDataGenerator.HairColorGenerator.randomHairColor());
        //userJsonTemplate.put("friends", ); //TODO Прокинуть список друзей из соответствующего эндпоинта

        return userJsonTemplate;
    }
}
