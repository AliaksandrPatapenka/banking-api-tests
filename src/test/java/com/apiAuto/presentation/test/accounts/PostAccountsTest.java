package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.common.helpers.DbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountDbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountSql;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.CreateUserTemplate;
import com.apiAuto.presentation.testData.UserData;
import com.apiAuto.presentation.patchs.AccountPatch;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class PostAccountsTest {

    @BeforeEach
    void dbCleanup() {
        PresentationDbCleanup.deleteUsers();
        PresentationDbCleanup.deleteAccounts();
    }

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("POST /accounts. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {
        private static final String ACCOUNT_CREATE_SCHEMA = "schemas/presentation/accountSchema/accountCreateSchema.json";

        @Test
        @Order(1)
        @DisplayName("Case 2.1: Создание счёта у пользователя, у которого отсутствуют счета")
        void createAccount() {
            String userLogin = CreateUserTemplate.userGetLogin();

            ApiSteps.postQuery(requestSpec(),
                            AccountPatch.ENDPOINT_ACCOUNTS,
                            Map.of("userLogin", userLogin),
                            200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_CREATE_SCHEMA));

            DbAssert.assertCount(AccountSql.SELECT_ACCOUNT_COUNT, userLogin, 1);
            AccountDbAssert.assertAccountBalance(userLogin, new BigDecimal("0"));
        }

        @Test
        @Order(2)
        @DisplayName("Case 2.2: Создание счёта у пользователя, у которого уже есть счет")
        void createTwoAccount() {
            String userLogin = CreateUserTemplate.userGetLogin();

            for (int i = 0; i < 2; i++) {
                ApiSteps.postQuery(requestSpec(),
                                AccountPatch.ENDPOINT_ACCOUNTS,
                                Map.of("userLogin", userLogin),
                                200)
                        .then()
                        .body(matchesJsonSchemaInClasspath(ACCOUNT_CREATE_SCHEMA));
            }

            DbAssert.assertCount(AccountSql.SELECT_ACCOUNT_COUNT, userLogin, 2);
            AccountDbAssert.assertAccountBalance(userLogin, new BigDecimal("0"));
        }
    }

    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("POST /accounts. NegativeTests")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NegativeTests {
        private static final String ERROR_SCHEMA = "schemas/errorSchema/errorSchema.json";

        @Test
        @Order(1)
        @DisplayName("Case2.1: В параметрах запроса передается неверный ключ")
        void createUserStatus500() {
            String userLogin = CreateUserTemplate.userGetLogin();

            ApiSteps.postQuery(requestSpec(),
                            AccountPatch.ENDPOINT_ACCOUNTS,
                            Map.of("keyLoginFalse", userLogin),
                            500)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
        }

        @Test
        @Order(2)
        @DisplayName("Case2.1: Создание счета для несуществующего пользователя")
        void createUserStatus400() {
            String userLogin = UserData.LOGIN_NOT_EXIST;

            ApiSteps.postQuery(requestSpec(),
                            AccountPatch.ENDPOINT_ACCOUNTS,
                            Map.of("userLogin", userLogin),
                            400)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
        }
    }
}
