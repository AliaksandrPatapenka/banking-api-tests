package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.presentation.base.properties.patch.UsersPatch;
import com.apiAuto.common.helpers.JsonContext;
import io.restassured.response.Response;

import java.util.Map;

import static com.apiAuto.common.base.Specs.requestSpec;
import static com.apiAuto.common.base.Specs.responseSpec;
import static io.restassured.RestAssured.given;

public class UserCreateTemplate {

    public static Response userCreateTemplate() {
        Map<String, Object> jsonRequest = UserJsonTemplate.userJsonTemplate();
        String requestBody = JsonContext.toJson(jsonRequest);

        return given(requestSpec())
                .body(requestBody)
                .when()
                .post(UsersPatch.ENDPOINT_USERS)
                .then()
                .spec(responseSpec())
                .extract().response();
    }

    public static String userGetLogin(){
        Map<String, Object> jsonRequest = UserJsonTemplate.userJsonTemplate();
        String requestBody = JsonContext.toJson(jsonRequest);
        String userLogin = (String) jsonRequest.get("login");

        given(requestSpec())
                .body(requestBody)
                .when()
                .post(UsersPatch.ENDPOINT_USERS)
                .then()
                .spec(responseSpec())
                .statusCode(200);

        return userLogin;

    }

}
