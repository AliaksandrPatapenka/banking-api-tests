package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.config.HttpStatus;
import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.common.helpers.DbAssert;
import com.apiAuto.presentation.conctants.endpoints.AccountEndpoints;
import com.apiAuto.presentation.conctants.queryParam.AccountQueryParam;
import com.apiAuto.presentation.conctants.schemasPatchs.AccountSchemas;
import com.apiAuto.presentation.conctants.schemasPatchs.ErrorSchemas;
import com.apiAuto.presentation.helpers.accountHelper.AccountDbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountSql;
import com.apiAuto.presentation.helpers.accountHelper.AccountTemplate;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import com.apiAuto.presentation.conctants.testData.UserData;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class PostAccountsTest {

    @BeforeAll
    static void dbCleanup() {
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
        private static final String ACCOUNT_CREATE_SCHEMA = AccountSchemas.ACCOUNT_CREATE_SCHEMA;

        @Test
        @Order(1)
        @DisplayName("Case 3.1: Создание счёта у пользователя, у которого отсутствуют счёта")
        void createAccount() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);

            AccountTemplate.createAccount(userLogin, HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_CREATE_SCHEMA));

            DbAssert.assertCount(AccountSql.SELECT_ACCOUNT_COUNT, userLogin, 1);
            AccountDbAssert.assertAccountBalance(userLogin, new BigDecimal("0"));
        }

        @Test
        @Order(2)
        @DisplayName("Case 3.2: Создание счёта у пользователя, у которого уже есть счёт")
        void createTwoAccount() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);

            for (int i = 0; i < 2; i++) {
                AccountTemplate.createAccount(userLogin, HttpStatus.OK)
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
        private static final String ERROR_400_SCHEMA = ErrorSchemas.ERROR_400_SCHEMA;

        @Test
        @Order(1)
        @DisplayName("Case 3.1: Создание счёта для несуществующего пользователя")
        void createUserStatus400() {
            String userLogin = UserData.LOGIN_NOT_EXIST;

            AccountTemplate.createAccount(userLogin, HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));
        }
    }
}
