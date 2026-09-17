package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.CommonDataGenerator;
import com.apiAuto.common.helpers.RequestTemplate;
import com.apiAuto.presentation.conctants.endpoints.UsersEndpoints;
import com.apiAuto.presentation.dto.CreateUserDto;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;

public class UserTemplate {
    public static String userGetLogin(int httpStatus) {
        Map<String, Object> jsonRequest = UserJsonTemplate.userJsonTemplate();
        String userLogin = (String) jsonRequest.get("login");

        RequestTemplate.postBody(requestSpec(),
                UsersEndpoints.ENDPOINT_USERS,
                jsonRequest,
                httpStatus);

        return userLogin;
    }

    public static Response createUser(CreateUserDto createUserDto, int httpStatus) {
        return RequestTemplate.postBody(requestSpec(),
                UsersEndpoints.ENDPOINT_USERS,
                createUserDto,
                httpStatus);
    }

    public static CreateUserDto defaultRequestBody() {
        return defaultRequestBody(Collections.emptyList());
    }

    public static CreateUserDto defaultRequestBody(List<String> friends) {
        String timeIndex = CommonDataGenerator.timeIndex();
        String userLogin = CommonDataGenerator.generatorString(timeIndex);
        String userName = CommonDataGenerator.generatorString(timeIndex);
        int userAge = PresentationDataGenerator.randomAge();
        String userGender = PresentationDataGenerator.GenderGenerator.randomGender();
        String userHairColor = PresentationDataGenerator.HairColorGenerator.randomHairColor();

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
