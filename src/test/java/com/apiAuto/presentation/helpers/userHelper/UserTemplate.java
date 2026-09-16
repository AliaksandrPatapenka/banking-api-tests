package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.conctants.endpoints.UsersEndpoints;
import com.apiAuto.presentation.models.CreateUser;
import io.restassured.response.Response;

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

}
