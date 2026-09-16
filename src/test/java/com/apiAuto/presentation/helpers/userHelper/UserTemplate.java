package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.common.helpers.CommonDataGenerator;
import com.apiAuto.presentation.conctants.endpoints.UsersEndpoints;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import com.apiAuto.presentation.models.CreateUser;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;

public class UserTemplate {
    public static String userGetLogin(int httpStatus) {
        Map<String, Object> jsonRequest = UserJsonTemplate.userJsonTemplate();
        String userLogin = (String) jsonRequest.get("login");

        ApiSteps.postBody(requestSpec(),
                UsersEndpoints.ENDPOINT_USERS,
                jsonRequest,
                httpStatus);

        return userLogin;
    }

    public static Response createUser(CreateUser createUser,  int httpStatus) {
        return ApiSteps.postBody(requestSpec(),
                UsersEndpoints.ENDPOINT_USERS,
                createUser,
                httpStatus);
    }

    public static CreateUser defaultRequestBody() {
        return defaultRequestBody(Collections.emptyList());
    }

    public static CreateUser defaultRequestBody(List<String> friends) {
        String timeIndex = CommonDataGenerator.timeIndex();
        String userLogin = CommonDataGenerator.generatorString(timeIndex);
        String userName = CommonDataGenerator.generatorString(timeIndex);
        int userAge = PresentationDataGenerator.randomAge();
        String userGender = PresentationDataGenerator.GenderGenerator.randomGender();
        String userHairColor = PresentationDataGenerator.HairColorGenerator.randomHairColor();

        CreateUser createUser = new CreateUser();
        createUser.setLogin(userLogin);
        createUser.setName(userName);
        createUser.setAge(userAge);
        createUser.setGender(userGender);
        createUser.setHairColor(userHairColor);
        createUser.setFriends(friends);

        return createUser;
    }

}
