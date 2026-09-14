package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserCreateTemplate;
import com.apiAuto.presentation.properties.patch.AccountPatch;
import org.junit.jupiter.api.*;

import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)

public class PostAccountsTest {

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
        private static final String ACCOUNT_CREATE_SCHEMA = "schemas/presentation/accountSchema/accountCreateSchema.json";

        @Test
        @DisplayName("Case 2.1: Создание счёта у пользователя, у которого отсутствуют счета")
        void createAccount() {
            String userLogin = UserCreateTemplate.userGetLogin();

            ApiSteps.post(requestSpec(),
                            AccountPatch.ENDPOINT_ACCOUNTS,
                            Map.of("userLogin", userLogin),
                            200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_CREATE_SCHEMA));

        }

    }
}
