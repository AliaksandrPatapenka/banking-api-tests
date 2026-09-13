package com.apiAuto.presentation.test.users;

import com.apiAuto.common.helpers.*;
import com.apiAuto.presentation.properties.patch.UsersPatch;
import com.apiAuto.presentation.helpers.userHelper.UserSql;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import com.apiAuto.presentation.helpers.userHelper.UserCreateTemplate;
import com.apiAuto.presentation.helpers.userHelper.UserDbAssert;
import com.apiAuto.presentation.helpers.userHelper.UserJsonTemplate;
import com.apiAuto.presentation.models.users.UserCreate;
import org.junit.jupiter.api.*;

import java.util.*;

import static com.apiAuto.common.config.Specs.requestSpec;
import static com.apiAuto.common.config.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class PostUsersTest {

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */

    @BeforeEach
    void dbCleanup() {
        PresentationDbCleanup.deleteFriends();
        PresentationDbCleanup.deleteUsers();
    }
    @Nested
    @DisplayName("POST /users. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {
        private static UserCreate defaultRequestBody(){
            return defaultRequestBody(Collections.emptyList());
        }

        private static UserCreate defaultRequestBody(List<String> friends){
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


        @Test
        @DisplayName("Case 1.1: Создание пользователя без привязки друзей")
        void userCreate() {
            UserCreate userCreate = defaultRequestBody();

            ApiSteps.post(requestSpec(), UsersPatch.ENDPOINT_USERS, userCreate, 200)
                    .then()
                    .body(matchesJsonSchemaInClasspath("schemas/presentation/userSchema/userCreateSchema.json"));

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

            UserCreate userCreate = defaultRequestBody(friends);

            ApiSteps.post(requestSpec(), UsersPatch.ENDPOINT_USERS, userCreate, 200)
                    .then()
                    .body(matchesJsonSchemaInClasspath("schemas/presentation/userSchema/userCreateSchema.json"));

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

        @Test
        @Order(1)
        @DisplayName("Case1.1: Создание пользователя при отсутствии в запросе ключа email")
        void userCreateInvalid() {
            Map<String, Object> jsonRequest = UserJsonTemplate.userJsonTemplate();
            jsonRequest.remove("login");

            String requestBody = JsonContext.toJson(jsonRequest);

            given(requestSpec())
                    .body(requestBody)
                    .when()
                    .post(UsersPatch.ENDPOINT_USERS)
                    .then()
                    .spec(responseSpec())
                    .statusCode(400)
                    .body(matchesJsonSchemaInClasspath("schemas/errorSchema/400errorSchema.json"));
        }
    }
}
