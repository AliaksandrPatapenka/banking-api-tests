package com.apiAuto.presentation.test.accounts;

import com.apiAuto.common.helpers.ApiSteps;
import com.apiAuto.presentation.endpoints.AccountEndpoints;
import com.apiAuto.presentation.helpers.accountHelper.AccountDbAssert;
import com.apiAuto.presentation.helpers.accountHelper.AccountTemplate;
import com.apiAuto.presentation.helpers.testHelper.PresentationDataGenerator;
import com.apiAuto.presentation.helpers.testHelper.PresentationDbCleanup;
import com.apiAuto.presentation.helpers.userHelper.UserTemplate;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Map;

import static com.apiAuto.common.config.Specs.requestSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AccountsWithdrawTest {
    private record AccountContext(String userLogin, int accountId) {
    }

    private static class TestData {
        private static AccountContext createAccount() {
            String userLogin = UserTemplate.userGetLogin();
            AccountTemplate.createAccount(userLogin);
            int accountId = AccountDbAssert.getAccountId(userLogin);

            return new AccountContext(userLogin, accountId);
        }

        private static BigDecimal depositAndGetBalance(AccountContext ctx) {
            AccountTemplate.accountDeposit(ctx.accountId());
            return AccountDbAssert.getAccountBalance(ctx.userLogin());
        }
    }


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
        private static final String ACCOUNT_DEPOSIT_SCHEMA = "schemas/presentation/accountSchema/accountWithdrawSchema.json";

        @Test
        @Order(1)
        @DisplayName("Case 5.1: Списание части баланса со счёта при достаточном балансе")
        void withdrawPart() {
            AccountContext ctx = TestData.createAccount();
            BigDecimal startBalance = TestData.depositAndGetBalance(ctx);
            BigDecimal debitAmount = PresentationDataGenerator.debitAmount(startBalance);

            ApiSteps.postPatchBody(requestSpec(),
                            AccountEndpoints.ENDPOINT_ACCOUNTS_WITHDRAW,
                            Map.of("id", ctx.accountId),
                            debitAmount,
                            200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_DEPOSIT_SCHEMA));

            BigDecimal finishBalance = startBalance.subtract(debitAmount);
            AccountDbAssert.assertAccountBalance(ctx.userLogin(), finishBalance);
        }

        @Test
        @Order(2)
        @DisplayName("Case 5.1: Списание всего баланса со счёта при достаточном балансе")
        void withdrawAll() {
            AccountContext ctx = TestData.createAccount();
            BigDecimal startBalance = TestData.depositAndGetBalance(ctx);
            ApiSteps.postPatchBody(requestSpec(),
                            AccountEndpoints.ENDPOINT_ACCOUNTS_WITHDRAW,
                            Map.of("id", ctx.accountId),
                            startBalance,
                            200)
                    .then()
                    .body(matchesJsonSchemaInClasspath(ACCOUNT_DEPOSIT_SCHEMA));

            BigDecimal finishBalance = startBalance.subtract(startBalance);
            AccountDbAssert.assertAccountBalance(ctx.userLogin(), finishBalance);
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

    }
}
