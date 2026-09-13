package com.apiAuto.presentation.test.users;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.common.helpers.CommonDataGenerator;
import com.apiAuto.common.helpers.DbAssert;
import com.apiAuto.common.helpers.JsonContext;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserCreateTemplate;
import com.apiAuto.presentation.helpers.userHelper.UserDbAssert;
import com.apiAuto.presentation.helpers.userHelper.UserJsonTemplate;
import com.apiAuto.presentation.helpers.userHelper.UserSql;
import com.apiAuto.presentation.models.users.UserCreate;
import com.apiAuto.presentation.properties.patch.UsersPatch;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class PostUsersTest {
    static class TestData {
        private static UserCreate defaultRequestBody() {
            return defaultRequestBody(Collections.emptyList());
        }

        private static UserCreate defaultRequestBody(List<String> friends) {
            String timeIndex = CommonDataGenerator.timeIndex();
            String userLogin = CommonDataGenerator.generatorString(timeIndex);
            String userName = CommonDataGenerator.generatorString(timeIndex);
            int userAge = PresentationDataGenerator.randomAge();
            String userGender = PresentationDataGenerator.GenderGenerator.randomGender();
            String userHairColor = PresentationDataGenerator.HairColorGenerator.randomHairColor();

            UserCreate userCreate = new UserCreate();
            userCreate.setLogin(userLogin);
            userCreate.setName(userName);
            userCreate.setAge(userAge);
            userCreate.setGender(userGender);
            userCreate.setHairColor(userHairColor);
            userCreate.setFriends(friends);

            return userCreate;
        }
    }

    @BeforeEach
    void dbCleanup() {
        PresentationDbCleanup.deleteFriends();
        PresentationDbCleanup.deleteUsers();
    }

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */

    @Nested
    @DisplayName("POST /users. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {
        private static final String  USER_CREATE_SCHEMA = "schemas/presentation/userSchema/userCreateSchema.json";

        @Test
        @DisplayName("Case 1.1: Создание пользователя без привязки друзей")
        void userCreate() {
            UserCreate userCreate = TestData.defaultRequestBody();

            ApiSteps.post(requestSpec(), UsersPatch.ENDPOINT_USERS, userCreate, 200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(USER_CREATE_SCHEMA));

            DbAssert.assertCount(UserSql.SELECT_USER_COUNT, userCreate.getLogin(), 1);

            UserDbAssert.assertDataUser(userCreate);

        }

        @Test
        @DisplayName("Case 1.2: Создание пользователя C Привязкой 3 друзей")
        void userFriendsCreate() {
            List<String> friends = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                friends.add(UserCreateTemplate.userGetLogin());
            }

            UserCreate userCreate = TestData.defaultRequestBody(friends);

            ApiSteps.post(requestSpec(), UsersPatch.ENDPOINT_USERS, userCreate, 200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(USER_CREATE_SCHEMA   ));

            DbAssert.assertCount(UserSql.SELECT_USER_COUNT, userCreate.getLogin(), 1);
            UserDbAssert.assertDataUser(userCreate);
            UserDbAssert.assertFriends(userCreate);
        }


    }


    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */

    @Nested
    @DisplayName("POST /users. NegativeTests")
    @Order(2)
    class NegativeTests {
        private  static final String ERROR_SCHEMA = "schemas/errorSchema/errorSchema.json";

        @Test
        @Order(1)
        @DisplayName("Case1.1: Создание пользователя при отсутствии в запросе поля friends")
        void userCreateStatus500() {
            Map<String, Object> jsonRequest = UserJsonTemplate.userJsonTemplate();
            jsonRequest.remove("friends");

            String requestBody = JsonContext.toJson(jsonRequest);

            ApiSteps.post(requestSpec(), UsersPatch.ENDPOINT_USERS, requestBody, 500)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
        }

        @Test
        @Order(2)
        @DisplayName("Case1.2: Создание пользователя с существующим в базе данных логином")
        void userCreateStatus400() {
            String userLogin = UserCreateTemplate.userGetLogin();
            UserCreate userCreate = TestData.defaultRequestBody();
            userCreate.setLogin(userLogin);

            ApiSteps.post(requestSpec(), UsersPatch.ENDPOINT_USERS, userCreate, 400)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
        }
    }
}
