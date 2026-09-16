package com.apiAuto.presentation.test.users;


import com.apiAuto.common.config.HttpStatus;
import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.conctants.endpoints.UsersEndpoints;
import com.apiAuto.presentation.conctants.queryParam.UserQueryParam;
import com.apiAuto.presentation.conctants.schemasPatchs.ErrorSchemas;
import com.apiAuto.presentation.conctants.schemasPatchs.UserSchemas;
import com.apiAuto.presentation.conctants.testData.UserData;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import org.junit.jupiter.api.*;

import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class GetUsersLoginTest {
    @BeforeAll
    static void dbCleanup() {
        PresentationDbCleanup.deleteFriends();
        PresentationDbCleanup.deleteUsers();
    }

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */

    @Nested
    @DisplayName("GET /users{login}. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {
        private static final String USER_BY_LOGIN_SCHEMA = UserSchemas.USER_BY_LOGIN_SCHEMA;

        @Test
        @DisplayName("Case 2.1: Получение пользователя по существующему в БД логину")
        void getUserByLogin() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);

            ApiSteps.get(requestSpec(),
                            Map.of(UserQueryParam.USER_LOGIN, userLogin),
                            UsersEndpoints.ENDPOINT_USERS_BY_LOGIN,
                            HttpStatus.OK)
                    .then()
                    .body("login", equalTo(userLogin))
                    .body(matchesJsonSchemaInClasspath(USER_BY_LOGIN_SCHEMA));
        }
    }

    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("GET /users{login}. NegativeTests")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NegativeTests {
        private static final String ERROR_400_SCHEMA = ErrorSchemas.ERROR_400_SCHEMA;

        @Test
        @DisplayName("Case 2.1: Получение пользователя по несуществующему в БД логину")
        void getUser400() {
            String userLogin = UserData.LOGIN_NOT_EXIST;

            ApiSteps.get(requestSpec(),
                            Map.of(UserQueryParam.USER_LOGIN, userLogin),
                            UsersEndpoints.ENDPOINT_USERS_BY_LOGIN,
                            HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));
        }
    }
}
