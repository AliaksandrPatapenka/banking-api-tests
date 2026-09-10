package com.apiAuto.presentation.test.users;

import com.apiAuto.helpers.testHelper.commonDataGenerator;
import com.apiAuto.presentation.base.properties.patch.UsersPatch;
import com.apiAuto.helpers.testHelper.JsonContext;
import com.apiAuto.presentation.helpers.testHelper.presentationDataGenerator;
import com.apiAuto.presentation.helpers.userHelper.UserJsonTemplate;
import com.apiAuto.presentation.models.users.UserCreate;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Map;
import static com.apiAuto.common.base.Specs.requestSpec;
import static com.apiAuto.common.base.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class PostUsersTest {

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */

    @Nested
    @DisplayName("POST /users. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {

        @Test
        @DisplayName("Case 1.1: Создание пользователя")
        void userCreate() {
            String timeIndex = commonDataGenerator.timeIndex();
            UserCreate userCreate = new UserCreate();
            userCreate.setLogin(commonDataGenerator.generatorString(timeIndex));
            userCreate.setName(commonDataGenerator.generatorString(timeIndex));
            userCreate.setAge(presentationDataGenerator.randomAge());
            userCreate.setGender(presentationDataGenerator.GenderGenerator.randomGender());
            userCreate.setHairColor(presentationDataGenerator.HairColorGenerator.randomHairColor());
            userCreate.setFriends(Collections.emptyList());;

            given(requestSpec())
                    .body(userCreate)
                    .when()
                    .post(UsersPatch.ENDPOINT_USERS)
                    .then()
                    .spec(responseSpec())
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/presentation/userCrudSchema/userCreateSchema.json"));
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
            jsonRequest.remove("email");

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
