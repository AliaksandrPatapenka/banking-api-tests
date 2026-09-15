package com.apiAuto.presentation.test.users;


import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import com.apiAuto.presentation.endpoints.UsersEndpoints;
import com.apiAuto.presentation.testData.UserData;
import org.junit.jupiter.api.*;

import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class GetUsersLoginTest {

    @BeforeEach
    void dbCleanup() {
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
        private static final String USER_BY_LOGIN_SCHEMA = "schemas/presentation/userSchema/userByLoginSchema.json";

        @Test
        @DisplayName("Case 2.1: Получение пользователя по существующему в БД логину")
        void getUserByLogin() {
            String userLogin = UserTemplate.userGetLogin();

            ApiSteps.get(requestSpec(),
                            Map.of("login", userLogin),
                            UsersEndpoints.ENDPOINT_USERS_BY_LOGIN,
                            200)
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
        @Test
        @DisplayName("Case 2.1: Получение пользователя по несуществующему в БД логину")
        void getUserByLoginNotExist() {
            String userLogin = UserData.LOGIN_NOT_EXIST;

            ApiSteps.get(requestSpec(),
                            Map.of("login", userLogin),
                            UsersEndpoints.ENDPOINT_USERS_BY_LOGIN,
                            400)
                    .then();
        }

    }
}
