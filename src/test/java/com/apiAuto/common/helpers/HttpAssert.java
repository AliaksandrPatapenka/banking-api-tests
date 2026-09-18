package com.apiAuto.common.helpers;

import io.restassured.response.Response;

public class HttpAssert {
    public static void httpStatusAssert(Response response, int expected) {
        int actual = response.getStatusCode();
        if (actual != expected) {
            throw new AssertionError("Тело ответа: " + response.body().asString());
        }
    }
}
