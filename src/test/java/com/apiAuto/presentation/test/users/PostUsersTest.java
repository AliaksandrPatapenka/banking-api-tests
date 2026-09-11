package com.apiAuto.presentation.test.users;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.common.helpers.CommonDataGenerator;
import com.apiAuto.common.helpers.DbUtils;
import com.apiAuto.presentation.base.properties.patch.UsersPatch;
import com.apiAuto.common.helpers.JsonContext;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import com.apiAuto.presentation.helpers.userHelper.UserCreateTemplate;
import com.apiAuto.presentation.helpers.userHelper.UserJsonTemplate;
import com.apiAuto.presentation.models.users.UserCreate;
import org.junit.jupiter.api.*;

import java.util.*;

import static com.apiAuto.common.base.Specs.requestSpec;
import static com.apiAuto.common.base.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;


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

        @Test
        @DisplayName("Case 1.1: Создание пользователя без привязки друзей")
        void userCreate() {
            String timeIndex = CommonDataGenerator.timeIndex();
            String userLogin = CommonDataGenerator.generatorString(timeIndex);
            String userName = CommonDataGenerator.generatorString(timeIndex);
            int userAge = PresentationDataGenerator.randomAge();
            String userGender = PresentationDataGenerator.GenderGenerator.randomGender();
            String userHairColor = PresentationDataGenerator.HairColorGenerator.randomHairColor();
            List<String> userFriends = Collections.emptyList();

            UserCreate userCreate = new UserCreate();
            userCreate.setLogin(userLogin);
            userCreate.setName(userName);
            userCreate.setAge(userAge);
            userCreate.setGender(userGender);
            userCreate.setHairColor(userHairColor);
            userCreate.setFriends(userFriends);

            ApiSteps.post(requestSpec(), UsersPatch.ENDPOINT_USERS, userCreate, 200)
                    .then()
                    .body(matchesJsonSchemaInClasspath("schemas/presentation/userCrudSchema/userCreateSchema.json"));

            int count = ((Number) Objects.requireNonNull(DbUtils.getValue(
                    "SELECT COUNT(*) FROM users WHERE login = ?", userCreate.getLogin()
            ))).intValue();

            assertThat(count)
                    .as("Количество строк с login = " + userCreate.getLogin())
                    .isEqualTo(1);

            Map<String, Object> user = DbUtils.getRow("SELECT * FROM users WHERE login = ?", userCreate.getLogin());

            assertThat(user).isNotNull();
            assertThat(user.get("login")).isEqualTo(userCreate.getLogin());
            assertThat(user.get("name")).isEqualTo(userCreate.getName());
            assertThat(((Number) user.get("age")).intValue()).isEqualTo(userCreate.getAge());
            assertThat(user.get("gender")).isEqualTo(userCreate.getGender());
            assertThat(user.get("hair_color")).isEqualTo(userCreate.getHairColor());

        }

        @Test
        @DisplayName("Case 1.2: Создание пользователя C Привязкой 3 друзей")
        void userFriendsCreate() {
            List<String> friends = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                friends.add(UserCreateTemplate.userGetLogin());
            }

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

            ApiSteps.post(requestSpec(), UsersPatch.ENDPOINT_USERS, userCreate, 200)
                    .then()
                    .body(matchesJsonSchemaInClasspath("schemas/presentation/userCrudSchema/userCreateSchema.json"));

            int count = ((Number) Objects.requireNonNull(DbUtils.getValue(
                    "SELECT COUNT(*) FROM users WHERE login = ?", userCreate.getLogin()
            ))).intValue();

            assertThat(count)
                    .as("Количество строк с login = " + userCreate.getLogin())
                    .isEqualTo(1);

            Map<String, Object> user = DbUtils.getRow(
                    "SELECT * FROM users WHERE login = ?", userCreate.getLogin());

            assertThat(user).isNotNull();
            assertThat(user.get("login")).isEqualTo(userCreate.getLogin());
            assertThat(user.get("name")).isEqualTo(userCreate.getName());
            assertThat(((Number) user.get("age")).intValue()).isEqualTo(userCreate.getAge());
            assertThat(user.get("gender")).isEqualTo(userCreate.getGender());
            assertThat(user.get("hair_color")).isEqualTo(userCreate.getHairColor());

            long userId = ((Number) user.get("id")).longValue();
            List<Map<String, Object>> userFriends = DbUtils.getRows(
                    "SELECT * FROM user_friends WHERE user_id = ?", userId);
            List<String> actualLogins = userFriends.stream()
                    .map(f -> (String) f.get("friend_login"))
                    .toList();

            assertThat(actualLogins)
                    .as("Логины друзей пользователя: " + userCreate.getLogin())
                    .containsExactlyInAnyOrderElementsOf(userCreate.getFriends());


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
