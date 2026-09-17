package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.config.HttpStatus;
import com.apiAuto.presentation.conctants.schemasPatchs.AccountSchemas;
import com.apiAuto.presentation.conctants.schemasPatchs.ErrorSchemas;
import com.apiAuto.presentation.conctants.testData.AccountData;
import com.apiAuto.presentation.helpers.accountHelper.AccountDbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountTemplate;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import org.junit.jupiter.api.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class PostAccountDepositTest {

    @BeforeAll
    static void dbCleanup() {
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
        private static final String ACCOUNT_DEPOSIT_SCHEMA = AccountSchemas.ACCOUNT_DEPOSIT_SCHEMA;

        @Test
        @Order(1)
        @DisplayName("Case 4.1: Пополнение счета (max значение)")
        void accountDepositMax() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);

            AccountTemplate.createAccount(userLogin, HttpStatus.OK);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            AccountTemplate.accountDeposit(accountId,
                            AccountData.ACCOUNT_DEPOSIT_MAX,
                            HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_DEPOSIT_SCHEMA));

            AccountDbAssert.assertAccountBalance(userLogin, AccountData.ACCOUNT_DEPOSIT_MAX);
        }

        @Test
        @Order(2)
        @DisplayName("Case 4.2: Пополнение счета (min значение)")
        void accountDepositMin() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);

            AccountTemplate.createAccount(userLogin, HttpStatus.OK);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            AccountTemplate.accountDeposit(accountId,
                            AccountData.ACCOUNT_DEPOSIT_MIN,
                            HttpStatus.OK)
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
        private static final String ERROR_400_SCHEMA = ErrorSchemas.ERROR_400_SCHEMA;

        @Test
        @Order(2)
        @DisplayName("Case 4.1: Пополнение счета (отрицательное значение)")
        void createUserStatus400() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);
            AccountTemplate.createAccount(userLogin, HttpStatus.OK);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            AccountTemplate.accountDeposit(accountId,
                            AccountData.ACCOUNT_DEPOSIT_BELOW_ZERO,
                            HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));
        }
    }
}
