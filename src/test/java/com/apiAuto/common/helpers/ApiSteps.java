package com.apiAuto.common.helpers;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiSteps {

    public static Response postQuery(RequestSpecification spec,
                                     String endpoint,
                                     Map<String, ?> queryParam,
                                     int status) {
        return given(spec)
                .queryParams(queryParam)
                .when().post(endpoint)
                .then().statusCode(status)
                .extract().response();
    }

    public static Response postBody(RequestSpecification spec,
                                    String endpoint,
                                    Object body,
                                    int status) {
        return given(spec)
                .body(body)
                .when().post(endpoint)
                .then().statusCode(status)
                .extract().response();
    }

    public static Response postPatchBody(RequestSpecification spec,
                                         String endpoint,
                                         Map<String, ?> pathParam,
                                         Object body,
                                         int status) {
        return given(spec)
                .pathParams(pathParam)
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
