package com.apiAuto.common.helpers;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    public static Response post(RequestSpecification spec, String endpoint, Object body, int status) {
        return given(spec)
                .body(body)
                .when().post(endpoint)
                .then().statusCode(status)
                .extract().response();
    }

    public static Response get(RequestSpecification spec,
                               Map<String, ?> pathParam,
                               String endpoint,
                               int status) {
        return given(spec)
                .pathParams(pathParam)
                .when().get(endpoint)
                .then().statusCode(status)
                .extract().response();
    }
}
