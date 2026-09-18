package com.apiAuto.presentation.test.users;

import com.apiAuto.common.constants.HttpStatus;
import com.apiAuto.common.helpers.DbAssert;
import com.apiAuto.presentation.constants.schemasPatchs.ErrorSchemas;
import com.apiAuto.presentation.constants.schemasPatchs.UserSchemas;
import com.apiAuto.presentation.constants.sql.UserSql;
import com.apiAuto.presentation.dto.CreateUserDto;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserDb;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
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
            CreateUserDto requestBody = UserTemplate.defaultRequestBody();

            UserTemplate.createUser(requestBody, HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(USER_CREATE_SCHEMA));

            DbAssert.assertCount(UserSql.SELECT_USER_COUNT, requestBody.getLogin(), 1);
            UserDb.assertDataUser(requestBody);
        }

        @Test
        @DisplayName("Case 1.2: Создание пользователя с привязкой 3 друзей")
        void friendsUserCreate() {
            List<String> friends = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                friends.add(UserTemplate.userGetLogin(HttpStatus.OK));
            }

            CreateUserDto createUserDto = UserTemplate.defaultRequestBody(friends);

            UserTemplate.createUser(createUserDto, HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(USER_CREATE_SCHEMA));

            DbAssert.assertCount(UserSql.SELECT_USER_COUNT, createUserDto.getLogin(), 1);
            UserDb.assertDataUser(createUserDto);
            UserDb.assertFriends(createUserDto);
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

        @Test
        @Order(2)
        @DisplayName("Case 1.2: Создание пользователя с существующим в базе данных логином")
        void createUserStatus400() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);
            CreateUserDto createUserDto = UserTemplate.defaultRequestBody();
            createUserDto.setLogin(userLogin);

            UserTemplate.createUser(createUserDto, HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));
        }
    }
}
