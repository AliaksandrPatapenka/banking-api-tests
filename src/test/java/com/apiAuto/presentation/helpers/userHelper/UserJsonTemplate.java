package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.CommonDataGenerator;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserJsonTemplate {
    public static Map<String, Object> userJsonTemplate() {
        String timeIndex = CommonDataGenerator.timeIndex();

        Map<String, Object> userJsonTemplate = new HashMap<>();
        userJsonTemplate.put("login", CommonDataGenerator.generatorString(timeIndex));
        userJsonTemplate.put("name", CommonDataGenerator.generatorString(timeIndex));
        userJsonTemplate.put("age", PresentationDataGenerator.randomAge());
        userJsonTemplate.put("gender", PresentationDataGenerator.GenderGenerator.randomGender());
        userJsonTemplate.put("hairColor", PresentationDataGenerator.HairColorGenerator.randomHairColor());
        userJsonTemplate.put("friends", new ArrayList<String>());

        return userJsonTemplate;
    }
}
