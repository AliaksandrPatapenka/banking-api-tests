package com.apiAuto.presentation.helpers.userHelper;

import com.apiAuto.common.helpers.RequestTemplate;
import com.apiAuto.presentation.constants.endpoints.UsersEndpoints;
import com.apiAuto.presentation.dto.CreateUserDto;
import io.restassured.response.Response;

import static com.apiAuto.common.config.Specs.requestSpec;


/**
 * Шаблоны для работы с пользователем
 */

public class UserTemplate {
    public static String userGetLogin(int httpStatus) {
        CreateUserDto requestBody = DefaultRequestBody.defaultRequestBody();
        createUser(requestBody, httpStatus);
        return requestBody.getLogin();
    }

    public static Response createUser(CreateUserDto createUserDto, int httpStatus) {
        return RequestTemplate.postBody(requestSpec(),
                UsersEndpoints.ENDPOINT_USERS,
                createUserDto,
                httpStatus);
    }
}
