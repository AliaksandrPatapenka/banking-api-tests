package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.constants.HttpStatus;
import com.apiAuto.presentation.constants.schemasPatchs.AccountSchemas;
import com.apiAuto.presentation.constants.schemasPatchs.ErrorSchemas;
import com.apiAuto.presentation.helpers.accountHelper.AccountDb;
import com.apiAuto.presentation.helpers.accountHelper.AccountSteps;
import com.apiAuto.presentation.helpers.accountHelper.AccountTemplate;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class PostAccountWithdrawTest {
    @BeforeAll
    static void dbCleanup() {
        PresentationDbCleanup.deleteUsers();
        PresentationDbCleanup.deleteAccounts();
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
            AccountSteps.AccountContext ctx = AccountSteps.createAccount();
            BigDecimal startBalance = AccountSteps.depositAndGetBalance(ctx);
            BigDecimal debitAmount = PresentationDataGenerator.debitAmount(startBalance);

            AccountTemplate.accountWithdraw(ctx.accountId(),
                            debitAmount,
                            HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_WITHDRAW_SCHEMA));

            BigDecimal finishBalance = startBalance.subtract(debitAmount);
            AccountDb.assertAccountBalance(ctx.userLogin(), finishBalance);
        }

        @Test
        @Order(2)
        @DisplayName("Case 5.2: Списание всего баланса со счёта при достаточном балансе")
        void withdrawAll() {
            AccountSteps.AccountContext ctx = AccountSteps.createAccount();
            BigDecimal startBalance = AccountSteps.depositAndGetBalance(ctx);

            AccountTemplate.accountWithdraw(ctx.accountId(),
                            startBalance,
                            HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_WITHDRAW_SCHEMA));

            BigDecimal finishBalance = startBalance.subtract(startBalance);
            AccountDb.assertAccountBalance(ctx.userLogin(), finishBalance);
        }

        @Test
        @Order(3)
        @DisplayName("Case 5.3: Списание со счёта нулевого значения (баланс нет нулевой)")
        void withdrawZero() {
            AccountSteps.AccountContext ctx = AccountSteps.createAccount();
            BigDecimal startBalance = AccountSteps.depositAndGetBalance(ctx);
            BigDecimal debitAmount = new BigDecimal("0.00");

            AccountTemplate.accountWithdraw(ctx.accountId(),
                            debitAmount,
                            HttpStatus.OK)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_WITHDRAW_SCHEMA));

            AccountDb.assertAccountBalance(ctx.userLogin(), startBalance);
        }
    }

    /**
     * ==================== НЕГАТИВНЫЕ ТЕСТЫ ====================
     */
    @Nested
    @DisplayName("POST AccountsWithdraw. NegativeTests")
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NegativeTests {
        private static final String ERROR_400_SCHEMA = ErrorSchemas.ERROR_400_SCHEMA;

        @Test
        @Order(1)
        @DisplayName("Case 5.1: Списание суммы, превышающей текущий баланс (не нулевой)")
        void withdrawPartExceedingBalance() {
            AccountSteps.AccountContext ctx = AccountSteps.createAccount();
            BigDecimal startBalance = AccountSteps.depositAndGetBalance(ctx);
            BigDecimal debitAmount = startBalance.add(new BigDecimal("0.01"));

            AccountTemplate.accountWithdraw(ctx.accountId(),
                            debitAmount,
                            HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));

            AccountDb.assertAccountBalance(ctx.userLogin(), startBalance);
        }

        @Test
        @Order(2)
        @DisplayName("Case 5.2: Списание со счёта при нулевом балансе")
        void withdrawBalanceZero() {
            AccountSteps.AccountContext ctx = AccountSteps.createAccount();
            BigDecimal startBalance = AccountDb.getAccountBalance(ctx.userLogin());
            BigDecimal debitAmount = new BigDecimal("0.01");

            AccountTemplate.accountWithdraw(ctx.accountId(),
                            debitAmount,
                            HttpStatus.BAD_REQUEST)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ERROR_400_SCHEMA));

            AccountDb.assertAccountBalance(ctx.userLogin(), startBalance);
        }
    }
}
