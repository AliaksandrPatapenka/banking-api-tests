package com.apiAuto.presentation.test.users;

import com.apiAuto.common.config.HttpStatus;
import com.apiAuto.common.helpers.DbAssert;
import com.apiAuto.presentation.conctants.schemasPatchs.ErrorSchemas;
import com.apiAuto.presentation.conctants.schemasPatchs.UserSchemas;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserDbAssert;
import com.apiAuto.presentation.helpers.userHelper.UserSql;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import com.apiAuto.presentation.models.CreateUser;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class PostUsersTest {
    @BeforeAll
    static void dbCleanup() {
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
        private static final String USER_CREATE_SCHEMA = UserSchemas.USER_CREATE_SCHEMA;

        @Test
        @DisplayName("Case 1.1: Создание пользователя без привязки друзей")
        void createUser() {
            CreateUser createUser = UserTemplate.defaultRequestBody();

            UserTemplate.createUser(createUser, HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(USER_CREATE_SCHEMA));

            DbAssert.assertCount(UserSql.SELECT_USER_COUNT, createUser.getLogin(), 1);
            UserDbAssert.assertDataUser(createUser);
        }

        @Test
        @DisplayName("Case 1.2: Создание пользователя с привязкой 3 друзей")
        void friendsUserCreate() {
            List<String> friends = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                friends.add(UserTemplate.userGetLogin(HttpStatus.OK));
            }

            CreateUser createUser = UserTemplate.defaultRequestBody(friends);

            UserTemplate.createUser(createUser, HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(USER_CREATE_SCHEMA));

            DbAssert.assertCount(UserSql.SELECT_USER_COUNT, createUser.getLogin(), 1);
            UserDbAssert.assertDataUser(createUser);
            UserDbAssert.assertFriends(createUser);
        }
    }


    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("POST /users. NegativeTests")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NegativeTests {
        private static final String ERROR_400_SCHEMA = ErrorSchemas.ERROR_400_SCHEMA;
        private static final String ERROR_500_SCHEMA = ErrorSchemas.ERROR_500_SCHEMA;

        @Test
        @Order(1)
        @DisplayName("Case 1.1: Создание пользователя при отсутствии в запросе поля friends")
        void createUserStatus500() {
            CreateUser createUser = new CreateUser();

            UserTemplate.createUser(createUser, HttpStatus.INTERNAL_ERROR)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_500_SCHEMA));
        }

        @Test
        @Order(2)
        @DisplayName("Case 1.2: Создание пользователя с существующим в базе данных логином")
        void createUserStatus400() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);
            CreateUser createUser = UserTemplate.defaultRequestBody();
            createUser.setLogin(userLogin);

            UserTemplate.createUser(createUser, HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));
        }
    }
}
