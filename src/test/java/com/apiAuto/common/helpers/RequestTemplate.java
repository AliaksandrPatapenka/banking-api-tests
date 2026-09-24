package com.apiAuto.common.helpers;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Шаблоны HTTP-запросов
 */

public class RequestTemplate {
    public static Response postQuery(RequestSpecification spec,
                                     String endpoint,
                                     Map<String, ?> queryParam,
                                     int httpStatus) {
        Response response = given(spec)
                .queryParams(queryParam)
                .when().post(endpoint)
                .then().extract().response();

        HttpAssert.httpStatusAssert(response, httpStatus);

        return response;
    }

    public static Response postBody(RequestSpecification spec,
                                    String endpoint,
                                    Object body,
                                    int httpStatus) {
        Response response = given(spec)
                .body(body)
                .when().post(endpoint)
                .then().extract().response();

        HttpAssert.httpStatusAssert(response, httpStatus);

        return response;
    }

    public static Response postBodyPatch(RequestSpecification spec,
                                         String endpoint,
                                         Map<String, ?> pathParam,
                                         Object body,
                                         int httpStatus) {
        Response response = given(spec)
                .pathParams(pathParam)
                .body(body)
                .when().post(endpoint)
                .then().extract().response();

        HttpAssert.httpStatusAssert(response, httpStatus);

        return response;
    }

    public static Response get(RequestSpecification spec,
                               Map<String, ?> pathParam,
                               String endpoint,
                               int httpStatus) {
        Response response = given(spec)
                .pathParams(pathParam)
                .when().get(endpoint)
                .then().extract().response();

        HttpAssert.httpStatusAssert(response, httpStatus);

        return response;
    }
}
