package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.endpoints.AccountEndpoints;
import com.apiAuto.presentation.helpers.accountHelper.AccountDbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountTemplate;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import com.apiAuto.presentation.testData.AccountData;
import org.junit.jupiter.api.*;

import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AccountsDepositTest {

    @BeforeEach
    void dbCleanup() {
        PresentationDbCleanup.deleteUsers();
        PresentationDbCleanup.deleteAccounts();
    }

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */

    @Nested
    @DisplayName("POST /accounts/{id}/deposit. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {
        private static final String ACCOUNT_DEPOSIT_SCHEMA = "schemas/presentation/accountSchema/accountDepositSchema.json";

        @Test
        @Order(1)
        @DisplayName("Case 4.1: Пополнение счета (max значение)")
        void accountDepositMax() {
            String userLogin = UserTemplate.userGetLogin();

            AccountTemplate.createAccount(userLogin);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            ApiSteps.postPatchBody(requestSpec(),
                            AccountEndpoints.ENDPOINT_ACCOUNTS_DEPOSIT,
                            Map.of("id", accountId),
                            AccountData.ACCOUNT_DEPOSIT_MAX,
                            200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_DEPOSIT_SCHEMA));

            AccountDbAssert.assertAccountBalance(userLogin, AccountData.ACCOUNT_DEPOSIT_MAX);
        }

        @Test
        @Order(2)
        @DisplayName("Case 4.2: Пополнение счета (min значение)")
        void accountDepositMin() {
            String userLogin = UserTemplate.userGetLogin();

            AccountTemplate.createAccount(userLogin);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            ApiSteps.postPatchBody(requestSpec(),
                            AccountEndpoints.ENDPOINT_ACCOUNTS_DEPOSIT,
                            Map.of("id", accountId),
                            AccountData.ACCOUNT_DEPOSIT_MIN,
                            200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_DEPOSIT_SCHEMA));

            AccountDbAssert.assertAccountBalance(userLogin, AccountData.ACCOUNT_DEPOSIT_MIN);
        }

    }

    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("POST /accounts/{id}/deposit. NegativeTests")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NegativeTests {
        private static final String ERROR_SCHEMA = "schemas/errorSchema/errorSchema.json";

        @Test
        @Order(1)
        @DisplayName("Case 4.1: Пополнение счета (значение больше максимального)")
        void createUserStatus500() {
            String userLogin = UserTemplate.userGetLogin();
            AccountTemplate.createAccount(userLogin);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            ApiSteps.postPatchBody(requestSpec(),
                            AccountEndpoints.ENDPOINT_ACCOUNTS_DEPOSIT,
                            Map.of("id", accountId),
                            AccountData.ACCOUNT_DEPOSIT_ABOVE_MAX,
                            500)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
        }

        @Test
        @Order(2)
        @DisplayName("Case 4.2: Пополнение счета (отрицательное значение)")
        void createUserStatus400() {
            String userLogin = UserTemplate.userGetLogin();
            AccountTemplate.createAccount(userLogin);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            ApiSteps.postPatchBody(requestSpec(),
                            AccountEndpoints.ENDPOINT_ACCOUNTS_DEPOSIT,
                            Map.of("id", accountId),
                            AccountData.ACCOUNT_DEPOSIT_BELOW_ZERO,
                            400)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_SCHEMA));
        }
    }
}
