package com.apiAuto.common.helpers;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    public static Response post(RequestSpecification spec, String endpoint, Object body, int status) {
        return given(spec)
                .body(body)
                .when().post(endpoint)
                .then().statusCode(status)
                .extract().response();
    }
}
