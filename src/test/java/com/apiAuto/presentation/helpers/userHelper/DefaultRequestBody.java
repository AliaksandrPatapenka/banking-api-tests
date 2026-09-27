package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.CommonDataGenerator;
import com.apiAuto.presentation.dto.CreateUserDto;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;

import java.util.Collections;
import java.util.List;

/**
 * JSON-шаблон пользователя
 */
public class DefaultRequestBody {
    public static CreateUserDto defaultRequestBody() {
        return defaultRequestBody(Collections.emptyList());
    }

    public static CreateUserDto defaultRequestBody(List<String> friends) {
        String timeIndex = CommonDataGenerator.timeIndex();
        String userLogin = CommonDataGenerator.generatorString(timeIndex);
        String userName = CommonDataGenerator.generatorString(timeIndex);
        int userAge = PresentationDataGenerator.randomAge();
        String userGender = PresentationDataGenerator.randomGender();
        String userHairColor = PresentationDataGenerator.randomHairColor();

        CreateUserDto requestBody = new CreateUserDto();
        requestBody.setLogin(userLogin);
        requestBody.setName(userName);
        requestBody.setAge(userAge);
        requestBody.setGender(userGender);
        requestBody.setHairColor(userHairColor);
        requestBody.setFriends(friends);

        return requestBody;
    }
}
