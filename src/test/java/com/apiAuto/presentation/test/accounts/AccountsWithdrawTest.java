package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.config.HttpStatus;
import com.apiAuto.presentation.conctants.schemasPatchs.AccountSchemas;
import com.apiAuto.presentation.conctants.schemasPatchs.ErrorSchemas;
import com.apiAuto.presentation.helpers.accountHelper.AccountDbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountTemplate;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import com.apiAuto.presentation.conctants.testData.AccountData;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AccountsWithdrawTest {
    @BeforeAll
    static void dbCleanup() {
        PresentationDbCleanup.deleteUsers();
        PresentationDbCleanup.deleteAccounts();
    }

    private record AccountContext(String userLogin, int accountId) {
    }

    private static class TestData {
        private static AccountContext createAccount() {
            String userLogin = UserTemplate.userGetLogin(HttpStatus.OK);
            AccountTemplate.createAccount(userLogin, HttpStatus.OK);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            return new AccountContext(userLogin, accountId);
        }

        private static BigDecimal depositAndGetBalance(AccountContext ctx) {
            AccountTemplate.accountDeposit(ctx.accountId(),
                    AccountData.ACCOUNT_DEPOSIT_MAX,
                    HttpStatus.OK);
            return AccountDbAssert.getAccountBalance(ctx.userLogin());
        }
    }

    /**
     * ==================== ПОЗИТИВНЫЕ ТЕСТЫ ====================
     */

    @Nested
    @DisplayName("POST /accounts/{id}/withdraw. PositiveTests")
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PositiveTests {
        private static final String ACCOUNT_WITHDRAW_SCHEMA = AccountSchemas.ACCOUNT_WITHDRAW_SCHEMA;

        @Test
        @Order(1)
        @DisplayName("Case 5.1: Списание части баланса со счёта при достаточном балансе")
        void withdrawPart() {
            AccountContext ctx = TestData.createAccount();
            BigDecimal startBalance = TestData.depositAndGetBalance(ctx);
            BigDecimal debitAmount = PresentationDataGenerator.debitAmount(startBalance);

            AccountTemplate.accountWithdraw(ctx.accountId(),
                            debitAmount,
                            HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_WITHDRAW_SCHEMA));

            BigDecimal finishBalance = startBalance.subtract(debitAmount);
            AccountDbAssert.assertAccountBalance(ctx.userLogin(), finishBalance);
        }

        @Test
        @Order(2)
        @DisplayName("Case 5.2: Списание всего баланса со счёта при достаточном балансе")
        void withdrawAll() {
            AccountContext ctx = TestData.createAccount();
            BigDecimal startBalance = TestData.depositAndGetBalance(ctx);

            AccountTemplate.accountWithdraw(ctx.accountId(),
                            startBalance,
                            HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_WITHDRAW_SCHEMA));

            BigDecimal finishBalance = startBalance.subtract(startBalance);
            AccountDbAssert.assertAccountBalance(ctx.userLogin(), finishBalance);
        }

        @Test
        @Order(3)
        @DisplayName("Case 5.3: Списание со счёта нулевого значения (баланс нет нулевой)")
        void withdrawZero() {
            AccountContext ctx = TestData.createAccount();
            BigDecimal startBalance = TestData.depositAndGetBalance(ctx);
            BigDecimal debitAmount = new BigDecimal("0.00");

            AccountTemplate.accountWithdraw(ctx.accountId(),
                            debitAmount,
                            HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_WITHDRAW_SCHEMA));

            AccountDbAssert.assertAccountBalance(ctx.userLogin(), startBalance);
        }
    }

    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("GET AccountsWithdraw. NegativeTests")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NegativeTests {
        private static final String ERROR_400_SCHEMA = ErrorSchemas.ERROR_400_SCHEMA;

        @Test
        @Order(1)
        @DisplayName("Case 5.1: Списание суммы, превышающей текущий баланс (не нулевой)")
        void withdrawPartExceedingBalance() {
            AccountContext ctx = TestData.createAccount();
            BigDecimal startBalance = TestData.depositAndGetBalance(ctx);
            BigDecimal debitAmount = startBalance.add(new BigDecimal("0.01"));

            AccountTemplate.accountWithdraw(ctx.accountId,
                            debitAmount,
                            HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));

            AccountDbAssert.assertAccountBalance(ctx.userLogin(), startBalance);
        }

        @Test
        @Order(2)
        @DisplayName("Case 5.2: Списание со счёта при нулевом балансе")
        void withdrawBalanceZero() {
            AccountContext ctx = TestData.createAccount();
            BigDecimal startBalance = AccountDbAssert.getAccountBalance(ctx.userLogin());
            BigDecimal debitAmount = new BigDecimal("0.01");

          AccountTemplate.accountWithdraw(ctx.accountId(),
                          debitAmount,
                          HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));

            AccountDbAssert.assertAccountBalance(ctx.userLogin(), startBalance);
        }
    }
}
