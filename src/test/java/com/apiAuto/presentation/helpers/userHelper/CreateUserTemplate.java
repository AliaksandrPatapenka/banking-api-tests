package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.patchs.UsersPatch;

import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.RestAssured.given;

public class CreateUserTemplate {
    public static String userGetLogin() {
        Map<String, Object> jsonRequest = UserJsonTemplate.userJsonTemplate();
        String userLogin = (String) jsonRequest.get("login");

        ApiSteps.postBody(requestSpec(),
                UsersPatch.ENDPOINT_USERS,
                jsonRequest,
                200);

        return userLogin;
    }

}
