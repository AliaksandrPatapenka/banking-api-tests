package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.helpers.testHelper.CommonDataGenerator;
import com.apiAuto.presentation.helpers.testHelper.presentationDataGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserJsonTemplate {
    public static Map<String, Object> userJsonTemplate() {
        String timeIndex = CommonDataGenerator.timeIndex();

        Map<String, Object> userJsonTemplate = new HashMap<>();
        userJsonTemplate.put("login", CommonDataGenerator.generatorString(timeIndex));
        userJsonTemplate.put("name", CommonDataGenerator.generatorString(timeIndex));
        userJsonTemplate.put("age", presentationDataGenerator.randomAge());
        userJsonTemplate.put("gender", presentationDataGenerator.GenderGenerator.randomGender());
        userJsonTemplate.put("hairColor", presentationDataGenerator.HairColorGenerator.randomHairColor());
        userJsonTemplate.put("friends", new ArrayList<String>());

        return userJsonTemplate;
    }
}
